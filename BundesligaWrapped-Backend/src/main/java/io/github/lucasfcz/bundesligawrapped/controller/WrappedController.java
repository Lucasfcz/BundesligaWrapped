package io.github.lucasfcz.bundesligawrapped.controller;

import io.github.lucasfcz.bundesligawrapped.dto.WrappedResponseDTO;
import io.github.lucasfcz.bundesligawrapped.service.WrappedGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wrapped")
@RequiredArgsConstructor
public class WrappedController {

    private final WrappedGeneratorService wrappedGeneratorService;

    @GetMapping("/{userId}")
    public ResponseEntity<WrappedResponseDTO> getWrapped(@PathVariable String userId) {
        return ResponseEntity.ok(wrappedGeneratorService.generate(userId));
    }
}