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


    private enum SortingParams {
        product_id, name, coordinate_x, coordinate_y,
        creationDate, price, manufactureCost, unitOfMeasure, org_id, org_name,
        org_fullName, org_annualTurnover, org_type, postalAddress_zipcode,
        desc_product_id, desc_name, desc_coordinate_x, desc_coordinate_y,
        desc_creationDate, desc_price, desc_manufactureCost,
        desc_unitOfMeasure, desc_org_id, desc_org_name, desc_org_fullName,
        desc_org_annualTurnover, desc_org_type, desc_postalAddress_zipcode;

        public static boolean containsString(String string) {
            for (SortingParams sortingParam : values()) {
                if (sortingParam.name().equalsIgnoreCase(string)) {
                    return true;
                }
            }
            return false;
        }

    }

    ;

    @Inject
    Storage storage;

    public Response addProduct(ProductWithoutDate body) {
        // do some magic!
        if (
                body.getName() == null ||
                        body.getCoordinates() == null ||
                        body.getPrice() == null ||
                        body.getManufactureCost() == null ||
                        body.getUnitOfMeasure() == null ||
                        body.getOrganization() == null
        ) {
            return Response.status(400).entity(new ApiResponseMessage("Неправильная структура продукта")).build();
        }
        Product product = storage.addProduct(body);
        return Response.ok().entity(product).build();
    }

    public Integer parseId(String idAsString) {
        int productId;
        try {
            productId = Integer.parseInt(idAsString);
        } catch (NumberFormatException e) {
            return null;
        }

        if (productId < 0) {
            return null;
        }

        return productId;
    }

    public Response deleteProductById(String productIdAsString) throws NotFoundException {

        Integer productId = parseId(productIdAsString);
        if (productId == null) {
            return Response.status(400).entity(new ApiResponseMessage("ID должен быть неотрицательным, также не может быть строкой")).build();
        }
        // do some magic!
        Product removedProduct = storage.removeProduct(productId);
        if (removedProduct == null) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }

        return Response.ok().entity(removedProduct).build();
    }

    public Response deleteProductByManufactureCost(String manufactureCostAsString) throws NotFoundException {
        // do some magic!
        long manufactureCost;
        try {
            manufactureCost = Long.parseLong(manufactureCostAsString);
        } catch (NumberFormatException e) {
            return Response.status(400).entity(new ApiResponseMessage("Неверное значение manufactureCost")).build();
        }

        if (manufactureCost < 0) {
            return Response.status(400).entity(new ApiResponseMessage("Неверное значение manufactureCost")).build();
        }

        Product removedProduct = storage.removeProduct(manufactureCost);
        if (removedProduct == null) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }

        return Response.ok().entity(removedProduct).build();
    }

    public Response getAllProducts(List<String> sort, List<String> filter, @Min(0) Integer page, @Min(1) Integer pagesCount) throws NotFoundException {
        // do some magic!
        if (sort != null) {
            for (String sort_element : sort) {
                if (!SortingParams.containsString(sort_element)) {
                    return Response.status(400).entity(new ApiResponseMessage("Вы должны были указать параметры сортировки и\n" +
                            "фильтрации в соответствии с требованиями, которые я вам\n" +
                            "указал")).build();
                }
            }
        }

        if (filter != null) {
            for (String filter_element : filter) {
                if (!filter_element.matches("^(id|name|coordinates\\\\.x|coordinates\\\\.y|creationDate|location\\\\.id|location\\\\.x|location\\\\.y|location\\\\.name|distance)(==|!=|>|<|>=|<=)(\\d+)")) {
                    return Response.status(400).entity(new ApiResponseMessage("Вы должны были указать параметры сортировки и\n" +
                            "фильтрации в соответствии с требованиями, которые я вам\n" +
                            "указал")).build();
                }
                ;
            }
        }

        List<Product> productList = storage.getProductList();
        if (productList.isEmpty()) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }

        Products products = new Products(productList);
        return Response.ok().entity(products).build();
    }

    public Response getProductById(String productIdAsString) throws NotFoundException {

        Integer productId = parseId(productIdAsString);
        if (productId == null) {
            return Response.status(400).entity(new ApiResponseMessage("ID должен быть неотрицательным, также не может быть строкой")).build();
        }

        Product product = storage.getProduct(productId);
        if (product == null) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }
        // do some magic!
        return Response.ok().entity(product).build();
    }

    public Response getProductByMaxUnitOfMeasure() throws NotFoundException {
        // do some magic!
        Product productWithMaxUnitOfMeasure = storage.getProductWithMaxUnitOfMaxUnitOfMeasure();
        if (productWithMaxUnitOfMeasure == null) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }
        return Response.ok().entity(productWithMaxUnitOfMeasure).build();
    }

    public Response getProductsByLessManufacturerAnnualTurnover(String annualTurnoverAsString) throws NotFoundException {
        // do some magic!
        float annualTurnover;
        try {
            annualTurnover = Float.parseFloat(annualTurnoverAsString);
        } catch (NumberFormatException e) {
            return Response.status(400).entity(new ApiResponseMessage("Неверное значение AnnualTurnover")).build();
        }

        if (annualTurnover < 0) {
            return Response.status(400).entity(new ApiResponseMessage("Неверное значение AnnualTurnover")).build();
        }

        List<Product> productList = storage.getProductListWithLessAnnualTurnover(annualTurnover);
        if (productList.isEmpty()) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }

        Products products = new Products(productList);
        return Response.ok().entity(products).build();
    }

    public Response updateProductById(ProductWithoutDate body, Integer productId) throws NotFoundException {

        if (
                body.getName().isEmpty() ||
                        body.getCoordinates() == null ||
                        body.getPrice() == null ||
                        body.getManufactureCost() == null ||
                        body.getUnitOfMeasure() == null ||
                        body.getOrganization() == null
        ) {
            return Response.status(400).entity(new ApiResponseMessage("Неверные входные данные")).build();
        }
        // do some magic!
        Product updatedProduct = storage.updateProductById(body, productId);

        if (updatedProduct == null) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }

        return Response.ok().entity(updatedProduct).build();
    }
}
