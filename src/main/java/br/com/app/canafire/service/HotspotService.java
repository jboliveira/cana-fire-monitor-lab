package br.com.app.canafire.service;


import br.com.app.canafire.model.Hotspot;
import org.springframework.stereotype.Service;

@Service
public class HotspotService {

    public void handle(Hotspot hotspot) {
        System.out.println("Processed hotspot: " + hotspot);
    }
}

