package br.com.app.canafire.service;


import br.com.app.canafire.model.Hotspot;
import org.springframework.stereotype.Service;

@Service
public class HotspotService {

    public void handle(Hotspot hotspot) {
        // aqui você pode salvar no BD, enviar para fila, etc.
        System.out.println("Processed hotspot: " + hotspot);
    }
}

