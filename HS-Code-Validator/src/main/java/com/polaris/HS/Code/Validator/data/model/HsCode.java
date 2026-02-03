package com.polaris.HS.Code.Validator.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Table(name = "hs_code")
@Getter
@Setter
public class HsCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10, unique = true)
    private String code;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private String description;

    @Column(name = "parent_code", length = 10)
    private String parentCode;

    @Column(nullable = false, length = 5)
    private String section;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
