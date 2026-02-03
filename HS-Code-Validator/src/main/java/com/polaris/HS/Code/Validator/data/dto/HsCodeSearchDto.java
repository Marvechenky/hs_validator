package com.polaris.HS.Code.Validator.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HsCodeSearchDto {
    private String code;
    private String description;
    private Integer level;
    private String parentCode;
    private String section;
    private Double score;
}
