package sos.rest;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import sos.rest.db.DB;
import sos.rest.models.*;

@Path ("/api/v1")
public class API {

    @Path("/users")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response addUser(User user) {
        return DB.createUser(user.getName(), user.getEmail(), user.getAge());
    }
    @Path("/users")
    @GET
    @Produces(MediaType.TEXT_XML)
    public Response getAllUsers(
        @QueryParam("q") String query) {
        return DB.getUsers(query);
    }
    

    @Path("/users/{id}")
    @GET
    @Produces(MediaType.TEXT_XML)
    public Response getUserById(@PathParam("id") long id) {
        // User user = userService.getUserById(id);
        // if (user == null) {
        //     return Response.status(Response.Status.NOT_FOUND).build();
        // }
        // return Response.status(Response.Status.OK).entity(user).build();
        return Response.ok("Hello Word").build();
    }

    @Path("/users/{id}")
    @PUT
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_XML)
    public Response updateUser(@PathParam("id") long id, User updatedUser) {
        // User user = userService.updateUser(id, updatedUser);
        // if (user == null) {
        //     return Response.status(Response.Status.NOT_FOUND).build();
        // }
        // return Response.status(Response.Status.OK).entity(user).build();
        return Response.ok("Hello Word").build();
    }

    @Path("/users/{id}")
    @DELETE
    @Produces(MediaType.TEXT_XML)
    public Response deleteUser(@PathParam("id") long id) {
        // User user = userService.deleteUser(id);
        // if (user == null) {
        //     return Response.status(Response.Status.NOT_FOUND).build();
        // }
        // return Response.status(Response.Status.OK).entity(user).build();
        return DB.deleteUserById(id);
    }

    // POST /users/{id}/friends
    @POST
    @Path("/users/{id}/friends")
    @Consumes(MediaType.TEXT_XML)
    public Response addFriend(@PathParam("id") Long id, User friend) {
        // code to add friend
        return Response.ok().build();
    }

    // GET /users/{id}/friends
    @GET
    @Path("/users/{id}/friends")
    @Produces(MediaType.TEXT_XML)
    public Response getFriends(
        @PathParam("id") Long id,
        @QueryParam("q") String q,
        @QueryParam("limit") int limit,
        @QueryParam("offset") Integer offset
    ) {
        return DB.getUserFriends(id, q, limit);
    }

    // DELETE /users/{id}/friends/{friend_id}
    @DELETE
    @Path("/users/{id}/friends/{friend_id}")
    public Response deleteFriend(
        @PathParam("id") Long id,
        @PathParam("friend_id") Long friendId
    ) {
        // code to delete friend
        return DB.deleteUserFriend(id, friendId);
    }

    // POST /users/{id}/messages
    @Path("/users/{id}/messages")
    @POST
    @Consumes(MediaType.TEXT_XML)
    public Response addMessage(
        @PathParam("id") Long id,
        Message message
    ) {
        // code to add message
        return Response.ok().build();
    }

    // GET /users/{id}/messages
    @GET
    @Path("/users/{id}/messages")
    @Produces(MediaType.TEXT_XML)
    public Response getMessages(
        @PathParam("id") Long id,
        @QueryParam("filter") String filter,
        @QueryParam("limit") Integer limit,
        @QueryParam("offset") Integer offset,
        @QueryParam("sdate") String startDate,
        @QueryParam("edate") String endDate
    ) {
        //return Response.ok("Hello").build();
        return DB.getPosts(id, filter, limit, offset, startDate, endDate);
    }
    

    // GET /users/{id}/messages/{message_id}
    @Path("/users/{id}/messages/{message_id}")
    @GET
    @Produces(MediaType.TEXT_XML)
    public Response getMessage(
        @PathParam("id") Long id,
        @PathParam("message_id") String messageId
    ) {
        return null;

    }

    @Path("/users/{id}/messages/{message_id}")
    @DELETE
    @Produces(MediaType.TEXT_XML)
    public Response deleteMessage(
        @PathParam("id") Long id,
        @PathParam("message_id") Long messageId
    ) {
        // Eliminar el mensaje de la base de datos
        DB.deleteSpecificPost(id, messageId);

        // Devolver una respuesta que indique que el mensaje se eliminó correctamente
        return Response.status(Response.Status.OK)
            .entity("<message>El mensaje se eliminó correctamente</message>")
            .build();
    }

    @Path("/users/users/{id}/messages/friends/search")
    @GET
    @Produces(MediaType.TEXT_XML)
    public Response searchUserMessages(
            @PathParam("id") Long id,
            @QueryParam("q") String query,
            @QueryParam("limit") @DefaultValue("10") int limit // default limit is 10
        ) {
        
        try {
            
            // get the user by id
            //User user = userService.getUserById(id);
            
            // search the user's friend messages TODO: Implementar esto en MessageService
            // List<Message> friendMessages = user.searchFriendMessages(query, limit);
            
            // create a list of user objects to return
            List<User> friends = new ArrayList<>();
            // for (Message message : friendMessages) {
            //     friends.add(message.getAuthor());
            // }
            
            return Response.ok(friends).build();
            
        } catch (InvalidParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
            
        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}