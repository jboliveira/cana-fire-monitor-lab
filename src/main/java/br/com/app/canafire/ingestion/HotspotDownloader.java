package br.com.app.canafire.ingestion;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class HotspotDownloader {

    private static final String BASE_URL = "https://dataserver-coids.inpe.br/queimadas/queimadas/focos/csv/10min/";

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadCsv(@RequestParam String fileName) {
        try {
            URL url = new URL(BASE_URL + fileName);
            InputStream inputStream = url.openStream();
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
