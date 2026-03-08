package com.hamdan.slinkapi.repository;

import com.hamdan.slinkapi.entity.user.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {

    Optional<ApiUser> findByUserName(String userName);

}
