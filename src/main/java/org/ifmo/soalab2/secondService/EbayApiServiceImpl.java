package org.ifmo.soalab2.secondService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.ApiResponseMessage;
import org.ifmo.soalab2.Storage;
import org.ifmo.soalab2.model.Product;
import org.ifmo.soalab2.model.Products;
import org.ifmo.soalab2.model.UnitOfMeasure;

import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class EbayApiServiceImpl {

    @Inject
    Storage storage;

    public Response getProductsWithRange(String rangeFromString, String rangeToString) {
        float rangeFrom;
        float rangeTo;
        try {
            rangeFrom = Float.parseFloat(rangeFromString);
            rangeTo = Float.parseFloat(rangeToString);
        } catch (NullPointerException | NumberFormatException e) {
            return Response.status(400).entity(new ApiResponseMessage("price-from и price-to должны быть положительными числами в формате Float, price-from должно быть меньше price-to")).build();
        }
        if (rangeFrom < 0 | rangeTo < 0 | rangeFrom > rangeTo) {
            return Response.status(400).entity(new ApiResponseMessage("price-from и price-to должны быть положительными числами в формате Float, price-from должно быть меньше price-to")).build();
        }

        List<Product> productList = storage.getProductList();
        List<Product> productListToBeRemoved = new ArrayList<>();
        for (Product product: productList) {
            if (!(rangeFrom <= product.getPrice() && product.getPrice() <= product.getPrice())) {
                productListToBeRemoved.add(product);
            }
        }
        productList.removeAll(productListToBeRemoved);
        if (productList.isEmpty()) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }


        Products products = new Products(productList);
        return Response.ok().entity(products).build();
    }

    public Response getProductsWithUnitOfMeasure(String unitOfMeasureAsString) {
        UnitOfMeasure unitOfMeasure;
        try {
            unitOfMeasure = UnitOfMeasure.valueOf(unitOfMeasureAsString);
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(new ApiResponseMessage("Неправильные входные параметры")).build();
        }
        List<Product> productList = storage.getProductList();
        List<Product> productListToBeRemoved = new ArrayList<>();
        for (Product product: productList) {
            if (product.getUnitOfMeasure() != unitOfMeasure) {
                productListToBeRemoved.add(product);
            }
        }
        productList.removeAll(productListToBeRemoved);
        if (productList.isEmpty()) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }

        Products products = new Products(productList);
        return Response.ok().entity(products).build();
    }
}
