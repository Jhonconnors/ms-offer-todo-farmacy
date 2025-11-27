package com.example.service;

import com.example.model.Precio;
import com.example.model.ProductListPharmacy;
import com.example.model.ProductPharmacy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PharmacyService {

    // IMPORTANTE: Aquí debes poner la URL real de la farmacia externa
    private final String EXTERNAL_API_URL = "https://api-farmacia-externa.com/api/productos";

    public ProductListPharmacy buscarMedicamentos(String busqueda, String comuna) {
        RestTemplate restTemplate = new RestTemplate();
        ProductListPharmacy respuestaFinal = new ProductListPharmacy();
        List<ProductPharmacy> listaMedicamentos = new ArrayList<>();

        try {
            // 1. Preparamos el Token de seguridad (Header)
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer TU_TOKEN_REAL_AQUI");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 2. Construimos la URL con el parámetro de búsqueda
            // Asumimos que la API externa recibe algo como ?search=paracetamol
            String urlConParametros = EXTERNAL_API_URL + "?search=" + busqueda;

            // 3. Hacemos la llamada a internet (GET)
            // NOTA: Aquí asumimos que la externa devuelve algo que podemos leer.
            // Si la externa devuelve JSON, Spring intentará convertirlo.
            // Por ahora, usaremos 'String.class' para ver qué responde y no romper el código,
            // pero luego deberás mapearlo a una clase real.
            ResponseEntity<String> response = restTemplate.exchange(
                    urlConParametros,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // --- SIMULACIÓN DE DATOS (MOCK) ---
            // Como no tenemos la API externa real conectada ahora mismo,
            // llenaremos la lista manualmente para que veas que tu Swagger funciona.
            // Cuando tengas la API real, borra esta parte y usa 'response.getBody()'.
            
            ProductPharmacy p1 = new ProductPharmacy();
            p1.setNombre("Resultado simulado para: " + busqueda);
            p1.setDescripcion("Medicamento de prueba");
            p1.setImagen("https://via.placeholder.com/150");
            p1.setFarmaciaUrl("https://farmacia.cl/producto/1");
            p1.setPrecioUnitario(new Precio(9990.0, "CLP"));
            
            listaMedicamentos.add(p1);
            // ----------------------------------

        } catch (Exception e) {
            System.out.println("Error conectando con la farmacia externa: " + e.getMessage());
        }

        respuestaFinal.setItems(listaMedicamentos);
        return respuestaFinal;
    }
}