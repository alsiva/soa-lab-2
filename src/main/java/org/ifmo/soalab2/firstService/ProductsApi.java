package org.ifmo.soalab2.firstService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.NotFoundException;
import org.ifmo.soalab2.model.Product;
import org.ifmo.soalab2.model.ProductWithoutDate;
import org.ifmo.soalab2.model.Products;

import java.util.List;

@Path("/products")
public class ProductsApi {

    @Inject
    private ProductsApiServiceImpl delegate;

    @POST
    @Consumes({"application/xml"})
    @Produces({"application/xml"})
    @Operation(summary = "Метод добавления продукта", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Метод возвращает продукт, которую мы только что создали", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = ProductWithoutDate.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные для продукта"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public Response addProduct(
            @Parameter(in = ParameterIn.DEFAULT, required = true) ProductWithoutDate body
    ) {
        return delegate.addProduct(body);
    }

    @DELETE
    @Path("/{product_id}")
    @Produces({"application/xml"})
    @Operation(summary = "Метод удаления продукта по идентификатору", description = "", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ с удалённым продуктом", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Неверное значение ID"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")}
    )
    public Response deleteProductById(
            @Parameter(in = ParameterIn.PATH, description = "", required = true) @PathParam("product_id") String productId
    ) throws org.ifmo.soalab2.NotFoundException {
        return delegate.deleteProductById(productId);
    }

    @DELETE
    @Path("/manufacture-cost/{cost}")
    @Produces({"application/xml"})
    @Operation(summary = "Метод удаления продукта по полю manufactureCost", description = "", tags = {"Product ExtraOps"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ с удалённым продуктом"),
            @ApiResponse(responseCode = "400", description = "Неверное значение manufactureCost"),
            @ApiResponse(responseCode = "404", description = "Нет данного ресурса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")})
    public Response deleteProductByManufactureCost(
            @Parameter(in = ParameterIn.PATH, description = "", required = true) @PathParam("cost") String manufactureCost
    ) throws org.ifmo.soalab2.NotFoundException {
        return delegate.deleteProductByManufactureCost(manufactureCost);
    }

    @GET
    @Produces({"application/xml"})
    @Operation(summary = "Метод получения списка продуктов", description = "", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ со списком продуктов", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = Products.class))),
            @ApiResponse(responseCode = "400", description = "Вы должны были указать параметры сортировки и фильтрации в соответствии с требованиями, которые я вам указал"),
            @ApiResponse(responseCode = "404", description = "Нет данного ресурса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public Response getAllProducts(
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "Массив полей для сортировки. \"!\" означает сортировку в обратном порядке",
                    schema = @Schema(
                            allowableValues = {
                                    "product_id",
                                    "name",
                                    "coordinate_x",
                                    "coordinate_y",
                                    "creationDate",
                                    "price",
                                    "manufactureCost",
                                    "unitOfMeasure",
                                    "org_id",
                                    "org_name",
                                    "org_fullName",
                                    "org_annualTurnover",
                                    "org_type",
                                    "postalAddress_zipcode",
                                    "desc_product_id",
                                    "desc_name",
                                    "desc_coordinate_x",
                                    "desc_coordinate_y",
                                    "desc_creationDate",
                                    "desc_price",
                                    "desc_manufactureCost",
                                    "desc_unitOfMeasure",
                                    "desc_org_id",
                                    "desc_org_name",
                                    "desc_org_fullName",
                                    "desc_org_annualTurnover",
                                    "desc_org_type",
                                    "desc_postalAddress_zipcode"
                            }
                    )
            ) @QueryParam("sort") List<String> sort,
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "Массив полей для фильтрации. Вы можете указать id, name coordinates_x, coordinates_y, location_id, location_x, location_y, location_name, distance. После одного из этих полей вы указываете символ сравнения (==) (!=) (>) (<) (>=) (<=)."
            ) @QueryParam("filter") List<String> filter,
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "Обозначает номер страницы, которую вывести, при условии, что работает пагинация. Если параметр 'pagesCount' не указан, то по умолчанию количество страниц равно 5. Если оба параметра не указаны, то возвращается полный список.",
                    schema = @Schema(allowableValues = {"0"})
            ) @DefaultValue("1") @QueryParam("page") Integer page,
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "Обозначает количество страниц, на которые произвести разбиение, при условии, что работает пагинация. Если параметр 'page' не указан, то по умолчанию отобразится 1-ая страница. Если оба параметра не указаны, то возвращается полный список.",
                    schema = @Schema(allowableValues = {"1"}, minimum = "1")
            ) @DefaultValue("5") @QueryParam("pagesCount") Integer pagesCount
    ) throws org.ifmo.soalab2.NotFoundException {
        return delegate.getAllProducts(sort, filter, page, pagesCount);
    }

    @GET
    @Path("/{product_id}")
    @Produces({"application/xml"})
    @Operation(summary = "Метод получения продукта по идентификатору", description = "", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ с продуктом", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Неверный параметр"),
            @ApiResponse(responseCode = "404", description = "Нет данного ресурса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public Response getProductById(
            @Parameter(in = ParameterIn.PATH, description = "", required = true) @PathParam("product_id") String productId
    ) throws org.ifmo.soalab2.NotFoundException {
        return delegate.getProductById(productId);
    }

    @GET
    @Path("/max-measure")
    @Produces({"application/xml"})
    @Operation(summary = "Метод получения продукта с максимальным unitOfMeasure", description = "", tags = {"Product ExtraOps"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ с удалённым продуктом", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public Response getProductByMaxUnitOfMeasure() throws org.ifmo.soalab2.NotFoundException {
        return delegate.getProductByMaxUnitOfMeasure();
    }

    @GET
    @Path("/max-annual-turnover/{turnover}")
    @Produces({"application/xml"})
    @Operation(summary = "Метод возвращает продукты, значения поля manufacturer которых меньше указанного", description = "", tags = {"Product ExtraOps"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ с продуктами", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = Products.class))),
            @ApiResponse(responseCode = "400", description = "Неверное значение AnnualTurnover"),
            @ApiResponse(responseCode = "404", description = "Нет данного ресурса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public Response getProductsByLessManufacturerAnnualTurnover(
            @Parameter(in = ParameterIn.PATH, description = "", required = true) @PathParam("turnover") String annualTurnover
    ) throws org.ifmo.soalab2.NotFoundException {
        return delegate.getProductsByLessManufacturerAnnualTurnover(annualTurnover);
    }

    @PUT
    @Path("/{product_id}")
    @Consumes({"application/xml"})
    @Produces({"application/xml"})
    @Operation(summary = "Метод изменения продукта по идентификатору", description = "", tags = {"Product"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ с продуктом", content = @Content(mediaType = "application/xml", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public Response updateProductById(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true) ProductWithoutDate body
            , @Parameter(in = ParameterIn.PATH, description = "", required = true) @PathParam("product_id") Integer productId)
            throws NotFoundException {
        return delegate.updateProductById(body, productId);
    }
}
