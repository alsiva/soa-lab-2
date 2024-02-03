package org.ifmo.soalab2.secondService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.ApiResponseMessage;
import org.ifmo.soalab2.model.Product;
import org.ifmo.soalab2.model.Products;
import org.ifmo.soalab2.model.UnitOfMeasure;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ApplicationScoped
public class EbayApiServiceImpl {

    private Client createClient() throws Exception {
        char[] password = "123456".toCharArray();
        KeyStore keystore = KeyStore.getInstance("PKCS12");

        try (InputStream keystoreInputStream = getClass().getResourceAsStream("../../../keystore.jks")) {
            keystore.load(keystoreInputStream, password);
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        return ClientBuilder.newBuilder()
//                .sslContext(sslContext)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }


    public Response getProductsWithRange(String rangeFromString, String rangeToString) {
        Client client;
        try {
            client = createClient();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        String clientUrl = new StringBuilder()
                .append("http://localhost:8080/service")
                .append("/api/products")
                .append("?filter=price-gte-")
                .append(rangeFromString) // todo: format
                .append("&filter=price-lte-")
                .append(rangeToString) // todo: format
                .toString();

        Response response = client.target(clientUrl)
                .request(MediaType.APPLICATION_XML)
                .get();

        int responseStatus = response.getStatus();

        if (responseStatus == Response.Status.NOT_FOUND.getStatusCode()) {
            // first service is not available
            return Response
                    .status(Response.Status.SERVICE_UNAVAILABLE)
                    .build();
        }

        if (responseStatus != Response.Status.OK.getStatusCode()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        Products products = response.readEntity(Products.class);

        return Response.status(Response.Status.OK)
                .entity(products)
                .build();

        /*
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

        return Response.ok().entity(products).build();
        */
    }

    public Response getProductsWithUnitOfMeasure(String unitOfMeasureAsString) {
        UnitOfMeasure unitOfMeasure;
        try {
            unitOfMeasure = UnitOfMeasure.valueOf(unitOfMeasureAsString);
        } catch (IllegalArgumentException e) {
            return Response.status(400).entity(new ApiResponseMessage("Неправильные входные параметры")).build();
        }

        //Start of inserted code
        Client client;
        try {
            client = createClient();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        String clientUrl = new StringBuilder()
                .append("http://localhost:8080/service")
                .append("/api/products")
                .append("?filter=unitOfMeasure-eq-")
                .append(unitOfMeasureAsString) // todo: format
                .toString();

        Response response = client.target(clientUrl)
                .request(MediaType.APPLICATION_XML)
                .get();

        int responseStatus = response.getStatus();

        if (responseStatus == Response.Status.NOT_FOUND.getStatusCode()) {
            // first service is not available
            return Response
                    .status(Response.Status.SERVICE_UNAVAILABLE)
                    .build();
        }

        if (responseStatus != Response.Status.OK.getStatusCode()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        Products products = response.readEntity(Products.class);

        return Response.status(Response.Status.OK)
                .entity(products)
                .build();
        //End of inserted code
    }
}
