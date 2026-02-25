package com.polaris.HS.Code.Validator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polaris.HS.Code.Validator.data.dto.ProductDescriptionResponse;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductIdentificationService {

    private final RestTemplate restTemplate;
    private final ChatLanguageModel chatLanguageModel;
    @Value("${google.gemini.api-key:}")
    private String geminiApiKey;


    public ProductDescriptionResponse identify(String barcode,
                                               MultipartFile image,
                                               String originCountry,
                                               String destinationCountry) {

        String productInfo;

        if (image != null && !image.isEmpty()) {
            productInfo = detectProduct(image);
        } else if (barcode != null && !barcode.isBlank()) {
            productInfo = lookUpProduct(barcode);
        } else {
            throw new IllegalArgumentException("Provide image or barcode");
        }

        String finalPrompt = createPrompt(productInfo, originCountry, destinationCountry, barcode);

        String json = chatLanguageModel.chat(finalPrompt);

        log.info("AI RAW JSON: {}", json);

        return ProductDescriptionResponse.from(json);
    }

    public String detectProduct(MultipartFile file) {
        try {
            String base64Image = Base64.getEncoder()
                    .encodeToString(file.getBytes());

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(Map.of(
                            "parts", List.of(
                                    Map.of("text", "Identify this product from its packaging. " +
                                            "Provide the brand and model number."),
                                    Map.of("inline_data", Map.of(
                                            "mime_type", "image/jpeg",
                                            "data", base64Image
                                    ))
                            )
                    ))
            );

            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";
            String fullUrl = apiUrl + geminiApiKey;;

            ResponseEntity<String> response =
                    restTemplate.postForEntity(fullUrl, requestBody, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            log.error("Gemini Vision failed", e);
            throw new RuntimeException("Gemini Vision failed", e);
        }
    }

    public String lookUpProduct(String barcode) {
        String generalInfo = lookUpGeneralGoods(barcode);

        if (generalInfo != null) {
            return generalInfo;
        }

        String foodInfo = lookUpFoodFacts(barcode);
        if (foodInfo != null) {
            return foodInfo;
        }

        throw new IllegalArgumentException("Product not found: Barcode " + barcode + "does not exist in the system");
    }

    private String lookUpGeneralGoods(String barcode) {
        try {
            String url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + barcode;
            JsonNode root = restTemplate.getForEntity(url, JsonNode.class).getBody();

            if (root != null && root.path("items").size() > 0) {
                JsonNode item = root.path("items").get(0);
                return String.format("Product: %s, Brand: %s, Description: %s",
                        item.path("title").asText(),
                        item.path("brand").asText(),
                        item.path("description").asText());
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Barcode {} not found in UPCitemdb. Falling back...", barcode);
        } catch (Exception e) {
            log.error("UPCitemdb API Error: {}", e.getMessage());
        }
        return null;
    }

    private String lookUpFoodFacts(String barcode) {
        try {
            String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
            JsonNode root = restTemplate.getForEntity(url, JsonNode.class).getBody();

            if (root != null && root.path("status").asInt() == 1) {
                JsonNode p = root.path("product");
                return String.format("Food Product: %s, Brand: %s, Categories: %s",
                        p.path("product_name").asText(),
                        p.path("brands").asText(),
                        p.path("categories").asText());
            }
        } catch (Exception e) {
            log.error("OpenFoodFacts API Error: {}", e.getMessage());
        }
        return null;
    }

    private String createPrompt(String productDescription,
                                String originCountry,
                                String destinationCountry,
                                String barcode) {

        String countryContext = (originCountry != null && !originCountry.isBlank()
                && destinationCountry != null && !destinationCountry.isBlank())
                ? "for " + destinationCountry + " customs regulations"
                : "for general international customs classification";

        return """
        Analyze the following product information:

        %s

        Return a VALID JSON object using EXACTLY this structure:

        {
          "officialProductName": string,
          "shortDescription": string,
          "category": string,
          "internationalHsCode": string,
          "countrySpecificHsCode": string,
          "importRequirements": array of strings,
          "estimatedDutiesVat": {
              "importDuty": string,
              "vat": string,
              "levy": string,
              "totalEstimated": string
          },
          "requiredCertifications": array of strings,
          "aiConfidenceScore": string,
          "notes": string,
          "barcode": string
        }

        Rules:
        - ALWAYS return all fields.
        - If unknown, use empty string "" or empty array [].
        - importRequirements MUST be an array.
        - requiredCertifications MUST be an array.
        - estimatedDutiesVat MUST be an object.
        - Do NOT include additional fields.
        - Do NOT include markdown.
        - Do NOT include explanation.
        - Return ONLY valid JSON.

        Use this barcode value: "%s"
        Context: %s.
        """.formatted(productDescription,
                barcode == null ? "" : barcode,
                countryContext);
    }
}
