package com.hamdan.slinkapi.repository;

import com.hamdan.slinkapi.entity.url.Url;
import com.hamdan.slinkapi.entity.user.ApiUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Url, Long> {

    @Query("SELECT nextval('url_seq')")
    Long nextVal();

    Optional<Url> findBySlink(String slink);

    boolean existsBySlink(String slink);

    Page<Url> findAllByUser(ApiUser apiUser, Pageable pageable);

}
