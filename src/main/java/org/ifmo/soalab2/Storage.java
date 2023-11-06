package org.ifmo.soalab2;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.ifmo.soalab2.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Named
@ApplicationScoped
public class Storage {
    private List<Product> productList = List.of(
            new Product(
                    "Pelmeni",
                    new ProductCoordinates(10, 20),
                    new Date(),
                    500f,
                    200L,
                    UnitOfMeasure.METERS,
                    new ProductOrganization(
                            1,
                            "TanyaCo",
                            "TanyaCompany",
                            10000f,
                            ProductOrganization.OrgTypeEnum.COMMERCIAL,
                            new ProductOrganizationPostalAddress("3535")
                    )
            )
    );

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

    public List<Product> getProductList() {
        return productList;
    }

    public void removeProduct(int id) {
        Product productToBeRemoved = null;
        for (Product product : productList) {
            if (product.getId() == id) {
                productToBeRemoved = product;
            }
        }
        if (productToBeRemoved != null) {
            productList.remove(productToBeRemoved);
        }
    }


}
