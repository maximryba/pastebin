package com.example.pasteservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewUserPayload(
        @NotNull(message = "{pastebin.users.create.errors.username_is_null}")
        @Size(min = 4, max = 50, message = "{pastebin.users.create.errors.username_size_is_invalid}")
        String username,
        @NotNull(message = "{pastebin.users.create.errors.password_is_null}")
        @Size(min = 4, max = 30, message = "{pastebin.users.create.errors.password_size_is_invalid}")
        String password
) {
}
