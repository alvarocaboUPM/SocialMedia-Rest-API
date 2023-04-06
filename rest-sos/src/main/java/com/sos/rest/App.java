package com.sos.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import javax.servlet.*;

import com.sos.rest.API.API;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class App extends Application {

    public static void main(String[] args) throws Exception {
        // Create a new Jetty server instance
        Server server = new Server(8000);

        // Create a new ServletContextHandler
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");

        // Create a new ServletHolder for your JAX-RS application
        // ServletHolder servletHolder = handler.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        // servletHolder.setInitParameter("jersey.config.server.provider.packages", "com.sos.rest");

        // Set the ServletContextHandler as the handler for the Jetty server
        server.setHandler(handler);

        // Start the Jetty server
        server.start();
        server.join();
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(API.class); // Replace with your own resource class
        return s;
    }
}
