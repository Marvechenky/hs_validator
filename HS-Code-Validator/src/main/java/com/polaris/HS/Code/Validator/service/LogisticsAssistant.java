package com.polaris.HS.Code.Validator.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface LogisticsAssistant {

    String chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
