package com.haiilo.interview.haiilosupermarketcheckout.api.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}