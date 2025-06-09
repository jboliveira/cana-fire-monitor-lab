package br.com.app.canafire.ingestion;

import br.com.app.canafire.exception.GlobalWebClientErrorHandler;
import br.com.app.canafire.parser.HotspotParser;
import br.com.app.canafire.service.HotspotService;
import br.com.app.canafire.util.HotspotUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class HotspotDownloader {

    private final WebClient webClient;
    private final HotspotService hotspotService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Scheduled(fixedDelayString = "${app.pull-interval}")
    public void pullLatestCsv() {
        System.out.println("Start...");

        String dynamicURL = HotspotUrlGenerator.generateLatestUrl(baseUrl); // metodoLastUrl(baseURL)
        System.out.println("URL: " + dynamicURL);

        webClient.get()
                .uri(dynamicURL)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        GlobalWebClientErrorHandler::handleResponse
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
}
