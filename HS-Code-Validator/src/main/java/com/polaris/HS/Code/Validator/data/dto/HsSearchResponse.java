package com.polaris.HS.Code.Validator.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HsSearchResponse {
    private String code;
    private Integer level;
    private String description;
    private String parentCode;
    private String section;
}
