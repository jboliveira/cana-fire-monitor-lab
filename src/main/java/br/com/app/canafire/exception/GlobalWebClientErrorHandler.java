package br.com.app.canafire.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class GlobalWebClientErrorHandler {

    public static Mono<? extends Throwable> handleResponse(ClientResponse response) {
        HttpStatus status = (HttpStatus) response.statusCode();
        return response.bodyToMono(String.class)
                .defaultIfEmpty("Corpo de resposta vazio")
                .flatMap(body -> {
                    String message = "Erro HTTP " + status.value() + " - " + status.getReasonPhrase() + ": " + body;
                    if (status.is4xxClientError()) {
                        return Mono.error(new HotspotDownloadException("Erro 400/499: " + message));
                    } else if (status.is5xxServerError()) {
                        return Mono.error(new HotspotDownloadException("Erro servidor: " + message));
                    } else {
                        return Mono.error(new HotspotDownloadException("Erro inesperado: " + message));
                    }
                });
    }


    public static Mono<String> handleGenericError(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException ex = (WebClientResponseException) throwable;
            return Mono.error(new HotspotDownloadException("Erro HTTP: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString(), ex));
        }

        return Mono.error(new HotspotDownloadException("Erro inesperado: " + throwable.getMessage(), throwable));
    }
}
