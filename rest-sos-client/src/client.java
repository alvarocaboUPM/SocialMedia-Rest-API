

//import javax.ws.rs.client.config.DefaultClientConfig;
//import javax.ws.rs.representation.Form;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
//import javax.ws.rs.client.config.DefaultClientConfig;
//import javax.ws.rs.representation.Form;
import models.*;
import services.*;
public class client {
    //TODO : Modify URI
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/rest_sos_war_exploded/api/v1").build();
    }
    public static void main(String[] args) throws Exception {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(getBaseURI());

        //----------------------------------------------------------------------
        // Añadir un usuario nuevo a la red
        //----------------------------------------------------------------------
        /**
         *    @Path("/users")
         *     @POST
         *     @Consumes(MediaType.TEXT_XML)
         *     @Produces(MediaType.TEXT_XML)
         *     public Response addUser(User user) {
         *         // Agregar usuario a la base de datos
         *         int result = DB.addUserToDB(user);
         *         if (result == 1) {
         *             // Si se agregó correctamente, devolver código 201 Created
         *             return Response.status(Response.Status.CREATED).build();
         *         } else {
         *             // Si falló la inserción, devolver código 400 Bad Request
         *             return Response.status(Response.Status.BAD_REQUEST).build();
         *         }
         *     }
         */

        //User test = new User("Juan", "juani1782@mail.com",23);
        //Response r1 = target.path("users").request().accept(MediaType.TEXT_XML).post(Entity.entity(test, MediaType.TEXT_XML ));
        //System.out.println(r1.getStatus());
        /**
         * provoca Error!
         */
        //----------------------------------------------------------------------
        // Obtener una lista de todos los usuarios existentes en la red social.
        //----------------------------------------------------------------------
        /**
         *   @Path("/users")
         *     @GET
         *     @Produces(MediaType.TEXT_XML)
         *     public Response getAllUsers(
         *             @QueryParam("q") String query) {
         *         return DB.getUsers(query);
         *     }
         */

        Response resGetUsers = target.path("users").request().accept(MediaType.TEXT_XML).get();
        System.out.println(rUsers.readEntity(String.class));

        //----------------------------------------------------------------------
        // Ver los datos básicos de un usuario
        //----------------------------------------------------------------------
        Re

    }



}
