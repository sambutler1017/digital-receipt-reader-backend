package com.digital.receipt.app.user.client;

import java.util.List;

import com.digital.receipt.annotations.interfaces.Client;
import com.digital.receipt.app.user.client.domain.User;
import com.digital.receipt.app.user.client.domain.request.UserGetRequest;
import com.digital.receipt.common.abstracts.AbstractClient;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * This class exposes the user endpoint's to other app's to pull data across the
 * platform.
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Client
public class UserClient extends AbstractClient {

    /**
     * Initialize the Abstract client with the active profile and endpoint path.
     */
    public UserClient() {
        super("api/user-app/users");
    }

    /**
     * Get users based on given request filter
     * 
     * @param request of the user
     * @return User profile object {@link User}
     */
    public List<User> getUsers(UserGetRequest request) {
        return get("").toEntityList(User.class).block().getBody();
    }

    /**
     * Client method to get the user given a user id
     * 
     * @param id of the user
     * @return User profile object
     */
    public User getUserById(int id) {
        return get("/{id}", id).toEntity(User.class).block().getBody();
    }

    /**
     * Gets the current logged in user information.
     * 
     * @return The user currently logged in.
     */
    public User getCurrentUser() {
        return get("/current-user").toEntity(User.class).block().getBody();
    }

    /**
     * Update the user's information such as email, first name, last name, and
     * password
     * 
     * @param id   of the user
     * @param user what information on the user needs to be updated.
     * @return user associated to that id with the updated information
     */
    public User updateUser(User user) {
        return put("", user).toEntity(User.class).block().getBody();
    }

    /**
     * Updates a user role, this endpoint can only be used by Admins.
     * 
     * @param id of the user
     * @return user associated to that id with the updated information
     */
    public User updateUserRole(int id, String role) {
        return put("/{id}/role/{role}", null, id, role).toEntity(User.class).block().getBody();
    }

    /**
     * This gets called when a user forgets their password. They will set the forgot
     * password flag and get an email with the temporary password.
     * 
     * @return user associated to that id with the updated information
     */
    public User forgotPassword(@RequestBody String email) {
        return put("/forgot-password", email).toEntity(User.class).block().getBody();
    }

    /**
     * Will delete a user for the given id. This endpoint can only be accessed by a
     * user with admin access.
     * 
     * @param id of the user that is to be deleted.
     * @throws Exception
     */
    public void deleteUser(int id) throws Exception {
        delete("/{id}", null, id).toBodilessEntity().block();
    }
}
