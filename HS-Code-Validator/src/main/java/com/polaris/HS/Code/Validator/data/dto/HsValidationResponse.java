package com.polaris.HS.Code.Validator.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class HsValidationResponse {
    private String code;
    private String normalizedCode;
    private boolean valid;
    private Integer level;
    private String description;
    private String section;
    private String parentCode;
    private String country;
    private String type;

    public static HsValidationResponse invalid(String raw, String normalized) {
        return HsValidationResponse.builder()
                .code(raw)
                .normalizedCode(normalized)
                .valid(false)
                .build();
    }
}
