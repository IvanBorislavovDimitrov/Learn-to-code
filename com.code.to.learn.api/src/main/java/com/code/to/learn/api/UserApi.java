package com.code.to.learn.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.to.learn.api.service.UserService;

@Path("/user")
public class UserApi {

    @Autowired
    private UserService userService;

    @GET
    @Path("/")
    public Response getUserServiceInformation() {
        String infoFormUserService = userService == null ? "NEMA GO" : "IMA GO";
        return Response.status(200)
                       .entity(infoFormUserService)
                       .build();
    }
}
