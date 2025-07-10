package tech.kood.match_me.user_management.internal.features.getUser;

import java.util.UUID;

import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;
import tech.kood.match_me.user_management.models.User;



public class GetUserHandler {
    
    private final UserRepository userRepository;

    public GetUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GetUserByUsernameResults handle(GetUserByUsernameRequest request) {
        String username = request.username();

        if (username == null || username.isBlank()) {
            return new GetUserByUsernameResults.InvalidUsername(username, request.tracingId());
        }

        try {
            var userQueryResult = userRepository.findUserByUsername(username);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var publicUser = new User(userEntity.id(), userEntity.username(), userEntity.email());

                return new GetUserByUsernameResults.Success(publicUser, request.tracingId());
            } else {
                return new GetUserByUsernameResults.UserNotFound(username, request.tracingId());
            }
        } catch (Exception e) {
            return new GetUserByUsernameResults.SystemError("Failed to retrieve user by username: " + e.getMessage(), request.tracingId());
        }
    }

    public GetUserByEmailResults handle(GetUserByEmailRequest request) {
        String email = request.email();

        if (email == null || email.isBlank()) {
            return new GetUserByEmailResults.InvalidEmail(email, request.tracingId());
        }

        try {
            var userQueryResult = userRepository.findUserByEmail(email);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = new User(userEntity.id(), userEntity.username(), userEntity.email());

                return new GetUserByEmailResults.Success(user, request.tracingId());
            } else {
                return new GetUserByEmailResults.UserNotFound(email, request.tracingId());
            }
        } catch (Exception e) {
            return new GetUserByEmailResults.SystemError("Failed to retrieve user by email: " + e.getMessage(), request.tracingId());
        }
    }

    public GetUserByIdResults handle(GetUserByIdRequest request) {
        UUID id = request.id();

        if (id == null) {
            return new GetUserByIdResults.InvalidUserId(request.tracingId());
        }

        try {
            var userQueryResult = userRepository.findUserById(id);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = new User(userEntity.id(), userEntity.username(), userEntity.email());

                return new GetUserByIdResults.Success(user, request.tracingId());
            } else {
                return new GetUserByIdResults.UserNotFound(id, request.tracingId());
            }
        } catch (Exception e) {
            return new GetUserByIdResults.SystemError("Failed to retrieve user by ID: " + e.getMessage(), request.tracingId());
        }
    }
}