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
    public Response getAllProducts(
            @QueryParam("sort") List<String> sort,
            @QueryParam("filter") List<String> filter,
            @QueryParam("pageIndex") Integer pageIndex,
            @QueryParam("pageSize") Integer pageSize
    ) throws org.ifmo.soalab2.NotFoundException {
        return delegate.getAllProducts(sort, filter, pageIndex, pageSize);
    }

    @GET
    @Path("/insert-sample-data")
    @Produces({"application/xml"})
    public Response insertSampleData() {
        return delegate.insertSampleData();
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
