package com.example.pasteservice.controller;

import com.example.pasteservice.controller.exception.PasteNotFoundException;
import com.example.pasteservice.controller.payload.NewPastePayload;
import com.example.pasteservice.controller.payload.UpdatePastePayload;
import com.example.pasteservice.entity.Paste;
import com.example.pasteservice.service.PasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("paste-api/")
public class PasteController {

    private final PasteService pasteService;

    private final MessageSource messageSource;

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
        return paste.orElseThrow(() -> new NoSuchElementException("pastebin.errors.paste.not_found"));
    }

    @PostMapping("{userId}/create")
    public ResponseEntity<?> createPaste(@Valid @RequestBody NewPastePayload payload,
                                         @PathVariable Long userId,
                                         BindingResult bindingResult,
                                         UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Paste paste = this.pasteService.createPaste(payload.text(), userId);
            return ResponseEntity
                    .created(uriBuilder
                            .replacePath("/paste-api/find-by-id/{id}")
                            .build(Map.of("id", paste.getId())))
                    .body(paste);
        }
    }

    @PatchMapping("update/{pasteId}")
    public ResponseEntity<?> updatePaste(@PathVariable("pasteId") Long pasteId,
                                         @Valid @RequestBody UpdatePastePayload payload,
                                         BindingResult bindingResult
                                         ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.pasteService.updatePaste(pasteId, payload.text());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("delete/{pasteId}")
    public ResponseEntity<?> deletePaste(@PathVariable("pasteId") Long pasteId) {
        this.pasteService.deletePaste(pasteId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception,
                                                                      Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(exception.getMessage(), new Object[0],
                                exception.getMessage(), locale)));
    }



}
