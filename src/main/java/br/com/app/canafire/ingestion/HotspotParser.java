    package br.com.app.canafire.ingestion;

    import com.opencsv.CSVReader;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.io.InputStreamReader;
    import java.net.URL;
    import java.util.ArrayList;
    import java.util.List;

    @RestController
    @RequestMapping("/api")
    public class HotspotParser {

        private static final String BASE_URL = "https://dataserver-coids.inpe.br/queimadas/queimadas/focos/csv/10min/";

        @GetMapping("/parse")
        public ResponseEntity<?> parseCsv(@RequestParam String fileName) {
            try {
                URL url = new URL(BASE_URL + fileName);
                CSVReader csvReader = new CSVReader(new InputStreamReader(url.openStream()));

                List<String[]> rows = csvReader.readAll();
                List<String> output = new ArrayList<>();

                for (String[] row : rows) {
                    String linha = String.join(" | ", row);
                    output.add(linha);
                }

                return ResponseEntity.ok(output);

            } catch (Exception e) {
                return ResponseEntity.status(500).body("Erro ao processar CSV: " + e.getMessage());
            }
        }
    }
