package org.ifmo.soalab2;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import org.ifmo.soalab2.model.Product;

import javax.persistence.EntityManager;
import java.util.List;


@ApplicationScoped
//@Transactional
@Default
public class ProductRepositoryImpl implements ProductRepository {

//    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Product> getProducts() {
        return em.createQuery("select products from products", Product.class).getResultList();
    }

    /*@Override
    public Product getProductById(Integer id) {
        return null;
    }

    @Override
    public Product saveProduct(Product product) {
        return null;
    }

    @Override
    public Product deleteProduct(Product product) {
        return null;
    }*/
}
