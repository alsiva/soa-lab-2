package org.ifmo.soalab2;

import org.ifmo.soalab2.model.Product;

import java.util.List;

public interface ProductRepository {
    public List<Product> getProducts();
}
