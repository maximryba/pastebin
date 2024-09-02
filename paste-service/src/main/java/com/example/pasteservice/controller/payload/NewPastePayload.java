package com.example.pasteservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewPastePayload(
        @NotNull(message = "{pastebin.pastes.create.errors.text_is_null}")
                @Size(max = 5000, message = "{pastebin.pastes.create.errors.size_is_too_big}")
        String text) {
}
