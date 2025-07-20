package com.example.demo.parse;

import com.example.demo.dto.HotspotEvent;
import com.opencsv.CSVReader;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.StringReader;
import java.util.List;

@Component
public class HotspotParse {

    @SneakyThrows
    public static Flux<HotspotEvent> parse(String csv) {
        try (CSVReader reader = new CSVReader(new StringReader(csv))) {
            List<String[]> all = reader.readAll();

            // pula o header (linha 0)
            return Flux.fromIterable(all)
                    .skip(1)
                    .map(cols -> {
                        double lat        = Double.parseDouble(cols[0]);
                        double lon        = Double.parseDouble(cols[1]);
                        String confidence = cols[2];
                        String satTs      = cols[3];
                        return new HotspotEvent(satTs, lat, lon, confidence);
                    });
        }
    }
}
