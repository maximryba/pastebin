package com.example.pasteservice.service;

import com.example.pasteservice.entity.Paste;
import com.example.pasteservice.repository.PasteRepository;
import com.example.pasteservice.repository.UserRepository;
import com.example.pasteservice.service.util.LinkGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasteServiceImpl implements PasteService {

    private final PasteRepository pasteRepository;

    private final UserRepository userRepository;

    @Override
    public List<Paste> getAllPastes() {
        return this.pasteRepository.findAll();
    }

    @Override
    public List<Paste> getPastesByUserId(Long id) {
        return this.pasteRepository.findAllByUserId(id);
    }

    @Override
    public Optional<Paste> getPasteById(Long id) {
        return this.pasteRepository.findById(id);
    }

    @Override
    @Transactional
    public Paste createPaste(String text, Long userId) {

        String link = LinkGenerator.generateRandomLink();

        Paste paste = Paste.builder()
                .text(text)
                .user(this.userRepository.findById(userId).orElse(null))
                .link(link)
                .build();
        return this.pasteRepository.save(paste);
    }

    @Override
    @Transactional
    @PreAuthorize("@pasteSecurityService.isOwner(#id, authentication.name)")
    public void updatePaste(Long id, String text) {
        this.pasteRepository.findById(id)
                .ifPresentOrElse(paste -> paste.setText(text), () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional
    @PreAuthorize("@pasteSecurityService.isOwner(#id, authentication.name)")
    public void deletePaste(Long id) {
        this.pasteRepository.deleteById(id);
    }
}
