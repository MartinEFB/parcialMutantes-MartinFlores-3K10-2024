package org.demo.parcialmagneto.controllers;

import org.demo.parcialmagneto.dto.DnaRequest;
import org.demo.parcialmagneto.services.DnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mutant")
@Validated
public class DnaController {

    private final DnaService dnaService;

    @Autowired
    public DnaController(DnaService dnaService) {
        this.dnaService = dnaService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> isMutant(@RequestBody DnaRequest dnaRequest) {
        boolean isMutant = dnaService.analyzeDna(dnaRequest.getDna()); // Llamar a analyzeDna
        return isMutant ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}