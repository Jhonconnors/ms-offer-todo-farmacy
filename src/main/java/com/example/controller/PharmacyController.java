package com.example.controller;

import com.example.model.ProductListPharmacy;
import com.example.service.DrSimiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/farmacy")
public class PharmacyController {

    @Autowired
    private DrSimiService drSimiService;

    // Anotacion para indicar el Scope que tiene permisos de usar el enpoint
    @PreAuthorize("hasAuthority('read:data')")
    @GetMapping("/medicament/product")
    public ResponseEntity<ProductListPharmacy> getProducts(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "comuna", required = false) String comuna
    ) {
        // Llamamos al servicio (al trabajador)
        ProductListPharmacy resultado = drSimiService.buscarProductos(query);
        
        // Respondemos con HTTP 200 (OK) y los datos
        return ResponseEntity.ok(resultado);
    }
}