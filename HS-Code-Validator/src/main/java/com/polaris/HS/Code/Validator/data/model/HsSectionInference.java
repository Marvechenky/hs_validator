package com.polaris.HS.Code.Validator.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hs_section_inference")
@Getter
@NoArgsConstructor
public class HsSectionInference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_code", nullable = false)
    private String sectionCode;

    @Column(name = "section_vector", columnDefinition = "tsvector", nullable = false)
    private String sectionVector;

}
