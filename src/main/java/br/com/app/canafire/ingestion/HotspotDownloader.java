package br.com.app.canafire.ingestion;

import br.com.app.canafire.service.HotspotService;
import br.com.app.canafire.parser.HotspotParser;
import br.com.app.canafire.service.HotspotService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class HotspotDownloader {

    private final WebClient webClient;
    private final HotspotService hotspotService;

    private static final String CSV_URL =
            "https://dataserver-coids.inpe.br/queimadas/queimadas/focos/csv/10min/focos_10min_20250526_0000.csv";

    @Scheduled(fixedDelayString = "${app.pull-interval}") // Insert interval application.properties
    public void pullLatestCsv() {
        System.out.println("Starting CSV download...");
        webClient.get()
                .uri(CSV_URL)
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(HotspotParser::parse)
                .doOnError(e -> System.err.println("Download error: " + e.getMessage()))
                .subscribe(
                        hotspotService::handle,
                        err -> { /* exceptions */ },
                        () -> System.out.println("CSV completed.")
                );
    }
}
