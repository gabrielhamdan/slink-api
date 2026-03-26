package com.hamdan.slinkapi.dto;

import com.hamdan.slinkapi.entity.url.Url;

import java.time.LocalDateTime;

public record SlinkDetailDto(Long id, String originalUrl, String slink, LocalDateTime expiresAt, Integer clickCount, LocalDateTime lastAccessedAt) {

    public SlinkDetailDto(Url url) {
        this(url.getId(), url.getOriginalUrl(), url.getSlink(), url.getExpiresAt(), url.getClickCount(), url.getLastAccessedAt());
    }

}
