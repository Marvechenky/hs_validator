package com.polaris.HS.Code.Validator.data.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimatedDutiesVatDto {

    private String importDuty;
    private String vat;
    private String levy;
    private String totalEstimated;

    private Map<String, String> additionalCharges = new HashMap<>();

    @JsonAnySetter
    public void handleUnknown(String key, String value){
        additionalCharges.put(key, value);
    }
}
