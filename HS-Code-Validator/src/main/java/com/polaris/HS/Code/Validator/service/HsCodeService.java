package com.polaris.HS.Code.Validator.service;

import com.polaris.HS.Code.Validator.data.dto.HsCodeSearchDto;
import com.polaris.HS.Code.Validator.data.model.HsCode;
import com.polaris.HS.Code.Validator.data.dto.HsValidationResponse;
import com.polaris.HS.Code.Validator.repository.HsCodeRepository;
import com.polaris.HS.Code.Validator.util.HsCodeNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class HsCodeService {

    private final HsCodeRepository hsCodeRepository;
    private final HsSectionInferenceService hsSectionInferenceService;


    public HsValidationResponse validate(String rawCode, String country) {
        String normalizedCode = HsCodeNormalizer.normalize(rawCode);

        if (!normalizedCode.matches("\\d{2,10}")) {
            throw new IllegalArgumentException("Invalid HS code format");
        }

        boolean isExtension = normalizedCode.length() > 6;
        Optional<HsCode> hsCodeOpt = hsCodeRepository.findByCode(normalizedCode);

        if (hsCodeOpt.isEmpty()) {
            return HsValidationResponse.invalid(rawCode, normalizedCode);
        }
        HsCode hsCode = hsCodeOpt.get();

        return HsValidationResponse.builder()
                .code(rawCode)
                .normalizedCode(normalizedCode)
                .valid(true)
                .level(hsCode.getLevel())
                .description(hsCode.getDescription())
                .section(hsCode.getSection())
                .parentCode(hsCode.getParentCode())
                .type(isExtension ? "EXTENSION" : "HS")
                .country(country)
                .build();
    }

    public List<HsCodeSearchDto> search(String query, int limit) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String cleaned = query.trim().toLowerCase();

        String inferredSection = hsSectionInferenceService.detectSection(query);
        System.out.println("Inferred Section: " + inferredSection);

        List<Object[]> results = hsCodeRepository.searchWithSectionBoost(
                cleaned, inferredSection, limit);

        return results.stream()
                .map(r -> new HsCodeSearchDto(
                        (String) r[0],
                        (String) r[1],
                        (Integer) r[2],
                        (String) r[3],
                        (String) r[4],
                        ((Number) r[5]).doubleValue()
                ))
                .toList();
    }

}

