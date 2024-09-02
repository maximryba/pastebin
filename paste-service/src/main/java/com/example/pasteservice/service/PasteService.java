package com.example.pasteservice.service;

import com.example.pasteservice.entity.Paste;

import java.util.List;
import java.util.Optional;

public interface PasteService {
    List<Paste> getAllPastes();

    List<Paste> getPastesByUserId(Long id);

    Optional<Paste> getPasteById(Long id);

    Paste createPaste(String text, Long userId);

    void updatePaste(Long id, String text);

    void deletePaste(Long id);
}
