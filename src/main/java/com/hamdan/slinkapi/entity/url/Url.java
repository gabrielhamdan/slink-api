package com.hamdan.slinkapi.entity.url;

import com.hamdan.slinkapi.entity.user.ApiUser;
import com.hamdan.slinkapi.entity.user.User;
import com.hamdan.slinkapi.infra.exception.ApiErrorException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Url {

    public static final int API_USER_LINK_DURATION = 30;
    public static final int ANON_USER_LINK_DURATION = 1;

    @Id
    private Long id;

    private String originalUrl;

    private String slink;

    private LocalDateTime expiresAt;

    private Integer clickCount;

    private LocalDateTime lastAccessedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApiUser user;

    public static Url from(Long id, User user, String originalUrl) {
        var url = new Url();
        url.setId(id);
        url.setOriginalUrl(originalUrl);

        if (user instanceof ApiUser)
            url.setUser((ApiUser) user);

        return url;
    }

    @PrePersist
    public void onPrePersist() {
        if (user != null)
            expiresAt = LocalDateTime.now().plusDays(API_USER_LINK_DURATION);
        else
            expiresAt = LocalDateTime.now().plusDays(ANON_USER_LINK_DURATION);
    }

    public void incrementClicks(int clicks) {
        if (clickCount == null)
            clickCount = 0;

        clickCount += clicks;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

}
