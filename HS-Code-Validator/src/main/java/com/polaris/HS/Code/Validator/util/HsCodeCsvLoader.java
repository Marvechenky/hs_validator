package com.polaris.HS.Code.Validator.util;

import com.polaris.HS.Code.Validator.data.model.HsCode;
import com.polaris.HS.Code.Validator.repository.HsCodeRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class HsCodeCsvLoader implements ApplicationRunner {

    private final HsCodeRepository hsCodeRepository;

    public HsCodeCsvLoader(HsCodeRepository hsCodeRepository) {
        this.hsCodeRepository = hsCodeRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (hsCodeRepository.count() > 0) {
            return;
        }

        InputStream is = new ClassPathResource("data/harmonized-system.csv").getInputStream();

        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .setIgnoreEmptyLines(true)
                .build();


        CSVParser csvParser = new CSVParser(reader, format);

        List<HsCode> batch = new ArrayList<>();

        for (CSVRecord record : csvParser) {

            HsCode hs = new HsCode();
            hs.setSection(record.get("section"));
            hs.setCode(record.get("hscode"));
            hs.setDescription(record.get("description"));
            hs.setParentCode(
                    "TOTAL".equals(record.get("parent")) ? null : record.get("parent")
            );
            hs.setLevel(Integer.parseInt(record.get("level")));

            batch.add(hs);

            if (batch.size() == 500) {
                hsCodeRepository.saveAll(batch);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            hsCodeRepository.saveAll(batch);
        }
    }
}
