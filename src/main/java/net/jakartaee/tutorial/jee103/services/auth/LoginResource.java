package net.jakartaee.tutorial.jee103.services.auth;

import static net.jakartaee.tutorial.auth.JwtHandler.BEARER;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.jakartaee.tutorial.ApplicationResources;
import net.jakartaee.tutorial.auth.JwtHandler;
import net.jakartaee.tutorial.auth.PasswordHandler;
import net.jakartaee.tutorial.data.UserDAO;
import net.jakartaee.tutorial.exceptions.AuthnException;
import net.jakartaee.tutorial.exceptions.AuthzException;
import net.jakartaee.tutorial.exceptions.NotFoundException;
import net.jakartaee.tutorial.model.ErrorResponse;
import net.jakartaee.tutorial.model.User;
import net.jakartaee.tutorial.model.UserDB;

@Path("/auth/")
public class LoginResource {
	@Context
	private HttpServletRequest req;
 
	@Context
    private ServletContext servletContext; 		// Necessary to set Request Scope Attr
    
    
	@POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserDB user) {

     	try {
     		UserDB dbUser = new UserDAO().getUserByUsername(user.getUsername().toLowerCase());		// username is stored in lower case
     		PasswordHandler pwh = new PasswordHandler(dbUser.getPwdsalt());			// Create PasswordHandler with the saved (dbUser)  SALT
     		//pwh.checkPassword(dbUser.getPwdhash(), user.getPassword());

     		User returnUser = new User(dbUser);
			return Response.ok(returnUser, MediaType.APPLICATION_JSON).build();
		} catch (NotFoundException nfe) {
			ErrorResponse response = new ErrorResponse("Access denied.", "Credentials do not match any authorized users.", 409);
			return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(response).build();	
		} catch (Exception e) {
			e.printStackTrace();
			//return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(e.getErrorResponse()).build();
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(e.getMessage()).build();
		}

    }
	
//    @DELETE
//    @Path("logout")
//    public Response logoutUser() {
//    	//
//    	// If you want to do a HARD logout of the API, then you can add a hashSet with the logged in users,
//    	// and delete the username from the hashSet when the user logs out.
//    	// The current implementation will just naturally logout the API after the inactivity timeout, which is typically short ( < 20 min )
//    	//
//    	//
//    }

}
