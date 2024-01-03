package org.ifmo.soalab2;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.ifmo.soalab2.model.*;
import java.util.*;

@ApplicationScoped
@Transactional
public class ProductRepository {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public List<Product> getProducts() {
        return em.createQuery("select p from products p", Product.class).getResultList();
    }

    public Product getProduct(Integer id) {
        return em.find(Product.class, id);
    }


    public Product updateProductById(ProductWithoutDate body, Integer id) {

        em.merge(body.getOrganization());

        Product productToBeUpdated = em.find(Product.class, id);
        productToBeUpdated.setName(body.getName());
        productToBeUpdated.setCoordinates(body.getCoordinates());
        productToBeUpdated.setPrice(body.getPrice());
        productToBeUpdated.setManufactureCost(body.getManufactureCost());
        productToBeUpdated.setOrganization(em.find(ProductOrganization.class, body.getOrganization().getOrgId()));


        return em.merge(productToBeUpdated);
    }

    public Product removeProduct(Integer id) {
        Product productToBeRemoved = em.find(Product.class, id);
        em.remove(productToBeRemoved);
        return productToBeRemoved;
    }

    public Product removeProductByManufactureCost(Long manufactureCost) {
        Product productToBeRemoved = em.createQuery("select p from products p where p.manufactureCost = :manufactureCost", Product.class)
                .setParameter("manufactureCost", manufactureCost).setMaxResults(1).getSingleResult();
        em.remove(productToBeRemoved);
        return productToBeRemoved;
    }

    public Product getProductWithMaxUnitOfMeasure() {
        return em.createQuery("select p from products p order by p.unitOfMeasure", Product.class).setMaxResults(1).getSingleResult();
    }



    public List<Product> getProductListWithLessAnnualTurnover(Float annualTurnover) {
        return em.createQuery("select p from products p where p.organization.annualTurnover < :annualTurnover", Product.class)
                .setParameter("annualTurnover", annualTurnover).getResultList();
    }



    public Product addProduct(ProductWithoutDate productWithoutDate) {
        ProductOrganization newProductOrganization = productWithoutDate.getOrganization();

        ProductOrganization productOrganizationToBeUpdated = em.find(ProductOrganization.class, productWithoutDate.getOrganization().getOrgId());
        if (productOrganizationToBeUpdated != null) {
            productOrganizationToBeUpdated.setName(newProductOrganization.getName());
            productOrganizationToBeUpdated.setFullName(newProductOrganization.getFullName());
            productOrganizationToBeUpdated.setAnnualTurnover(newProductOrganization.getAnnualTurnover());
            productOrganizationToBeUpdated.setOrgType(newProductOrganization.getOrgType());
            productOrganizationToBeUpdated.setPostalAddress(newProductOrganization.getPostalAddress());
            em.merge(productOrganizationToBeUpdated);
        } else {
            em.persist(newProductOrganization);
        }

        Product product = new Product(
                productWithoutDate.getName(),
                productWithoutDate.getCoordinates(),
                new Date(),
                productWithoutDate.getPrice(),
                productWithoutDate.getManufactureCost(),
                productWithoutDate.getUnitOfMeasure(),
                em.find(ProductOrganization.class, productWithoutDate.getOrganization().getOrgId())
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
