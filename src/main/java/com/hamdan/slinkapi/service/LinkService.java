package com.hamdan.slinkapi.service;

import com.hamdan.slinkapi.dto.PaginationDto;
import com.hamdan.slinkapi.dto.SlinkDetailDto;
import com.hamdan.slinkapi.dto.SlinkRequestDto;
import com.hamdan.slinkapi.dto.SlinkResponseDto;
import com.hamdan.slinkapi.entity.url.Url;
import com.hamdan.slinkapi.entity.user.ApiUser;
import com.hamdan.slinkapi.entity.user.User;
import com.hamdan.slinkapi.infra.exception.ApiAssert;
import com.hamdan.slinkapi.repository.LinkRepository;
import com.hamdan.slinkapi.util.encoding.Base62;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class LinkService {

    private final String BASE_URL;

    private final LinkRepository linkRepository;

    private final LinkCacheService linkCacheService;

    public LinkService(@Value("${slink.api.base_url}") String baseUrl, LinkRepository linkRepository, LinkCacheService linkCacheService) {
        BASE_URL = baseUrl;
        this.linkRepository = linkRepository;
        this.linkCacheService = linkCacheService;
    }

    public SlinkResponseDto shorten(User user, SlinkRequestDto req) {
        var url = Url.from(linkRepository.nextVal(), user, req.url());

        if (user instanceof ApiUser && req.customAlias() != null) {
            ApiAssert.isTrue(!linkRepository.existsBySlink(req.customAlias()));

            url.setSlink(req.customAlias());
        } else
            url.setSlink(Base62.encode(url.getId()));

        url = linkRepository.save(url);

        var slink = String.format("%s/%s", BASE_URL, url.getSlink());

        return new SlinkResponseDto(slink);
    }

    public String resolve(String slink) {
        linkCacheService.incrementClicks(slink);

        return linkCacheService.getOriginalCachedUrl(slink);
    }

    public PaginationDto<SlinkDetailDto> findAll(ApiUser apiUser, Pageable pageable) {
        var page = linkRepository.findAllByUser(apiUser, pageable);

        return new PaginationDto<>(page, SlinkDetailDto::new);
    }

}
