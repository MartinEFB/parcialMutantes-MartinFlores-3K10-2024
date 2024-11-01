package org.demo.parcialmagneto.services;

import org.demo.parcialmagneto.entities.Dna;
import org.demo.parcialmagneto.repositories.DnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class DnaService {

    private final DnaRepository dnaRepository;
    private static final int SEQUENCE_LENGTH = 4; // Longitud de la secuencia a buscar

    @Autowired
    public DnaService(DnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    public boolean isMutant(String[] dna) {
        int n = dna.length;
        int sequenceCount = 0;

        // Verificar filas y columnas
        for (int i = 0; i < n; i++) {
            // Verificar fila
            if (hasRepeatedSequence(dna[i])) {
                sequenceCount++;
            }
            // Verificar columna
            StringBuilder columnData = new StringBuilder();
            for (int j = 0; j < n; j++) {
                columnData.append(dna[j].charAt(i));
            }
            if (hasRepeatedSequence(columnData.toString())) {
                sequenceCount++;
            }
            // Parada temprana
            if (sequenceCount > 1) return true;
        }

        // Verificar diagonales
        sequenceCount += checkDiagonals(dna);
        return sequenceCount > 1;
    }

    private int checkDiagonals(String[] dna) {
        int sequenceCount = 0;
        int n = dna.length;

        // Diagonales principales y secundarias en un solo bucle
        for (int i = 0; i <= n - SEQUENCE_LENGTH; i++) {
            for (int j = 0; j <= n - SEQUENCE_LENGTH; j++) {
                // Diagonal principal
                if (isMutantDiagonal(dna, i, j, false)) { // Validación para diagonal principal
                    sequenceCount++;
                }
                // Diagonal secundaria
                if (j >= SEQUENCE_LENGTH - 1 && isMutantDiagonal(dna, i, j, true)) { // Validar j para diagonal secundaria
                    sequenceCount++;
                }
                // Parada temprana
                if (sequenceCount > 1) return sequenceCount;
            }
        }
        return sequenceCount;
    }

    private boolean isMutantDiagonal(String[] dna, int i, int j, boolean secondary) {
        char c = dna[i].charAt(j);
        if (secondary) {
            // Verificar si no se sale del límite para diagonal secundaria
            return c == dna[i + 1].charAt(j - 1) && c == dna[i + 2].charAt(j - 2) && c == dna[i + 3].charAt(j - 3);
        } else {
            // Verificar si no se sale del límite para diagonal principal
            return c == dna[i + 1].charAt(j + 1) && c == dna[i + 2].charAt(j + 2) && c == dna[i + 3].charAt(j + 3);
        }
    }



    private boolean hasRepeatedSequence(String s) {
        Set<String> seenSequences = new HashSet<>();
        int n = s.length();

        // Hash Rolling para encontrar secuencias
        for (int i = 0; i <= n - SEQUENCE_LENGTH; i++) {
            String sequence = s.substring(i, i + SEQUENCE_LENGTH);
            if (seenSequences.contains(sequence)) {
                return true; // Secuencia encontrada
            }
            seenSequences.add(sequence);
        }
        return false; // Ninguna secuencia repetida
    }

    public boolean analyzeDna(String[] dna) {
        String dnaSequence = String.join(",", dna);

        // Verificamos si el ADN ya existe en la base de datos
        Optional<Dna> existingDna = dnaRepository.findByDna(dnaSequence);
        if (existingDna.isPresent()) {
            // Si el ADN ya fue analizado, retornamos el resultado existente
            return existingDna.get().isMutant();
        }
        // Determinamos si el ADN es mutante
        boolean isMutant = this.isMutant(dna);
        // Guardamos el resultado en la base de datos
        Dna dnaEntity = Dna.builder()
                .dna(dnaSequence)
                .isMutant(isMutant)
                .build();
        dnaRepository.save(dnaEntity);

        return isMutant;
    }
}
