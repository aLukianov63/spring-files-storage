package com.alukianov.FileServer.models;

import lombok.Builder;

@Builder
public record Response(
        Integer status,
        String message,
        Object payload
) {
}
