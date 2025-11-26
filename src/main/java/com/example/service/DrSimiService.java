package com.example.service;

import com.example.model.Precio;
import com.example.model.ProductListPharmacy;
import com.example.model.ProductPharmacy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrSimiService {

    // USAMOS LA API DIRECTA (Más estable y sin bloqueos complejos)
    private final String API_URL = "https://www.drsimi.cl/api/catalog_system/pub/products/search/%s?_from=0&_to=19&sc=1";

    public ProductListPharmacy buscarProductos(String busqueda) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        List<ProductPharmacy> listaResultados = new ArrayList<>();

        System.out.println("--- CONECTANDO A API DIRECTA DR SIMI: " + busqueda + " ---");

        try {
            // 1. Headers simples (A veces son necesarios para que no rechacen la conexión)
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 2. Construir URL
            String urlFinal = String.format(API_URL, busqueda);
            System.out.println("URL: " + urlFinal);

            // 3. Petición
            ResponseEntity<String> response = restTemplate.exchange(urlFinal, HttpMethod.GET, entity, String.class);
            
            // 4. Leer el JSON (Esta API devuelve directamente una LISTA de productos [])
            JsonNode rootArray = mapper.readTree(response.getBody());

            System.out.println("Productos encontrados: " + rootArray.size());

            // 5. Recorrer la lista
            for (JsonNode productoNode : rootArray) {
                try {
                    ProductPharmacy p = new ProductPharmacy();
                    
                    // Nombre
                    p.setNombre(productoNode.path("productName").asText());
                    
                    // URL
                    p.setFarmaciaUrl(productoNode.path("link").asText());

                    // Imagen y Precio (Están dentro de 'items')
                    JsonNode items = productoNode.path("items");
                    if (items.isArray() && items.size() > 0) {
                        JsonNode item = items.get(0); // Primer variante del producto
                        
                        // Imagen
                        JsonNode images = item.path("images");
                        if (images.isArray() && images.size() > 0) {
                            p.setImagen(images.get(0).path("imageUrl").asText());
                        }

                        // Precio
                        JsonNode sellers = item.path("sellers");
                        if (sellers.isArray() && sellers.size() > 0) {
                            // Buscamos el vendedor principal (normalmente el index 0)
                            Double precio = sellers.get(0)
                                    .path("commertialOffer")
                                    .path("Price")
                                    .asDouble();
                            p.setPrecioUnitario(new Precio(precio, "CLP"));
                        }
                    }

                    listaResultados.add(p);
                } catch (Exception e) {
                    System.out.println("Error mapeando un producto: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR API: " + e.getMessage());
            e.printStackTrace();
        }

        return new ProductListPharmacy(listaResultados);
    }
}