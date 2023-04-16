

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
import org.junit.Test;
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
        System.out.println(resGetUsers.readEntity(String.class));

        //----------------------------------------------------------------------
        // Ver los datos básicos de un usuario
        //----------------------------------------------------------------------
        /**
         *  @Path("/users/{id}")
         *     @GET
         *     @Produces(MediaType.TEXT_XML)
         *     public Response getUserById(@PathParam("id") long id) {
         *         // User user = userService.getUserById(id);
         *         // if (user == null) {
         *         //     return Response.status(Response.Status.NOT_FOUND).build();
         *         // }
         *         // return Response.status(Response.Status.OK).entity(user).build();
         *         //return Response.ok("Hello Word").build();
         *         return DB.getUserById(id);
         *     }
         */
        Response resBasic = target.path("users").path("32").request().accept(MediaType.TEXT_XML).get();
        System.out.println(resBasic.readEntity(String.class));

        //----------------------------------------------------------------------
        // Cambiar datos básicos de nuestro perfil de usuario
        //----------------------------------------------------------------------
        /**
         *     @Path("/users/{id}")
         *     @PUT
         *     @Consumes(MediaType.TEXT_XML)
         *     @Produces(MediaType.TEXT_XML)
         *     public Response updateUser(@PathParam("id") long id, User updatedUser) {
         *         // User user = userService.updateUser(id, updatedUser);
         *         // if (user == null) {
         *         //     return Response.status(Response.Status.NOT_FOUND).build();
         *         // }
         *         // return Response.status(Response.Status.OK).entity(user).build();
         *         return Response.ok("Hello Word").build();
         *     }
         */
        User cambios = new User();
        cambios.setName("SilverEagle");
        cambios.setEmail("nuevomail@mail.com");
        Response resUserMod = target.path("users").path("1").request().post(Entity.entity(cambios, MediaType.TEXT_XML));
        System.out.println(resUserMod);
        //----------------------------------------------------------------------
        // Borrar nuestro perfil de la red social
        //----------------------------------------------------------------------
        /**
         *   @Path("/users/{id}")
         *     @DELETE
         *     @Produces(MediaType.TEXT_XML)
         *     public Response deleteUser(@PathParam("id") long id) {
         *         // User user = userService.deleteUser(id);
         *         // if (user == null) {
         *         //     return Response.status(Response.Status.NOT_FOUND).build();
         *         // }
         *         // return Response.status(Response.Status.OK).entity(user).build();
         *         return DB.deleteUserById(id);
         *     }
         */

        Response resAutoremove = target.path("users").path("1").request().delete();
        System.out.println(resAutoremove);
        //----------------------------------------------------------------------
        // Añadir un nuevo amigo de entre los usuarios registrados en la red social
        //----------------------------------------------------------------------
        /**
         * @POST
         *     @Path("/users/{id}/friends")
         *     @Consumes(MediaType.TEXT_XML)
         *     public Response addFriend(@PathParam("id") Long id, String friend) throws JAXBException {
         *         User amigo = XmlToUserConverter.fromXml(friend);
         *         // code to add friend
         *         return DB.postAddFriend(id,amigo.getUserId().intValue());
         *     }
         */
         //Response resAddFriend  =
        //----------------------------------------------------------------------
        // Listado de amigos
        //----------------------------------------------------------------------
        /**
         *   @GET
         *     @Path("/users/{id}/friends")
         *     @Produces(MediaType.TEXT_XML)
         *     public Response getFriends(
         *             @PathParam("id") Long id,
         *             @QueryParam("q") String q,
         *             @QueryParam("limit") Integer limit
         *
         *     ) {
         *         return DB.getUserFriends(id, q, limit);
         *     }
         */

        Response resListFriends = target.path("users").path("1").path("friends").queryParam("limit", 3).queryParam("offset", "").request().accept(MediaType.TEXT_XML).get();
        System.out.println(resBasic.readEntity(String.class));
        //----------------------------------------------------------------------
        // Eliminar un amigo
        //----------------------------------------------------------------------
        /**
         *     @Path("/users/{id}")
         *     @DELETE
         *     @Produces(MediaType.TEXT_XML)
         *     public Response deleteUser(@PathParam("id") long id) {
         *         // User user = userService.deleteUser(id);
         *         // if (user == null) {
         *         //     return Response.status(Response.Status.NOT_FOUND).build();
         *         // }
         *         // return Response.status(Response.Status.OK).entity(user).build();
         *         return DB.deleteUserById(id);
         *     }
         */

        Response resDelFriend = target.path("users").path("1").path("friends").path("3").request().delete();
        System.out.println(resDelFriend);
        //----------------------------------------------------------------------
        // Un usuario puede publicar un nuevo mensaje en su página personal
        //----------------------------------------------------------------------
        /**
         *  @POST
         *     @Path("/users/{id}/friends")
         *     @Consumes(MediaType.TEXT_XML)
         *     public Response addFriend(@PathParam("id") Long id, String friend) throws JAXBException {
         *         User amigo = XmlToUserConverter.fromXml(friend);
         *         // code to add friend
         *         return DB.postAddFriend(id,amigo.getUserId().intValue());
         *     }
         */
        // Response resPostMsg


        //----------------------------------------------------------------------
        // Obtener una lista de todos los mensajes escritos por un usuario en su página personal.
        // Además, esta lista debe permitir la opción de ser filtrada por
        // fecha o limitar la cantidad de información obtenida por número
        //----------------------------------------------------------------------
        /**
         *     @Path("/users/{id}/messages/friends/search")
         *     @GET
         *     @Produces(MediaType.TEXT_XML)
         *     public Response searchUserMessagesWithContent(
         *             @PathParam("id") Long id,
         *             @QueryParam("q") String query,
         *             @QueryParam("limit") Integer limit
         *
         *     )
         *     {
         *         return DB.getFriendsPostsByContent(id, query, limit);
         *     }
         */

         Response resGetSelfMsg = target.path("users").path("1").path("messages").request().get();
        System.out.println(resGetSelfMsg);

        //----------------------------------------------------------------------
        // Obtener detalles sobre un mensaje específico
        //----------------------------------------------------------------------
        /**
         *     @Path("/users/{id}/messages/{message_id}")
         *     @GET
         *     @Produces(MediaType.TEXT_XML)
         *     public Response getMessage(
         *             @PathParam("id") Long id,
         *             @PathParam("message_id") Long messageId
         *     ) {
         *         return DB.getSpecificPost(id, messageId);
         *
         *     }
         */
         Response resPersonalMsg = target.path("users").path("1").path("messages").path("1").request().get();
         System.out.println(resPersonalMsg.getStatus());

        //----------------------------------------------------------------------
        // Un usuario puede editar un mensaje de su página personal
        //----------------------------------------------------------------------


        //----------------------------------------------------------------------
        //  Un usuario puede eliminar un mensaje de su página personal
        //----------------------------------------------------------------------

        /**
         *   @Path("/users/{id}/messages/{message_id}")
         *     @DELETE
         *     @Produces(MediaType.TEXT_XML)
         *     public Response deleteMessage(
         *             @PathParam("id") Long id,
         *             @PathParam("message_id") Long messageId
         *     ) {
         *         // Eliminar el mensaje de la base de datos
         *         DB.deleteSpecificPost(id, messageId);
         *
         *         // Devolver una respuesta que indique que el mensaje se eliminó correctamente
         *         return Response.status(Response.Status.OK)
         *                 .entity("<message>El mensaje se eliminó correctamente</message>")
         *                 .build();
         *     }
         */
        Response resRmvMsg =target.path("users").path("1").path("messages").path("1").request().delete();
        System.out.println(resRmvMsg.getStatus());

        //----------------------------------------------------------------------
        // Consultar los últimos mensajes publicados de nuestros amigos
        // en su página personal obtener una lista de los últimos mensajes de todos mis amigos,
        // pudiendo filtrar estos mensajepor fecha (último antes de cierta fecha).
        // Poder limitar la cantidad de inform
        //----------------------------------------------------------------------

        /**
         * @Path("/users/{id}/messages/friends")
         *     @GET
         *     @Produces(MediaType.TEXT_XML)
         *     public Response searchUserMessages(
         *             @PathParam("id") Long id,
         *             @QueryParam("q") String query,
         *             @QueryParam("limit") Integer limit, // default limit is 10
         *             @QueryParam("sdate") String startDate,
         *             @QueryParam("edate") String endDate
         *     ) {
         *
         *         try {
         *
         *             return DB.getFriendsPostsByDate(id, startDate, endDate, limit );
         *
         *         } catch (InvalidParameterException e) {
         *             return Response.status(Response.Status.BAD_REQUEST).build();
         *
         *         } catch (ForbiddenException e) {
         *             return Response.status(Response.Status.FORBIDDEN).build();
         *         }
         *     }
         */
         Response resFrendMsgs = target.path("users").path("1").path("messages").path("friends").request().get();
         System.out.println(resFrendMsgs);


//Buscar en todos los mensajes de nuestros amigos por contenido. Poder limitar la cantidad de información obtenida por número.


        //----------------------------------------------------------------------
        //  Un usuario puede eliminar un mensaje de su página personal
        //----------------------------------------------------------------------
        /**
         *     @Path("/users/{id}/messages/friends/search")
         *     @GET
         *     @Produces(MediaType.TEXT_XML)
         *     public Response searchUserMessagesWithContent(
         *             @PathParam("id") Long id,
         *             @QueryParam("q") String query,
         *             @QueryParam("limit") Integer limit
         *
         *     )
         *     {
         *         return DB.getFriendsPostsByContent(id, query, limit);
         *     }
         */

        Response resFilterMsg = target.path("users").path("1").path("messages").path("friends").queryParam("good").request().get();
        System.out.println(resFilterMsg);
    }



}
