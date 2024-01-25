package org.ifmo.soalab2.secondService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.ifmo.soalab2.NotFoundException;

@Path("/filter")
public class EbayApi {

    @Inject
    EbayApiServiceImpl delegate;


    @GET
    @Path("price/{priceFrom}/{priceTo}")
    @Produces({"application/xml"})
    public Response getProductsWithRange(@PathParam("priceFrom") String priceFrom, @PathParam("priceTo") String priceTo) throws NotFoundException {
        return delegate.getProductsWithRange(priceFrom, priceTo);
    }

    @GET
    @Path("unit-of-measure/{unit-of-measure}")
    public Response getProductsWithUnitOfMeasure(@PathParam("unit-of-measure") String unitOfMeasure) {
        return delegate.getProductsWithUnitOfMeasure(unitOfMeasure);
    }

}
