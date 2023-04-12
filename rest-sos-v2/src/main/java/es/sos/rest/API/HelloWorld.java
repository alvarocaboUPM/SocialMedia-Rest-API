package com.sos.rest.API;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class HelloWorld {

    @GET
    public Response helloWorld() {
        return Response.ok("hello world").build();
    }
    @Path("/hello")
    @GET
    public Response helloWorld2() {
        return Response.ok("hello world").build();
    }
}
