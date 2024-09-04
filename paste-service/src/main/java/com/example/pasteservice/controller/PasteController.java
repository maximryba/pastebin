package com.example.pasteservice.controller;

import com.example.pasteservice.controller.exception.PasteNotFoundException;
import com.example.pasteservice.controller.payload.NewPastePayload;
import com.example.pasteservice.controller.payload.UpdatePastePayload;
import com.example.pasteservice.entity.Paste;
import com.example.pasteservice.service.PasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("paste-api/")
public class PasteController {

    private final PasteService pasteService;

    @GetMapping("all")
    public List<Paste> getAll() {
        return this.pasteService.getAllPastes();
    }

    @GetMapping("find-by-user-id/{userId}")
    public List<Paste> findByUserId(@PathVariable Long userId) {
        return this.pasteService.getPastesByUserId(userId);
    }

    @GetMapping("find-by-id/{pasteId}")
    public Paste findById(@PathVariable Long pasteId) {
        Optional<Paste> paste = this.pasteService.getPasteById(pasteId);
        return paste.orElseThrow(() -> new PasteNotFoundException("Paste with id " + pasteId + " not found."));
    }

    @PostMapping("{userId}/create")
    public ResponseEntity<?> createPaste(@Valid @RequestBody NewPastePayload payload,
                                         @PathVariable Long userId,
                                         UriComponentsBuilder uriBuilder) {
        Paste paste = this.pasteService.createPaste(payload.text(), userId);
        return ResponseEntity
                .created(uriBuilder
                        .replacePath("/paste-api/find-by-id/{id}")
                        .build(Map.of("id", paste.getId())))
                .body(paste);
    }

    @PatchMapping("update/{pasteId}")
    public ResponseEntity<?> updatePaste(@PathVariable("pasteId") Long pasteId,
                                         @Valid @RequestBody UpdatePastePayload payload
                                         ) {
        this.pasteService.updatePaste(pasteId, payload.text());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("delete/{pasteId}")
    public ResponseEntity<?> deletePaste(@PathVariable("pasteId") Long pasteId) {
        this.pasteService.deletePaste(pasteId);
        return ResponseEntity.noContent().build();
    }



}
