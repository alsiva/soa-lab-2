package org.ifmo.soalab2.secondService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.NotFoundException;

@Path("/ebay")
public class EbayApi {

    @Inject
    EbayApiServiceImpl delegate;


    @GET
    @Path("filter/price/{priceFrom}/{priceTo}")
    @Produces({"application/xml"})
    public Response getProductsWithRange(@PathParam("priceFrom") String priceFrom, @PathParam("priceTo") String priceTo) throws NotFoundException {
        return delegate.getProductsWithRange(priceFrom, priceTo);
    }

    @GET
    public Response getProductsWithUnitOfMeasure(String unitOfMeasure) {
        return delegate.getProductsWithUnitOfMeasure(unitOfMeasure);
    }

}
