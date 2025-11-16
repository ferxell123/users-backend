package com.springboot.backend.andres.usersapp.usersbackend.pageable;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
        List<T> content,
        Metadata metadata) {

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                new Metadata(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast(),
                        page.isFirst()));
    }

    public record Metadata(
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last,
            boolean first) {

    }
}
