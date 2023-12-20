package org.ifmo.soalab2;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.ifmo.soalab2.model.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ApplicationScoped
@Transactional
public class ProductRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public List<Product> getProducts() {
        return em.createQuery("select p from products p", Product.class).getResultList();
    }

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

        em.persist(product);
        return product;
    }

    public void insertSampleData() {
        em.persist(tanyaOrganization);
        productList.forEach(product -> em.persist(product));
    }

    private final ProductOrganization tanyaOrganization = new ProductOrganization(
            1,
            "TanyaCo",
            "TanyaCompany",
            10000f,
            ProductOrganization.OrgTypeEnum.COMMERCIAL,
            new ProductOrganizationPostalAddress("3535")
    );


    private final List<Product> productList = new ArrayList<>(Arrays.asList(
            new Product(
                    "Pelmeni",
                    new ProductCoordinates(10, 20),
                    new Date(),
                    500f,
                    200L,
                    UnitOfMeasure.METERS,
                    tanyaOrganization
            ),
            new Product(
                    "Pizza",
                    new ProductCoordinates(10, 20),
                    new Date(),
                    500f,
                    200L,
                    UnitOfMeasure.METERS,
                    tanyaOrganization
            )
    ));
}
