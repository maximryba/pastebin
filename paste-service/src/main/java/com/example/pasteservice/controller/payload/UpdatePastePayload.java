package com.example.pasteservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePastePayload(
        @NotNull(message = "{pastebin.pastes.update.errors.text_is_null}")
        @Size(max = 5000, message = "{pastebin.pastes.update.errors.size_is_too_big}")
        String text
) {
}
