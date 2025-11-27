package com.example.model;

import java.util.List;

public class ProductListPharmacy {
    private List<ProductPharmacy> items;

    public ProductListPharmacy() {}
    
    public ProductListPharmacy(List<ProductPharmacy> items) {
        this.items = items;
    }

    public List<ProductPharmacy> getItems() { return items; }
    public void setItems(List<ProductPharmacy> items) { this.items = items; }
}
