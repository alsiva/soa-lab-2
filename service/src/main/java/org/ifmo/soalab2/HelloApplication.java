package org.ifmo.soalab2;

import jakarta.persistence.*;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.ifmo.soalab2.model.*;

import java.util.Date;

@ApplicationPath("/api")
public class HelloApplication extends Application {

    /*
    private final static ProductOrganization tanyaOrganization = new ProductOrganization(
            1,
            "TanyaCo",
            "TanyaCompany",
            10000f,
            ProductOrganization.OrgTypeEnum.COMMERCIAL,
            new ProductOrganizationPostalAddress("3535")
    );

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            Product pelmeniProduct = new Product(
                    "Pelmeni",
                    new ProductCoordinates(10, 20),
                    new Date(),
                    500f,
                    200L,
                    UnitOfMeasure.METERS,
                    tanyaOrganization
            );

            entityManager.persist(pelmeniProduct);


            transaction.begin();
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
            entityManagerFactory.close();
        }
    }*/
}