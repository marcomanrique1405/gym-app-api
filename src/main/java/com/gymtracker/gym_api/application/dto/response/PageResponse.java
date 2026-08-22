package com.gymtracker.gym_api.application.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public PageResponse {
        content = List.copyOf(content);
    }
}
