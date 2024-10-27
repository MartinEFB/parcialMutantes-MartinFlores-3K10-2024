package org.demo.parcialmagneto.services;

import org.demo.parcialmagneto.entities.Dna;
import org.demo.parcialmagneto.repositories.DnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class DnaService {

    private final DnaRepository dnaRepository;
    private static final int SEQUENCE_LENGTH = 4; // Longitud de la secuencia a buscar

    @Autowired
    public DnaService(DnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    public boolean isMutant(String[] dna) {
        AtomicInteger sequenceCount = new AtomicInteger(0);
        int n = dna.length;

        // Verificar filas
        for (int row = 0; row < n; row++) {
            sequenceCount.addAndGet(hasRepeatedSequence(dna[row]));
            // Parada temprana si ya se encontró más de una secuencia mutante
            if (sequenceCount.get() > 1) return true;
        }

        // Verificar columnas
        for (int col = 0; col < n; col++) {
            StringBuilder columnData = new StringBuilder();
            for (int i = 0; i < n; i++) {
                columnData.append(dna[i].charAt(col));
            }
            sequenceCount.addAndGet(hasRepeatedSequence(columnData.toString()));
            // Parada temprana si ya se encontró más de una secuencia mutante
            if (sequenceCount.get() > 1) return true;
        }

        // Verificar diagonales
        checkDiagonals(dna, sequenceCount);

        // Retornar true si se encontró más de una secuencia mutante
        return sequenceCount.get() > 1;
    }



    private int hasRepeatedSequence(String s) {
        Set<String> seenSequences = new HashSet<>();
        int count = 0;

        for (int i = 0; i <= s.length() - SEQUENCE_LENGTH; i++) {
            String sequence = s.substring(i, i + SEQUENCE_LENGTH);

            // Verificamos si la secuencia no ha sido vista antes y es una secuencia mutante
            if (seenSequences.add(sequence) && sequence.chars().distinct().count() == 1) {
                count++; // Aumentar el contador si encontramos una nueva secuencia mutante
            }
        }
        return count; // Retornar el conteo de secuencias mutantes encontradas
    }


    private void checkDiagonals(String[] dna, AtomicInteger sequenceCount) {
        int n = dna.length;

        // Diagonales principales (de arriba-izquierda a abajo-derecha)
        for (int i = 0; i <= n - SEQUENCE_LENGTH; i++) {
            for (int j = 0; j <= n - SEQUENCE_LENGTH; j++) {
                StringBuilder diagonal = new StringBuilder();
                for (int k = 0; k < SEQUENCE_LENGTH; k++) {
                    diagonal.append(dna[i + k].charAt(j + k));
                }
                sequenceCount.addAndGet(hasRepeatedSequence(diagonal.toString()));
                // Parada temprana si ya se encontró más de una secuencia mutante
                if (sequenceCount.get() > 1) return;
            }
        }

        // Diagonales secundarias (de arriba-derecha a abajo-izquierda)
        for (int i = 0; i <= n - SEQUENCE_LENGTH; i++) {
            for (int j = SEQUENCE_LENGTH - 1; j < n; j++) {
                StringBuilder diagonal = new StringBuilder();
                for (int k = 0; k < SEQUENCE_LENGTH; k++) {
                    diagonal.append(dna[i + k].charAt(j - k));
                }
                sequenceCount.addAndGet(hasRepeatedSequence(diagonal.toString()));
                // Parada temprana si ya se encontró más de una secuencia mutante
                if (sequenceCount.get() > 1) return;
            }
        }
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
