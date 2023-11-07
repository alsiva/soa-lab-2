package org.ifmo.soalab2;



import javax.validation.constraints.Min;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.model.Product;
import org.ifmo.soalab2.model.ProductWithoutDate;
import org.ifmo.soalab2.model.Products;

import java.util.List;


@Named
@ApplicationScoped
public class ProductsApiServiceImpl {

    @Inject
    Storage storage;

    public Response addProduct(ProductWithoutDate body) {
        // do some magic!
        Product product = storage.addProduct(body);
        return Response.ok().entity(product).build();
    }
    public Response deleteProductById(Integer productId) throws NotFoundException {
        // do some magic!
        Product removedProduct = storage.removeProduct(productId);
        return Response.ok().entity(removedProduct).build();
    }
    public Response deleteProductByManufactureCost(Long manufactureCost) throws NotFoundException {
        // do some magic!
        Product removedProduct = storage.removeProduct(manufactureCost);
        return Response.ok().entity(removedProduct).build();
    }
    public Response getAllProducts(List<String> sort,  List<String> filter,  @Min(0) Integer page,  @Min(1) Integer pagesCount) throws NotFoundException {
        // do some magic!
        List<Product> productList = storage.getProductList();
        Products products = new Products(productList);
        return Response.ok().entity(products).build();
    }
    public Response getProductById(Integer productId) throws NotFoundException {
        Product product = storage.getProduct(productId);
        // do some magic!
        return Response.ok().entity(product).build();
    }
    public Response getProductByMaxUnitOfMeasure() throws NotFoundException {
        // do some magic!
        Product productWithMaxUnitOfMeasure = storage.getProductWithMaxUnitOfMaxUnitOfMeasure();
        return Response.ok().entity(productWithMaxUnitOfMeasure).build();
    }
    public Response getProductsByLessManufacturerAnnualTurnover(Double annualTurnover) throws NotFoundException {
        // do some magic!
        Products products = new Products(storage.getProductListWithLessAnnualTurnover(annualTurnover));
        return Response.ok().entity(products).build();
    }
    public Response updateProductById(ProductWithoutDate body, Integer productId) throws NotFoundException {
        // do some magic!
        Product updatedProduct = storage.updateProductById(body, productId);
        return Response.ok().entity(updatedProduct).build();
    }
}
