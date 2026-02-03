package com.polaris.HS.Code.Validator.service;

import com.polaris.HS.Code.Validator.data.dto.HsCodeSearchDto;
import com.polaris.HS.Code.Validator.data.model.HsCode;
import com.polaris.HS.Code.Validator.data.dto.HsSearchResponse;
import com.polaris.HS.Code.Validator.data.dto.HsValidationResponse;
import com.polaris.HS.Code.Validator.data.mapper.HsMapper;
import com.polaris.HS.Code.Validator.repository.HsCodeRepository;
import com.polaris.HS.Code.Validator.util.HsCodeNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HsCodeService {

    private final HsCodeRepository hsCodeRepository;

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

//    @Cacheable(
//            value = "hsDescriptionSearch",
//            key = "#query.toLowerCase() + ':' + #limit"
//    )
//    public List<HsSearchResponse> search(String query, int limit){
//
//        Pageable pageable = PageRequest.of(0, limit);
//
//        return hsCodeRepository
//                .searchByDescription(query, 0.2f, pageable)
//                .getContent()
//                .stream()
//                .map(HsMapper::toDto)
//                .toList();
//    }

    public List<HsCodeSearchDto> search(String query, int limit) {
        return hsCodeRepository.search(query, 0.2, limit)
                .stream()
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
