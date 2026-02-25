package com.polaris.HS.Code.Validator.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlParser {

    private static final Logger log = LoggerFactory.getLogger(HtmlParser.class);

    public static List<String> parseSections(String html, String selector) {
        if (html == null || html.isEmpty()) {
            log.warn("Empty HTML content");
            return Collections.emptyList();
        }

        try {
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select(selector);

            if (elements.isEmpty()) {
                log.warn("No elements found for selector: {}", selector);
                return Collections.emptyList();
            }

            return elements.stream()
                    .map(Element::text)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error parsing HTML", e);
            return Collections.emptyList();
        }
    }
}