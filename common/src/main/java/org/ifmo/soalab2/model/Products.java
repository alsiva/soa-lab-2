package org.ifmo.soalab2.model;

import jakarta.xml.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Products
 */
@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class Products implements Serializable {
    @XmlElement(name="product")
    private List<Product> products = new ArrayList<>();

    public Products() {
    }

    public Products(List<Product> products) {
        this.products = products;
    }
}
