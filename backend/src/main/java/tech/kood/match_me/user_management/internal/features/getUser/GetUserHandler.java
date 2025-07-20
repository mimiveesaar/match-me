package tech.kood.match_me.user_management.internal.features.getUser;

import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;

@Service
public class GetUserHandler {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUserHandler(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

       public GetUserByUsernameResults handle(GetUserByUsernameRequest request ) {
        String username = request.username();

        try {
            var userQueryResult = userRepository.findUserByUsername(username);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var userDTO = userMapper.toUserDTO(userEntity);

                return new GetUserByUsernameResults.Success(userDTO, request.tracingId());
            } else {
                return new GetUserByUsernameResults.UserNotFound(username, request.tracingId());
            }
        } catch (Exception e) {
            return new GetUserByUsernameResults.SystemError("Failed to retrieve user by username: " + e.getMessage(), request.tracingId());
        }
    }


    public GetUserByEmailResults handle(GetUserByEmailRequest request) {
        String email = request.email();

        try {
            var userQueryResult = userRepository.findUserByEmail(email);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var userDTO = userMapper.toUserDTO(userEntity);

                return new GetUserByEmailResults.Success(userDTO, request.requestId(), request.tracingId());
            } else {
                return new GetUserByEmailResults.UserNotFound(email, request.requestId(), request.tracingId());
            }
        } catch (Exception e) {
            return new GetUserByEmailResults.SystemError("Failed to retrieve user by email: " + e.getMessage(), request.requestId(), request.tracingId());
        }
    }

    public GetUserByIdResults handle(GetUserByIdRequest request) {
        UUID id = request.userId();

        try {
            var userQueryResult = userRepository.findUserById(id);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var userDTO = userMapper.toUserDTO(userEntity);

                return new GetUserByIdResults.Success(userDTO, request.requestId(), request.tracingId());
            } else {
                return new GetUserByIdResults.UserNotFound(id, request.requestId(), request.tracingId());
            }
        } catch (Exception e) {
            return new GetUserByIdResults.SystemError("Failed to retrieve user by ID: " + e.getMessage(), request.requestId(), request.tracingId());
        }
    }
}