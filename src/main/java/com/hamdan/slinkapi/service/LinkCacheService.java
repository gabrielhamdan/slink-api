package com.hamdan.slinkapi.service;

import com.hamdan.slinkapi.infra.exception.ApiErrorException;
import com.hamdan.slinkapi.infra.redis.cache.RedisConfig;
import com.hamdan.slinkapi.infra.redis.cache.SlinkCache;
import com.hamdan.slinkapi.repository.LinkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LinkCacheService {

    private final LinkRepository linkRepository;

    private final StringRedisTemplate redisTemplate;

    private final RedisConfig redisConfig;

    public LinkCacheService(LinkRepository linkRepository, StringRedisTemplate redisTemplate, RedisConfig redisConfig) {
        this.linkRepository = linkRepository;
        this.redisTemplate = redisTemplate;
        this.redisConfig = redisConfig;
    }

    public void incrementClicks(String slink) {
        final var key = "slink:" + slink;
        redisTemplate.opsForHash().increment(key, "clickCount", 1);
        redisTemplate.opsForHash().put(key, "lastAccessedAt", String.valueOf(Instant.now().toEpochMilli()));
        redisTemplate.expire(key, Duration.ofSeconds(redisConfig.CACHE_DURATION));
    }

    @Cacheable(value = "slinks", key = "#slink")
    public String getOriginalCachedUrl(String slink) {
        var url = linkRepository.findBySlink(slink).orElseThrow(EntityNotFoundException::new);

        if (url.isExpired()) {
            linkRepository.delete(url);

            throw new ApiErrorException(HttpStatus.GONE, "Link expirado.");
        }

        return url.getOriginalUrl();
    }

    @Scheduled(fixedRateString = "${app.cache.click_sync_rate}")
    public void syncClicks() {
        List<SlinkCache> links = getCachedSlinks();

        links.forEach(l -> {
            if (l.getClickCount() > 0) {
                var slink = linkRepository.findBySlink(l.getSlink()).orElse(null);

                if (slink != null) {
                    slink.incrementClicks(l.getClickCount());
                    slink.setLastAccessedAt(l.getLastAccessedAt());
                    linkRepository.save(slink);
                }
            }
        });
    }

    private List<SlinkCache> getCachedSlinks() {
        List<SlinkCache> links = new ArrayList<>();

        ScanOptions options = ScanOptions.scanOptions().match("slink:*").count(100).build();

        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
                .getConnection()
                .scan(options)) {

            while (cursor.hasNext()) {
                var key = new String(cursor.next());

                var map = redisTemplate.opsForHash().entries(key);

                int clicks = map.get("clickCount") != null
                        ? Integer.parseInt((String) map.get("clickCount"))
                        : 0;

                long lastAccess = map.get("lastAccessedAt") != null
                        ? Long.parseLong((String) map.get("lastAccessedAt"))
                        : 0L;

                SlinkCache cache = new SlinkCache(key, clicks, LocalDateTime.ofInstant(Instant.ofEpochMilli(lastAccess), ZoneOffset.of("-03:00")));

                links.add(cache);

                resetClicks(key);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return links;
    }

    private void resetClicks(String key) {
        redisTemplate.opsForHash().put(key, "clickCount", "0");
    }

}
