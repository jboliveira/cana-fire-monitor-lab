package br.com.app.canafire.util;

import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class HotspotUrlGenerator {


    public static String generateLatestUrl(String baseUrl) {
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(10); // subtrai 10 minutos
        int roundedMinute = (utcNow.getMinute() / 10) * 10; // calculo para arredondamento de 10 minutos
        utcNow = utcNow.withMinute(roundedMinute).withSecond(0).withNano(0); // minutos arredondados.segundos.milesegundos

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String formattedTimestamp = utcNow.format(formatter); // incluindo formatação

        return baseUrl + "focos_10min_" + formattedTimestamp + ".csv";
    }
}
