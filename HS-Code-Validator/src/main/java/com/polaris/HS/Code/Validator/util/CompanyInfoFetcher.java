package com.polaris.HS.Code.Validator.util;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class CompanyInfoFetcher {

    public static List<String> fetchCreseadaWebsiteInfo() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String html = restTemplate.getForObject("https://www.creseada.com", String.class);

            if (html == null || html.isEmpty()) {
                return Collections.singletonList("Company info not available at the moment.");
            }

            return HtmlParser.parseSections(html, "div.about, section.about, .about");

        } catch (Exception e) {
            return Collections.singletonList("Company info not available at the moment.");
        }
    }
}