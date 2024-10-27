package org.demo.parcialmagneto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.demo.parcialmagneto.validators.ValidDna;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {
    @ValidDna
    private String[] dna;
}