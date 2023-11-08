package org.ifmo.soalab2;


import javax.validation.constraints.Min;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.model.Product;
import org.ifmo.soalab2.model.ProductWithoutDate;
import org.ifmo.soalab2.model.Products;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


@Named
@ApplicationScoped
public class ProductsApiServiceImpl {

    private enum SortingParams {
        product_id,
        name,
        coordinate_x,
        coordinate_y,
        creationDate,
        price,
        manufactureCost,
        unitOfMeasure,
        org_id,
        org_name,
        org_fullName,
        org_annualTurnover,
        org_type,
        postalAddress_zipcode,
        desc_product_id,
        desc_name,
        desc_coordinate_x,
        desc_coordinate_y,
        desc_creationDate,
        desc_price,
        desc_manufactureCost,
        desc_unitOfMeasure,
        desc_org_id,
        desc_org_name,
        desc_org_fullName,
        desc_org_annualTurnover,
        desc_org_type,
        desc_postalAddress_zipcode;

        public static boolean containsString(String string) {
            for (SortingParams sortingParam : values()) {
                if (sortingParam.name().equalsIgnoreCase(string)) {
                    return true;
                }
            }
            return false;
        }

        public static List<SortingParams> stringToEnum(List<String> stringList) {
            List<SortingParams> answer = new ArrayList<>();
            for (SortingParams sortingParam : values()) {
                for (String stringSortElement : stringList) {
                    if (sortingParam.name().equalsIgnoreCase(stringSortElement)) {
                        answer.add(sortingParam);
                    }
                }
            }
            return answer;
        }
    }

