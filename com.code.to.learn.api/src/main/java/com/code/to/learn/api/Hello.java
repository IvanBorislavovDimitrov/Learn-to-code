package com.code.to.learn.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/*")
public class Hello {
    @GET
    @Path("/")
    public Response getMsg() {

        String output = "Jersey : ";

        return Response.status(200)
                       .entity(output)
                       .build();

    }

    @GET
    @Path("/putki")
    public Response getAnotherMessage() {

        String output = "Prusnaha se";

        return Response.status(200)
                       .entity(output)
                       .build();

    }
}
