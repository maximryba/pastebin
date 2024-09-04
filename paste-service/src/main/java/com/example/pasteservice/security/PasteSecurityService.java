package com.example.pasteservice.security;

import com.example.pasteservice.repository.PasteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasteSecurityService {

    private final PasteRepository pasteRepository;

    public boolean isOwner(Long pasteId, String username) {
        return pasteRepository.findById(pasteId)
                .map(paste -> paste.getUser().getUsername().equals(username))
                .orElse(false);
    }

}
