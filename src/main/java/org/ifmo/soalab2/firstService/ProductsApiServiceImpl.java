package org.ifmo.soalab2.firstService;


import javax.validation.constraints.Min;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.ApiResponseMessage;
import org.ifmo.soalab2.IllegalFilterException;
import org.ifmo.soalab2.NotFoundException;
import org.ifmo.soalab2.Storage;
import org.ifmo.soalab2.model.*;

import java.nio.channels.IllegalChannelGroupException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Named
@ApplicationScoped
public class ProductsApiServiceImpl {

    private final static String filterRegex = "^(id|name|coordinates\\\\.x|coordinates\\\\.y|creationDate|price|manufactureCost|unitOfMeasure|org_id|org_name|org_fullName|org_annualTurnover|org_type|postalAddress_zipcode)-(eq|nq|gt|lt|gte|lte)-(.+)";
    private final static Pattern filterPattern = Pattern.compile(filterRegex);


    public static <T extends Comparable<T>> boolean compareField(T a, T b, String operator) {
        switch (operator) {
            case "eq":
                return a.equals(b);
            case "nq":
                return !a.equals(b);
            case "gt":
                return a.compareTo(b) > 0;
            case "lt":
                return a.compareTo(b) < 0;
            case "gte":
                return a.compareTo(b) >= 0;
            case "lte":
                return a.compareTo(b) <= 0;
        }

        return true;
    }

