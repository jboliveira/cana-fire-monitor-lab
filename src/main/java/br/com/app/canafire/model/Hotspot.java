package br.com.app.canafire.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Hotspot {
    private double lat;
    private double lon;
    private String sat;
    private String date;


    public Hotspot(double lat, double lon, String sat, String date) {
        this.lat = lat;
        this.lon = lon;
        this.sat = sat;
        this.date = date;
    }
}
