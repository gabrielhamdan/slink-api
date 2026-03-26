package com.hamdan.slinkapi.controller;

import com.hamdan.slinkapi.dto.PaginationDto;
import com.hamdan.slinkapi.dto.SlinkDetailDto;
import com.hamdan.slinkapi.dto.SlinkRequestDto;
import com.hamdan.slinkapi.dto.SlinkResponseDto;
import com.hamdan.slinkapi.entity.user.ApiUser;
import com.hamdan.slinkapi.entity.user.User;
import com.hamdan.slinkapi.service.LinkService;
import com.hamdan.slinkapi.service.QrCodeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
public class LinkController {

    private final LinkService linkService;

    private final QrCodeService qrCodeService;

    public LinkController(LinkService linkService, QrCodeService qrCodeService) {
        this.linkService = linkService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/qrcode")
    public ResponseEntity<String> toQrCode(@RequestParam("link") @Valid @URL String link, @RequestParam(name = "width", required = false) @Min(1) Integer width) {
        return ResponseEntity.ok(qrCodeService.generateQrCode(link, width));
    }

    @PostMapping
    public ResponseEntity<SlinkResponseDto> shorten(@AuthenticationPrincipal User user, @RequestBody @Valid SlinkRequestDto req) {
        return ResponseEntity.ok(linkService.shorten(user, req));
    }

    @GetMapping("/{slink}")
    public ResponseEntity<Void> resolve(@PathVariable String slink) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, linkService.resolve(slink))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('API_USER')")
    public ResponseEntity<PaginationDto<SlinkDetailDto>> findAll(@AuthenticationPrincipal ApiUser apiUser, @PageableDefault(sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(linkService.findAll(apiUser, pageable));
    }

}
