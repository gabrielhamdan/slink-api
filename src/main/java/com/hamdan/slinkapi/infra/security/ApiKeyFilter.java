package com.hamdan.slinkapi.infra.security;

import com.hamdan.slinkapi.entity.user.AnonUser;
import com.hamdan.slinkapi.entity.user.ApiUser;
import com.hamdan.slinkapi.entity.user.User;
import com.hamdan.slinkapi.infra.exception.ApiErrorException;
import com.hamdan.slinkapi.repository.ApiUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiUserRepository apiUserRepository;

    private  final PasswordEncoder passwordEncoder;

    public ApiKeyFilter(ApiUserRepository apiUserRepository, PasswordEncoder passwordEncoder) {
        this.apiUserRepository = apiUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var userName = request.getHeader("userName");
        var apiKey = ApiKey.get(request);

        var user = apiUserRepository.findByUserName(userName)
                .filter(u -> passwordEncoder.matches(apiKey, u.getApiKey()))
                .map(u -> (User) u)
                .orElse(new AnonUser(request.getRemoteAddr()));

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

}
