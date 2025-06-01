package br.com.app.canafire.parser;

import br.com.app.canafire.exception.CsvParseException;
import br.com.app.canafire.model.Hotspot;
import reactor.core.publisher.Flux;

public class HotspotParser {

    public static Flux<Hotspot> parse(String csvContent) {
        if (csvContent == null || csvContent.trim().isEmpty()) {
            return Flux.error(new IllegalArgumentException("CSV vazio ou nulo"));
        }

        return Flux
                .fromArray(csvContent.split("\\r?\\n"))
                .skip(1) // pula o cabeçalho
                .filter(line -> !line.trim().isEmpty()) // ignora linhas vazias
                .flatMap(line -> {
                    try {
                        String[] cols = line.split(",");
                        if (cols.length < 4) {
                            return Flux.error(new IllegalArgumentException("Linha incompleta: " + line));
                        }

                        double lat = Double.parseDouble(cols[0].trim());
                        double lon = Double.parseDouble(cols[1].trim());
                        String sat = cols[2].trim();
                        String date = cols[3].trim();

                        return Flux.just(new Hotspot(lat, lon, sat, date));
                    } catch (NumberFormatException e) {
                        return Flux.error(new CsvParseException("Erro ao processar linha: " + line, e));
                    } catch (Exception e) {
                        return Flux.error(new CsvParseException("Erro ao processar linha: " + line, e));
                    }
                });
    }
}
