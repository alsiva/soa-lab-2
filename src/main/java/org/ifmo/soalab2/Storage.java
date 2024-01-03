package org.ifmo.soalab2;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.ifmo.soalab2.model.*;

import java.util.*;

@Named
@ApplicationScoped
public class Storage {


    private final List<Product> productList = new ArrayList<>();

    public Product addProduct(ProductWithoutDate productWithoutDate) {
        Product product = new Product(
                productWithoutDate.getName(),
                productWithoutDate.getCoordinates(),
                new Date(),
                productWithoutDate.getPrice(),
                productWithoutDate.getManufactureCost(),
                productWithoutDate.getUnitOfMeasure(),
                productWithoutDate.getOrganization()
        );
        productList.add(product);
        return product;
    }

    public Product getProduct(int id) {
        Product result = null;
        for (Product product : productList) {
            if (product.getId() == id) {
                result = product;
            }
        }
        return result;
    }

    public List<Product> getProductListWithLessAnnualTurnover(double annualTurnover) {
        List<Product> result = new ArrayList<>();
        for (Product product : productList) {
            if (product.getOrganization().getAnnualTurnover() < annualTurnover) {
                result.add(product);
            }
        }
        return result;
    }

    public Product getProductWithMaxUnitOfMaxUnitOfMeasure() {
        Product productWithMaxUnitOfMeasure = null;
        for (Product product : productList) {
            if (productWithMaxUnitOfMeasure == null) {
                productWithMaxUnitOfMeasure = product;
            } else if (productWithMaxUnitOfMeasure.getUnitOfMeasure().compareTo(product.getUnitOfMeasure()) < 0) {
                productWithMaxUnitOfMeasure = product;
            }
            ;
        }
        return productWithMaxUnitOfMeasure;

    }


    public List<Product> getProductList() {
        return productList;
    }

    public Product removeProduct(int id) {
        Product productToBeRemoved = null;
        for (Product product : productList) {
            if (product.getId() == id) {
                productToBeRemoved = product;
            }
        }
        if (productToBeRemoved != null) {
            productList.remove(productToBeRemoved);
        }
        return productToBeRemoved;
    }

    public Product removeProduct(Long manufactureCost) {
        Product productToBeRemoved = null;
        for (Product product: productList) {
            if (Objects.equals(product.getManufactureCost(), manufactureCost)) {
                productToBeRemoved = product;
            }
        }
        if (productToBeRemoved != null) {
            productList.remove(productToBeRemoved);
        }
        return productToBeRemoved;
    }

    public Product updateProductById(ProductWithoutDate productWithoutDate, Integer productId) {
        Product product = productList.get(productId - 1);
        if (product == null) {
            return null;
        }

        product.setName(productWithoutDate.getName());
        product.setCoordinates(productWithoutDate.getCoordinates());
        product.setPrice(productWithoutDate.getPrice());
        product.setManufactureCost(productWithoutDate.getManufactureCost());
        product.setUnitOfMeasure(productWithoutDate.getUnitOfMeasure());
        product.setOrganization(productWithoutDate.getOrganization());

        return product;
    }
}
