package com.polaris.HS.Code.Validator.config;

import com.polaris.HS.Code.Validator.service.LiveCustomsFetcher;
import com.polaris.HS.Code.Validator.service.LogisticsAssistant;
import com.polaris.HS.Code.Validator.util.CompanyInfoFetcher;
import com.polaris.HS.Code.Validator.util.NameStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class GeminiConfig {

    @Value("${google.gemini.api-key:}")
    private String geminiApiKey;

    @Value("${gemini.model.name:gemini-2.5-flash}")
    private String modelName;

    private final LiveCustomsFetcher liveCustomsFetcher;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            throw new IllegalArgumentException("Gemini API key cannot be null or blank");
        }

        return GoogleAiGeminiChatModel.builder()
                .apiKey(geminiApiKey)
                .modelName(modelName)
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(10);
    }

    @Bean
    public LogisticsAssistant logisticsAssistantBean
            (ChatLanguageModel chatLanguageModel,

             ChatMemoryProvider chatMemoryProvider) {

        String liveUpdates = String.join("\n", liveCustomsFetcher.fetchLatestUpdates());
        String companyInfo = String.join("\n", CompanyInfoFetcher.fetchCreseadaWebsiteInfo());

        return AiServices.builder(LogisticsAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .systemMessageProvider(memoryId -> {
                    String name = NameStore.getName((String) memoryId);
                    if (name == null) {
                        name = "unknown";
                    }
                    return """
                        You are Seada Assistant, the AI for Creseada International Limited.
                        
                        The user's name is %s.
                        
                        Always respond politely and address them by name when appropriate.
                        
                        After greeting and acknowledging the name, continue by answering the user's question clearly and professionally.
                        
                        Provide guidance on logistics, shipping, customs, tariffs, duties, and taxes.
                        When giving regulatory information, clearly state that it may change and should be verified with customs authorities or Creseada support.
                        
                        Refer users to https://www.creseada.com/contact-us for human support when necessary.
                        
                        Company Info(from website):
                        %s
                        
                        Latest Customs Updates:
                        %s
                        """.formatted(name, companyInfo,liveUpdates);
                })
                .build();
    }
}
