package com.polaris.HS.Code.Validator.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.polaris.HS.Code.Validator.util.FlexibleStringListDeserializer;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "officialProductName",
        "shortDescription",
        "category",
        "internationalHsCode",
        "countrySpecificHsCode",
        "importRequirements",
        "estimatedDutiesVat",
        "requiredCertifications",
        "aiConfidenceScore",
        "notes",
        "barcode"
})
public class ProductDescriptionResponse {

    private String officialProductName;
    private String shortDescription;
    private String category;
    private String internationalHsCode;
    private String countrySpecificHsCode;

    @JsonDeserialize(using = FlexibleStringListDeserializer.class)
    private List<String> importRequirements;

    private EstimatedDutiesVatDto estimatedDutiesVat;

    @JsonDeserialize(using = FlexibleStringListDeserializer.class)
    private List<String> requiredCertifications;

    private String aiConfidenceScore;
    private String notes;
    private String barcode;


    public static ProductDescriptionResponse from(String aiRawResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            String cleaned = aiRawResponse.trim();

            int firstBrace = cleaned.indexOf("{");
            int lastBrace = cleaned.lastIndexOf("}");

            if (firstBrace == -1 || lastBrace == -1) {
                throw new IllegalArgumentException("No JSON object found in AI response");
            }

            cleaned = cleaned.substring(firstBrace, lastBrace + 1);

            return mapper.readValue(cleaned, ProductDescriptionResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}



