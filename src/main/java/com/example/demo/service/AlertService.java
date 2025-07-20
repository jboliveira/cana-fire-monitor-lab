package com.example.demo.service;

import com.example.demo.dto.HotspotEvent;
import com.example.demo.model.Alert;
import com.example.demo.repository.AlertRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    @Autowired
    public AlertRepository alertRepository;

    public void createAlert(HotspotEvent e) {
        Point p = e.toPoint(); // cria o ponto geométrico

        Alert alert = new Alert();
        alert.setLat(p.getY()); // latitude é a coordenada Y
        alert.setLon(p.getX()); // longitude é a coordenada X
        alert.setTimestamp(e.getTimestamp());

        alertRepository.save(alert);
        System.out.println("🚨 ALERTA! Foco de calor na fazenda: " + p);
    }

}
