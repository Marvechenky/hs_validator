package com.polaris.HS.Code.Validator.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "hs_national_extension",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_national_code_country",
                columnNames = {"code", "country_code"})})
public class HsNationalExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(name = "hs6_code", nullable = false, length = 6)
    private String hs6Code;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer level;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
