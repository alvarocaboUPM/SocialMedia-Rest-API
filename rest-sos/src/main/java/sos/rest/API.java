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
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_XML)
    public Response addUser(User user) {
        return DB.createUser(user.getUserId(), user.getName(), user.getAge());
    }

    @Path("/users")
    @GET
    @Produces(MediaType.TEXT_XML)
    public Response getAllUsers(@QueryParam("q") String query) {
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
        return Response.ok("Hello Word").build();
    }

    // POST /users/{id}/friends
    @POST
    @Path("/users/{id}/friends")
    @Consumes(MediaType.TEXT_XML)
    public Response addFriend(@PathParam("id") String id, User friend) {
        // code to add friend
        return Response.ok().build();
    }

    // GET /users/{id}/friends
    @GET
    @Path("/users/{id}/friends")
    @Produces(MediaType.TEXT_XML)
    public Response getFriends(
        @PathParam("id") String id,
        @QueryParam("q") String searchQuery,
        @QueryParam("limit") int limit
    ) {
        // code to get friends
        //TODO: Implementar esto en MessageService
        List<User> friends = null;
        return Response.ok(friends).build();
    }

    // DELETE /users/{id}/friends/{friend_id}
    @DELETE
    @Path("/users/{id}/friends/{friend_id}")
    public Response deleteFriend(
        @PathParam("id") String id,
        @PathParam("friend_id") String friendId
    ) {
        // code to delete friend
        return Response.ok().build();
    }

    // POST /users/{id}/messages
    @POST
    @Path("/users/{id}/messages")
    @Consumes(MediaType.TEXT_XML)
    public Response addMessage(
        @PathParam("id") String id,
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
        @PathParam("id") String id,
        @QueryParam("filter") String filter,
        @QueryParam("limit") int limit
    ) {
        // code to get messages
        //TODO: Implementar esto en MessageService
        List<Message> messages = null;
        return Response.ok(messages).build();
    }

    // GET /users/{id}/messages/{message_id}
    @GET
    @Path("/users/{id}/messages/{message_id}")
    @Produces(MediaType.TEXT_XML)
    public Response getMessage(
        @PathParam("id") String id,
        @PathParam("message_id") String messageId
    ) {
        return null;

        //TODO: Implementar esto en MessageService
        // Message message = messageService.getMessageByID(Long id, long message_id);
        // if (message == null) {
        //     return Response.status(Response.Status.NOT_FOUND).build();
        // } else {
        //     return Response.ok(message).build();
        // }
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