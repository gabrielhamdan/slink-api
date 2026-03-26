package com.hamdan.slinkapi.infra.redis.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SlinkCache {

    private String slink;

    private int clickCount;

    private LocalDateTime lastAccessedAt;

    public String getSlink() {
        return slink.replace("slink:", "");
    }
}