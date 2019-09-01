package com.code.to.learn.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/hello")
public class Hello {
    @GET
    @Path("/")
    public Response getMsg() {

        String output = "Jersey say : ";

        return Response.status(200)
                       .entity(output)
                       .build();

    }
}
