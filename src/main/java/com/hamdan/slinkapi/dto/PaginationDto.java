package com.hamdan.slinkapi.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PaginationDto<D>(List<D> content, int page, int totalPages, boolean hasPrevious, boolean hasNext) {

    public <T> PaginationDto(Page<T> page, Function<T, D> mapper) {
        this(
                page.stream().map(mapper).toList(),
                page.getNumber(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

}
