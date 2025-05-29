package br.com.app.canafire.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Hotspot {
    private double lat;
    private double lon;
    private String satelite;
    private String data;

    /**
     * Construtor completo para passagem de valores.
     */
    public Hotspot(double lat, double lon, String satelite, String data) {
        this.lat = lat;
        this.lon = lon;
        this.satelite = satelite;
        this.data = data;
    }
}
