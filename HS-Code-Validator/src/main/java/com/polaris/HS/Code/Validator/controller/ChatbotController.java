package com.polaris.HS.Code.Validator.controller;

import com.polaris.HS.Code.Validator.data.dto.ChatRequestDto;
import com.polaris.HS.Code.Validator.data.dto.ProductDescriptionResponse;
import com.polaris.HS.Code.Validator.service.LogisticsAssistantDispatcher;
import com.polaris.HS.Code.Validator.service.ProductIdentificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class ChatbotController {

    private final LogisticsAssistantDispatcher logisticsAssistantDispatcher;
    private final ProductIdentificationService productIdentificationService;

    @GetMapping("/welcome")
    @Operation(summary = "Welcome message by chatbot")
    public ResponseEntity<String> welcome(HttpSession session){
        return ResponseEntity.ok(
                "Hello, I am Seada Assistant. What's your name?");
    }

    @PostMapping("/chatbot")
    @Operation(summary = "chat with Seda Assistant")
    public ResponseEntity<String> chat(
            @RequestBody ChatRequestDto chatRequestDto,
            HttpSession httpSession) {

        String sessionId = httpSession.getId();

        String response =
                logisticsAssistantDispatcher.handleInquiry(
                        sessionId,
                        chatRequestDto.getMessage());
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "search-by-barcode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "search using image or barcode")
    public ResponseEntity<ProductDescriptionResponse> identifyProduct(
            @RequestParam(required = false) String barcode,
            @RequestPart(required = false) MultipartFile image,
            @RequestParam() String originCountry,
            @RequestParam() String destinationCountry) {

        ProductDescriptionResponse response =
        productIdentificationService.identify(barcode, image, originCountry, destinationCountry);

        return ResponseEntity.ok(response);
    }
}
