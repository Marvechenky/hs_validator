package com.polaris.HS.Code.Validator.service;

import com.polaris.HS.Code.Validator.util.NameStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogisticsAssistantDispatcher {

    private final LogisticsAssistant logisticsAssistant;

    public String handleInquiry(String memoryId, String message) {

        if (!NameStore.hasName(memoryId) && isName(message)) {
            NameStore.storeName(memoryId, message);
            return "You're welcome, " + message + ". How can I assist you today?";
        }

        String name = NameStore.getName(memoryId);
        String enrichedMessage = name != null ? "User's name is " + name + ". " + message : message;

        return logisticsAssistant.chat(memoryId, enrichedMessage);
    }


    private boolean isName(String message) {
        String[] words = message.trim().split("\\s+");
        return words.length == 1 && Character.isUpperCase(words[0].charAt(0));
    }

}
