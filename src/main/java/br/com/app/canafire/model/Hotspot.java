package br.com.app.canafire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotspot {
    private double lat;
    private double lon;
    private String satelite;
    private String data;


        public Hotspot(double lat, double lon, String satelite, String data) {
        this.lat = lat;
        this.lon = lon;
        this.satelite = satelite;
        this.data = data;
    }
}





