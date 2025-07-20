package com.example.demo.ingestion;


import com.example.demo.parse.HotspotParse;
import com.example.demo.service.HotspotService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HotspotDownloader {

    private final WebClient webClient;
    private final HotspotService hotspotService;

    public HotspotDownloader(WebClient webClient, HotspotService hotspotService) {
        this.webClient = webClient;
        this.hotspotService = hotspotService;
    }

    @Value("${api.base.url}")
    private String apiBaseUrl;

    @Scheduled(fixedDelayString = "${api.fixed.delay}")
    public void baixarUltimoArquivo() {
        System.out.println("Iniciando...");

        webClient.get()
                .uri(apiBaseUrl)
                .retrieve()
                .bodyToMono(String.class)
//                .doOnNext(html -> System.out.println("📄 HTML retornado:\n" + html))
                .map(Jsoup::parse)
                .map(this::extrairUltimoArquivo)
                .flatMap(this::baixarCsv)
                .flatMapMany(HotspotParse::parse)
                .doOnNext(hotspotService::handle)
                .doOnComplete(() -> System.out.println("Conseguiu baixar o arquivo!"))
                .doOnError(error -> System.out.println("❌ Erro: " + error.getMessage()))
                .subscribe();
    }


    private String extrairUltimoArquivo(Document document) {
        Elements links = document.select("a[href$=.csv]");
        System.out.println("🔗 Links CSV encontrados: " + links.size());


        Pattern pattern = Pattern.compile("focos_10min_(\\d{8}_\\d{4})\\.csv"); //regex

        return links.stream()
                .map(link -> link.attr("href"))
                .filter(name -> {
                    boolean matches = pattern.matcher(name).matches();
                    if (!matches) {
                        System.out.println("link Ignorado: " + name);
                    }
                    return matches;
                })
                .max(Comparator.comparing(name -> {
                    Matcher matcher = pattern.matcher(name);
                    return matcher.find() ? matcher.group(1) : "0";
                }))
                .orElseThrow(() -> {
                    System.out.println(" Nenhum CSV válido encontrado");
                    return new RuntimeException("Nenhum CSV encontrado");
                });
    }



    private Mono<String> baixarCsv(String fileName) {
        String fullUrl = apiBaseUrl + "/" + fileName;
        System.out.println("Arquivo baixado: " + fullUrl);

        return webClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(String.class);
    }
}
