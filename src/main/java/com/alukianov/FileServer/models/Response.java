package com.alukianov.FileServer.models;

import lombok.Builder;

@Builder
public record Response(
        String message,
        Object payload
) { }
