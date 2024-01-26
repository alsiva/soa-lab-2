package org.ifmo.soalab2.firstService;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.*;
import org.ifmo.soalab2.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


//@Named
@ApplicationScoped
public class ProductsApiServiceImpl {

    private final static String filterRegex = "^(id|name|coordinates\\.x|coordinates\\.y|creationDate|price|manufactureCost|unitOfMeasure|org_id|org_name|org_fullName|org_annualTurnover|org_type|postalAddress_zipcode)-(eq|nq|gt|lt|gte|lte)-(.*)";
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
                String operator;
                String value;
                if (expressionMatcher.find()) {
                    field = expressionMatcher.group(1);
                    operator = expressionMatcher.group(2);
                    value = expressionMatcher.group(3);
                } else {
                    throw new IllegalArgumentException("Фильтр должен иметь структуру <поле>-<оператор>-<значение>, вместо этого: " + filter);
                }

                if (value.trim().isEmpty()) {
                    continue;
                }

                switch (field) {
                    case "id":
                        int id;
                        try {
                            id = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getId(), id, operator)) {
                            return false;
                        }
                        break;

                    case "name":
                        if (!compareField(product.getName(), value, operator)) {
                            return false;
                        }
                        break;

                    case "coordinates.x":
                        int coordinateX;
                        try {
                            coordinateX = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getCoordinates().getX(), coordinateX, operator)) {
                            return false;
                        }
                        break;

                    case "coordinates.y":
                        int coordinateY;
                        try {
                            coordinateY = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getCoordinates().getY(), coordinateY, operator)) {
                            return false;
                        }
                        break;

                    case "creationDate":
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date;
                        try {
                            date = dateFormat.parse(value);
                        } catch (ParseException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getCreationDate(), date, operator)) {
                            return false;
                        }
                        break;

                    case "price":
                        float price;
                        try {
                            price = Float.parseFloat(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getPrice(), price, operator)) {
                            return false;
                        }
                        break;

                    case "manufactureCost":
                        long manufactureCost;
                        try {
                            manufactureCost = Long.parseLong(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getManufactureCost(), manufactureCost, operator)) {
                            return false;
                        }
                        break;

                    case "unitOfMeasure":
                        UnitOfMeasure unitOfMeasure;
                        try {
                            unitOfMeasure = UnitOfMeasure.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getUnitOfMeasure(), unitOfMeasure, operator)) {
                            return false;
                        }
                        break;

                    case "org_id":
                        int orgId;
                        try {
                            orgId = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getOrganization().getOrgId(), orgId, operator)) {
                            return false;
                        }
                        break;

                    case "org_name":
                        if (!compareField(product.getOrganization().getName(), value, operator)) {
                            return false;
                        }
                        break;

                    case "org_fullName":
                        if (!compareField(product.getOrganization().getFullName(), value, operator)) {
                            return false;
                        }
                        break;

                    case "org_annualTurnover":
                        float annualTurnover;
                        try {
                            annualTurnover = Float.parseFloat(value);
                        } catch (NumberFormatException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getOrganization().getAnnualTurnover(), annualTurnover, operator)) {
                            return false;
                        }
                        break;

                    case "org_type":
                        ProductOrganization.OrgTypeEnum orgType;
                        try {
                            orgType = ProductOrganization.OrgTypeEnum.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            throw new IllegalFilterException(field, value);
                        }
                        if (!compareField(product.getOrganization().getOrgType(), orgType, operator)) {
                            return false;
                        }
                        break;

                    case "postalAddress_zipcode":
                        if (!compareField(product.getOrganization().getPostalAddress().getZipcode(), value, operator)) {
                            return false;
                        }
                        break;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    private static class ProductCompositeComparator implements Comparator<Product> {
        private final List<SortingPair> sortingPairs;

        public ProductCompositeComparator(List<SortingPair> sortingParams) {
            this.sortingPairs = sortingParams;
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
            }
            return 0;
        }

        @Override
        public int compare(Product o1, Product o2) {
            for (SortingPair sortingPair : sortingPairs) {
                int comparison = getComparable(sortingPair.getParam(), o1, o2);
                if (sortingPair.getDirection() == SortingDirection.DESC) {
                    comparison = -comparison;
                }
                if (comparison != 0) {
                    return comparison;
                }
            }
            return 0;
        }

    }

    @Inject
    ProductRepository productRepository;

    @PostConstruct
    public void addData() {
        ProductOrganizationPostalAddress postalAddress = new ProductOrganizationPostalAddress("1018");
        ProductOrganization organization = new ProductOrganization(1, "org", "sample org", 100.0f, ProductOrganization.OrgTypeEnum.COMMERCIAL, postalAddress);
        ProductWithoutDate productWithoutDate = new ProductWithoutDate(
                "Potato",    //        this.name = name;
                new ProductCoordinates(67, 31),    //        this.coordinates = coordinates;
                200.0f,    //        this.price = price;
                200L,    //        this.manufactureCost = manufactureCost;
                UnitOfMeasure.METERS,    //        this.unitOfMeasure = unitOfMeasure;
                organization    //        this.organization = organization;
        );
        productRepository.addProduct(productWithoutDate);

        ProductOrganizationPostalAddress postalAddress1 = new ProductOrganizationPostalAddress("2325");
        ProductOrganization organization1 = new ProductOrganization(2, "org", "Tanya org", 3000.0f, ProductOrganization.OrgTypeEnum.TRUST, postalAddress1);
        ProductWithoutDate productWithoutDate1 = new ProductWithoutDate(
                "Pelmeni",    //        this.name = name;
                new ProductCoordinates(23, 19),    //        this.coordinates = coordinates;
                378.0f,    //        this.price = price;
                129L,    //        this.manufactureCost = manufactureCost;
                UnitOfMeasure.MILLILITERS,    //        this.unitOfMeasure = unitOfMeasure;
                organization1    //        this.organization = organization;
        );
        productRepository.addProduct(productWithoutDate1);

        ProductWithoutDate productWithoutDate2 = new ProductWithoutDate(
                "Agusha",    //        this.name = name;
                new ProductCoordinates(23, 19),    //        this.coordinates = coordinates;
                378.0f,    //        this.price = price;
                129L,    //        this.manufactureCost = manufactureCost;
                UnitOfMeasure.MILLILITERS,    //        this.unitOfMeasure = unitOfMeasure;
                organization1    //        this.organization = organization;
        );
        productRepository.addProduct(productWithoutDate2);

        ProductWithoutDate productWithoutDate3 = new ProductWithoutDate(
                "Beer",    //        this.name = name;
                new ProductCoordinates(23, 19),    //        this.coordinates = coordinates;
                378.0f,    //        this.price = price;
                129L,    //        this.manufactureCost = manufactureCost;
                UnitOfMeasure.MILLILITERS,    //        this.unitOfMeasure = unitOfMeasure;
                organization1    //        this.organization = organization;
        );
        productRepository.addProduct(productWithoutDate3);

        ProductWithoutDate productWithoutDate4 = new ProductWithoutDate(
                "Condons",    //        this.name = name;
                new ProductCoordinates(23, 19),    //        this.coordinates = coordinates;
                378.0f,    //        this.price = price;
                129L,    //        this.manufactureCost = manufactureCost;
                UnitOfMeasure.MILLILITERS,    //        this.unitOfMeasure = unitOfMeasure;
                organization1    //        this.organization = organization;
        );
        productRepository.addProduct(productWithoutDate4);
    }

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


        Product product = productRepository.addProduct(body);

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
        Product removedProduct = productRepository.removeProduct(productId);
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

        Product removedProduct = productRepository.removeProductByManufactureCost(manufactureCost);
        if (removedProduct == null) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }

        return Response.ok().entity(removedProduct).build();
    }

    public Response insertSampleData() {
        productRepository.insertSampleData();
        return Response.ok().build();
    }

    public Response getAllProducts(List<String> sort, List<String> filter, Integer pageIndex, Integer pageSize) throws NotFoundException {
        List<SortingPair> sortingParams;
        try {
            sortingParams = SortingParams.parseSortingParams(sort);
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }


        List<Product> productList = productRepository.getProducts();

        try {
            productList = filterProducts(productList, filter);
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }

        ProductCompositeComparator productCompositeComparator = new ProductCompositeComparator(sortingParams);
        productList.sort(productCompositeComparator);


        if (pageIndex != null && pageSize != null) {
            if (pageIndex >= 0 && pageSize >= 1) {
                final List<Product> finalProductList = productList;
                productList = IntStream.range(0, productList.size()).filter(i ->
                        pageSize * pageIndex <= i && i <= pageSize * (pageIndex + 1) - 1
                ).mapToObj(finalProductList::get).collect(Collectors.toList());
            }
        }
        /*
        if (productList.isEmpty()) {
            return Response.status(404).entity(new ApiResponseMessage("Список продуктов пуст")).build();
        }
        */

        Products products = new Products(productList);
        return Response.ok().entity(products).build();
    }

    public Response getProductById(String productIdAsString) throws NotFoundException {
        Integer productId = parseId(productIdAsString);
        if (productId == null) {
            return Response.status(400).entity(new ApiResponseMessage("ID должен быть неотрицательным, также не может быть строкой")).build();
        }

        Product product = productRepository.getProduct(productId);
        if (product == null) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }
        // do some magic!
        return Response.ok().entity(product).build();
    }

    public Response getProductByMaxUnitOfMeasure() throws NotFoundException {
        // do some magic!
        Product productWithMaxUnitOfMeasure = productRepository.getProductWithMaxUnitOfMeasure();
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

        List<Product> productList = productRepository.getProductListWithLessAnnualTurnover(annualTurnover);


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
        Product updatedProduct = productRepository.updateProductById(body, productId);


        if (updatedProduct == null) {
            return Response.status(404).entity(new ApiResponseMessage("Нет данного ресурса")).build();
        }

        return Response.ok().entity(updatedProduct).build();
    }
}
