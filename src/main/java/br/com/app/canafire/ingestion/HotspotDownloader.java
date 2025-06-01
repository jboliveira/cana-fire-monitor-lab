package br.com.app.canafire.ingestion;

import br.com.app.canafire.exception.GlobalWebClientErrorHandler;
import br.com.app.canafire.exception.HotspotDownloadException;
import br.com.app.canafire.parser.HotspotParser;
import br.com.app.canafire.service.HotspotService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HotspotDownloader {

    private final WebClient webClient;
    private final HotspotService hotspotService;

    private static final String CSV_URL =
            "https://dataserver-coids.inpe.br/queimadas/queimadas/focos/csv/10min/focos_10min_20250529_0000.csv";

    @Scheduled(fixedDelayString = "${app.pull-interval}")
    public void pullLatestCsv() {
        System.out.println("Start...");

        webClient.get()
                .uri(CSV_URL)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        GlobalWebClientErrorHandler::handleResponse //
                )
                .bodyToMono(String.class)
                .flatMapMany(HotspotParser::parse)
                .doOnError(e -> {
                    System.err.println("Erro no download ou parsing: " + e.getMessage());
                })
                .subscribe(
                        hotspotService::handle,
                        err -> {
                            System.err.println("Erro ao processar hotspot:");
                            err.printStackTrace();
                        },
                        () -> System.out.println("Completed.")
                );
    }

    public void RecpectorArchiveCSV(String CSV_URL) {
        CSV_URL = "https://dataserver-coids.inpe.br/queimadas/queimadas/focos/csv/10min";
    }
}