    private static List<Product> filterProducts(List<Product> productList, List<String> filterList) throws IllegalArgumentException {
        return productList.stream().filter(product -> {
            for (String filter : filterList) {
                Matcher expressionMatcher = filterPattern.matcher(filter);

                String field;
                String comp;
                String value;
                if (expressionMatcher.find()) {
                    field = expressionMatcher.group(1);
                    comp = expressionMatcher.group(2);
                    value = expressionMatcher.group(3);
                } else {
                    throw new IllegalArgumentException("Фильтр должен иметь структуру <поле>-<оператор>-<значение>, вместо этого: " + filter);
                }

                switch (field) {
                    case "id":
                        int id;
                        try {
                            id = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getId(), id, comp);

                    case "name":
                        return compareField(product.getName(), value, comp);

                    case "coordinates\\.x":
                        int coordinateX;
                        try {
                            coordinateX = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getCoordinates().getX(), coordinateX, comp);

                    case "coordinates\\.y":
                        int coordinateY;
                        try {
                            coordinateY = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getCoordinates().getY(), coordinateY, comp);

                    case "creationDate":
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date;
                        try {
                            date = dateFormat.parse(value);
                        } catch (ParseException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getCreationDate(), date, comp);

                    case "price":
                        float price;
                        try {
                            price = Float.parseFloat(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getPrice(), price, comp);

                    case "manufactureCost":
                        long manufactureCost;
                        try {
                            manufactureCost = Long.parseLong(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getManufactureCost(), manufactureCost, comp);

                    case "unitOfMeasure":
                        UnitOfMeasure unitOfMeasure;
                        try {
                            unitOfMeasure = UnitOfMeasure.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getUnitOfMeasure(), unitOfMeasure, comp);

                    case "org_id":
                        int orgId;
                        try {
                            orgId = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getOrganization().getOrgId(), orgId, comp);

                    case "org_name":
                        return compareField(product.getOrganization().getName(), value, comp);

                    case "org_fullName":
                        return compareField(product.getOrganization().getFullName(), value, comp);

                    case "org_annualTurnover":
                        float annualTurnover;
                        try {
                            annualTurnover = Float.parseFloat(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getOrganization().getAnnualTurnover(), annualTurnover, comp);

                    case "org_type":
                        ProductOrganization.OrgTypeEnum orgType;
                        try {
                            orgType = ProductOrganization.OrgTypeEnum.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        return compareField(product.getOrganization().getOrgType(), orgType, comp);

                    case "postalAddress_zipcode":
                        return compareField(product.getOrganization().getPostalAddress().getZipcode(), value, comp);
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    private static class ProductCompositeComparator implements Comparator<Product> {
        private final List<SortingParams> sortingParams;

        public ProductCompositeComparator(List<SortingParams> sortingParams) {
            this.sortingParams = sortingParams;
        }

        private int getComparable(SortingParams sortingParam, Product a, Product b) {
            switch (sortingParam) {
                case product_id:
                    return a.getId().compareTo(b.getId());
                case name:
                    return a.getName().compareTo(b.getName());
                case coordinate_x:
                    return a.getCoordinates().getX().compareTo(b.getCoordinates().getX());
                case coordinate_y:
                    return a.getCoordinates().getY().compareTo(b.getCoordinates().getY());
                case creationDate:
                    return a.getCreationDate().compareTo(b.getCreationDate());
                case price:
                    return a.getPrice().compareTo(b.getPrice());
                case manufactureCost:
                    return a.getManufactureCost().compareTo(b.getManufactureCost());
                case unitOfMeasure:
                    return a.getUnitOfMeasure().compareTo(b.getUnitOfMeasure());
                case org_id:
                    return a.getOrganization().getOrgId().compareTo(b.getOrganization().getOrgId());
                case org_name:
                    return a.getOrganization().getName().compareTo(b.getOrganization().getName());
                case org_fullName:
                    return a.getOrganization().getFullName().compareTo(b.getOrganization().getFullName());
                case org_annualTurnover:
                    return a.getOrganization().getAnnualTurnover().compareTo(b.getOrganization().getAnnualTurnover());
                case org_type:
                    return a.getOrganization().getOrgType().compareTo(b.getOrganization().getOrgType());
                case postalAddress_zipcode:
                    return a.getOrganization().getPostalAddress().getZipcode().compareTo(b.getOrganization().getPostalAddress().getZipcode());
                case desc_product_id:
                    return -a.getId().compareTo(b.getId());
                case desc_name:
                    return -a.getName().compareTo(b.getName());
                case desc_coordinate_x:
                    return -a.getCoordinates().getX().compareTo(b.getCoordinates().getX());
                case desc_coordinate_y:
                    return -a.getCoordinates().getY().compareTo(b.getCoordinates().getY());
                case desc_creationDate:
                    return -a.getCreationDate().compareTo(b.getCreationDate());
                case desc_price:
                    return -a.getPrice().compareTo(b.getPrice());
                case desc_manufactureCost:
                    return -a.getManufactureCost().compareTo(b.getManufactureCost());
                case desc_unitOfMeasure:
                    return -a.getUnitOfMeasure().compareTo(b.getUnitOfMeasure());
                case desc_org_id:
                    return -a.getOrganization().getOrgId().compareTo(b.getOrganization().getOrgId());
                case desc_org_name:
                    return -a.getOrganization().getName().compareTo(b.getOrganization().getName());
                case desc_org_fullName:
                    return -a.getOrganization().getFullName().compareTo(b.getOrganization().getFullName());
                case desc_org_annualTurnover:
                    return -a.getOrganization().getAnnualTurnover().compareTo(b.getOrganization().getAnnualTurnover());
                case desc_org_type:
                    return -a.getOrganization().getOrgType().compareTo(b.getOrganization().getOrgType());
                case desc_postalAddress_zipcode:
                    return -a.getOrganization().getPostalAddress().getZipcode().compareTo(b.getOrganization().getPostalAddress().getZipcode());
            }
            return 0;
        }

        @Override
        public int compare(Product o1, Product o2) {
            for (SortingParams sortingParam : sortingParams) {
                int comparison = getComparable(sortingParam, o1, o2);
                if (comparison != 0) {
                    return comparison;
                }
            }
            return 0;
        }

    }

    @Inject
    Storage storage;

    public Response addProduct(ProductWithoutDate body) {
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
        List<SortingParams> sortingParams;
        try {
            sortingParams = SortingParams.parseSortingParams(sort);
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }


        List<Product> productList = storage.getProductList();
        try {
            productList = filterProducts(productList, filter);
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }

        ProductCompositeComparator productCompositeComparator = new ProductCompositeComparator(sortingParams);
        productList.sort(productCompositeComparator);


        if (productList.isEmpty()) {
            return Response.status(404).entity(new ApiResponseMessage("Список продуктов пуст")).build();
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
