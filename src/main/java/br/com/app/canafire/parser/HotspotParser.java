package br.com.app.canafire.parser;

import br.com.app.canafire.model.Hotspot;
import reactor.core.publisher.Flux;

public class HotspotParser {

    public static Flux<Hotspot> parse(String csvContent) {
        return Flux
                .fromArray(csvContent.split("\\r?\\n"))
                .skip(1) // pula cabeçalho
                .map(line -> {
                    String[] cols = line.split(",");
                    double lat = Double.parseDouble(cols[0].trim());
                    double lon = Double.parseDouble(cols[1].trim());
                    String sat = cols[2].trim();
                    String date = cols[3].trim();
                    return new Hotspot(lat, lon, sat, date);
                });
    }
}
