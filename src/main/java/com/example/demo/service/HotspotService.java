package com.example.demo.service;

import com.example.demo.dto.HotspotEvent;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotspotService {

    private final List<PreparedGeometry> farms;
    private final AlertService alertService;

    public HotspotService(@Value("classpath:geo/farm.geojson") Resource geoJson,
                          AlertService alertService) throws Exception {
        this.alertService = alertService;

        // Lê o GeoJSON como Geometry (provavelmente um GeometryCollection)
        try (InputStreamReader reader = new InputStreamReader(geoJson.getInputStream())) {
            Geometry root = new GeoJsonReader().read(reader);
            this.farms = unpackAndPrepare(root);
        }
    }

    private List<PreparedGeometry> unpackAndPrepare(Geometry root) {
        PreparedGeometryFactory factory = new PreparedGeometryFactory();
        List<PreparedGeometry> list = new ArrayList<>();

        if (root instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) root;
            for (int i = 0; i < gc.getNumGeometries(); i++) {
                list.add(factory.create(gc.getGeometryN(i)));
            }
        } else {
            list.add(factory.create(root));
        }

        return list;
    }


    public void handle(HotspotEvent e) {
        farms.stream()
                .filter(pg -> pg.contains(e.toPoint()))
                .findFirst()
                .ifPresent(pg -> alertService.createAlert(e));
    }
}
