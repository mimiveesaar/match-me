package tech.kood.match_me.user_management.api;

import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserRequest;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;

public interface UserManagement {
    
    /**
     * Registers a new user with the provided details.
     *
     * @param request The registration request containing user details.
     * @return A result indicating the success or failure of the registration.
     */
    RegisterUserResults registerUser(RegisterUserRequest request);

    /**
     * Retrieves user information based on the provided email address.
     *
     * @param request The request object containing the email to search for.
     * @return A {@link GetUserByEmailResults} object containing the user details if found.
     */
    GetUserByEmailResults getUserByEmail(GetUserByEmailResults request);

    /**
     * Retrieves user information based on the provided username.
     *
     * @param request The request object containing the username to search for.
     * @return A {@link GetUserByUsernameResults} object containing the user details if found.
     */
    GetUserByUsernameResults getUserByUsername(GetUserByUsernameResults request);

    /**
     * Retrieves user information based on the provided user ID.
     *
     * @param request The request object containing the user ID to search for.
     * @return A {@link GetUserByIdResults} object containing the user details if found.
     */
    GetUserByIdResults getUserById(GetUserByIdResults request);

    
}