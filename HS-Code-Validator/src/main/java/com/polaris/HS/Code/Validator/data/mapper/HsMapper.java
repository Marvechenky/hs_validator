package com.polaris.HS.Code.Validator.data.mapper;

import com.polaris.HS.Code.Validator.data.model.HsCode;
import com.polaris.HS.Code.Validator.data.dto.HsSearchResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HsMapper {

    public static HsSearchResponse toDto(HsCode hsCode) {
        return new HsSearchResponse(
                hsCode.getCode(),
                hsCode.getLevel(),
                hsCode.getDescription(),
                hsCode.getParentCode(),
                hsCode.getSection()
        );
    }
}