    private List<Product> filteredList(List<Product> productList, List<String> filter) {
        List<Product> result = new ArrayList<>();

        for (String filterElement : filter) {
            String field = Pattern.compile("^(id|name|coordinates\\\\.x|coordinates\\\\.y|creationDate|location\\\\.id|location\\\\.x|location\\\\.y|location\\\\.name|distance)").matcher(filterElement).group(1);
            String comp = Pattern.compile("(==|!=|>|<|>=|<=)").matcher(filterElement).group(1);
            String value = Pattern.compile("(.+)").matcher(filterElement).group(1);

            switch (field) {
                case "id":
                    switch (comp) {
                        case "==":
                            for (Product product : productList) {
                                if (product.getId() == Integer.parseInt(value)) result.add(product);
                            }
                        case "!=":
                            for (Product product : productList) {
                                if (product.getId() != Integer.parseInt(value)) result.add(product);
                            }
                        case ">":
                            for (Product product : productList) {
                                if (product.getId() > Integer.parseInt(value)) result.add(product);
                            }
                        case "<":
                            for (Product product : productList) {
                                if (product.getId() < Integer.parseInt(value)) result.add(product);
                            }
                        case ">=":
                            for (Product product : productList) {
                                if (product.getId() >= Integer.parseInt(value)) result.add(product);
                            }
                        case "<=":
                            for (Product product : productList) {
                                if (product.getId() <= Integer.parseInt(value)) result.add(product);
                            }
                    }

                case "name":
                    switch (comp) {
                        case "==":
                            for (Product product : productList) {
                                if (product.getName().equals(value)) result.add(product);
                            }
                        case "!=":
                            for (Product product : productList) {
                                if (!product.getName().equals(value)) result.add(product);
                            }
                        case ">":
                            for (Product product : productList) {
                                if (product.getName().compareTo(value) > 0) result.add(product);
                            }
                        case "<":
                            for (Product product : productList) {
                                if (product.getName().compareTo(value) < 0) result.add(product);
                            }
                        case ">=":
                            for (Product product : productList) {
                                if (product.getName().compareTo(value) >= 0) result.add(product);
                            }
                        case "<=":
                            for (Product product : productList) {
                                if (product.getName().compareTo(value) <= 0) result.add(product);
                            }
                    }
                case "coordinates\\.x":
                    switch (comp) {
                        case "==":
                            for (Product product : productList) {
                                if (product.getCoordinates().getX() == Integer.parseInt(value)) result.add(product);
                            }
                        case "!=":
                            for (Product product : productList) {
                                if (product.getCoordinates().getX() != Integer.parseInt(value)) result.add(product);
                            }
                        case ">":
                            for (Product product : productList) {
                                if (product.getCoordinates().getX() > Integer.parseInt(value)) result.add(product);
                            }
                        case "<":
                            for (Product product : productList) {
                                if (product.getCoordinates().getX() < Integer.parseInt(value)) result.add(product);
                            }
                        case ">=":
                            for (Product product : productList) {
                                if (product.getCoordinates().getX() >= Integer.parseInt(value)) result.add(product);
                            }
                        case "<=":
                            for (Product product : productList) {
                                if (product.getCoordinates().getX() <= Integer.parseInt(value)) result.add(product);
                            }
                    }

                case "coordinates\\.y":
                    switch (comp) {
                        case "==":
                            for (Product product : productList) {
                                if (product.getCoordinates().getY() == Integer.parseInt(value)) result.add(product);
                            }
                        case "!=":
                            for (Product product : productList) {
                                if (product.getCoordinates().getY() != Integer.parseInt(value)) result.add(product);
                            }
                        case ">":
                            for (Product product : productList) {
                                if (product.getCoordinates().getY() > Integer.parseInt(value)) result.add(product);
                            }
                        case "<":
                            for (Product product : productList) {
                                if (product.getCoordinates().getY() < Integer.parseInt(value)) result.add(product);
                            }
                        case ">=":
                            for (Product product : productList) {
                                if (product.getCoordinates().getY() >= Integer.parseInt(value)) result.add(product);
                            }
                        case "<=":
                            for (Product product : productList) {
                                if (product.getCoordinates().getY() <= Integer.parseInt(value)) result.add(product);
                            }
                    }
                case "creationDate":
                    switch (comp) {

                        case "==":
                            for (Product product : productList) {
                                Date date = Date.from(LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                if (product.getCreationDate().equals(date)) result.add(product);
                            }
                        case "!=":
                            for (Product product : productList) {
                                Date date = Date.from(LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                if (!product.getCreationDate().equals(date)) result.add(product);
                            }
                        case ">":
                            for (Product product : productList) {
                                Date date = Date.from(LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                if (product.getCreationDate().after(date)) result.add(product);
                            }
                        case "<":
                            for (Product product : productList) {
                                Date date = Date.from(LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                if (product.getCreationDate().before(date)) result.add(product);
                            }
                        case ">=":
                            for (Product product : productList) {
                                Date date = Date.from(LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                if (product.getCreationDate().equals(date) || product.getCreationDate().after(date)) result.add(product);
                            }
                        case "<=":
                            for (Product product : productList) {
                                Date date = Date.from(LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault()).toInstant());
                                if (product.getCreationDate().before(date) || product.getCreationDate().equals(date)) result.add(product);
                            }
                    }
            }
        }
        return result;
    }

    private class ProductCompositeComparator implements Comparator<Product> {
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

    private boolean checkCorrectFilter(String filterElement, String value) {
        switch (filterElement) {
            case "id":
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return false;
                }
            case "coordinates\\.x":
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return false;
                }
            case "coordinates\\.y":
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return false;
                }
            case "creationDate":
                try {
                    Date.from(LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault()).toInstant());
                } catch (NullPointerException | IllegalArgumentException e) {
                    return false;
                }
        }
        return true;
    }

    public Response getAllProducts(List<String> sort, List<String> filter, @Min(0) Integer page, @Min(1) Integer pagesCount) throws NotFoundException {

        final String filterRegex = "^(id|name|coordinates\\\\.x|coordinates\\\\.y|creationDate|location\\\\.id|location\\\\.x|location\\\\.y|location\\\\.name|distance)(==|!=|>|<|>=|<=)(.+)";
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
                if (!filter_element.matches(filterRegex)) {
                    return Response.status(400).entity(new ApiResponseMessage("Вы должны были указать параметры сортировки и\n" +
                            "фильтрации в соответствии с требованиями, которые я вам\n" +
                            "указал")).build();
                }
                String field = Pattern.compile("^(id|name|coordinates\\\\.x|coordinates\\\\.y|creationDate|location\\\\.id|location\\\\.x|location\\\\.y|location\\\\.name|distance)").matcher(filter_element).group(1);
                String value = Pattern.compile("(.+)").matcher(filter_element).group(1);
                if (!checkCorrectFilter(field, value)); {
                    return Response.status(400).entity(new ApiResponseMessage("Вы должны были указать параметры сортировки и\n" +
                            "фильтрации в соответствии с требованиями, которые я вам\n" +
                            "указал")).build();
                }
            }
        }


        List<Product> productList = storage.getProductList();
        List<SortingParams> sortingParams = SortingParams.stringToEnum(sort);
        ProductCompositeComparator productCompositeComparator = new ProductCompositeComparator(sortingParams);
        productList.sort(productCompositeComparator);

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
