package com.example.demo.dto;

import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class HotspotEvent {
    private String satTs;
    private double lat;
    private double lon;
    private String confidence;

    public HotspotEvent(String satTs, double lat, double lon, String confidence) {
        this.satTs      = satTs;
        this.lat        = lat;
        this.lon        = lon;
        this.confidence = confidence;
    }

    public Point toPoint() {
        return new GeometryFactory().createPoint(new Coordinate(lon, lat));
    }

    public LocalDateTime getTimestamp() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(satTs, f);
    }

}
