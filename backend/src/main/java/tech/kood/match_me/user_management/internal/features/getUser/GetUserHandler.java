package tech.kood.match_me.user_management.internal.features.getUser;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.events.GetUserByEmailEvent;
import tech.kood.match_me.user_management.internal.features.getUser.events.GetUserByIdEvent;
import tech.kood.match_me.user_management.internal.features.getUser.events.GetUserByUsernameEvent;
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
    private final ApplicationEventPublisher events;

    public GetUserHandler(
        UserRepository userRepository,
        UserMapper userMapper,
        ApplicationEventPublisher events) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.events = events;
    }

       public GetUserByUsernameResults handle(GetUserByUsernameRequest request) {
        String username = request.username();

        try {
            var userQueryResult = userRepository.findUserByUsername(username);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);
                var result = new GetUserByUsernameResults.Success(user, request.tracingId());

                events.publishEvent(new GetUserByUsernameEvent(request, result));
                return result;
            } else {
                var result = new GetUserByUsernameResults.UserNotFound(username, request.tracingId());
                events.publishEvent(new GetUserByUsernameEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = new GetUserByUsernameResults.SystemError("Failed to retrieve user by username: " + e.getMessage(), request.tracingId());
            events.publishEvent(new GetUserByUsernameEvent(request, result));
            return result;
        }
    }

    public GetUserByEmailResults handle(GetUserByEmailRequest request) {
        String email = request.email();

        try {
            var userQueryResult = userRepository.findUserByEmail(email);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);

                var result = new GetUserByEmailResults.Success(user, request.requestId(), request.tracingId());
                events.publishEvent(new GetUserByEmailEvent(request, result));
                return result;
            } else {
                var result = new GetUserByEmailResults.UserNotFound(email, request.requestId(), request.tracingId());
                events.publishEvent(new GetUserByEmailEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = new GetUserByEmailResults.SystemError("Failed to retrieve user by email: " + e.getMessage(), request.requestId(), request.tracingId());
            events.publishEvent(new GetUserByEmailEvent(request, result));
            return result;
        }
    }

    public GetUserByIdResults handle(GetUserByIdRequest request) {
        UUID id = request.userId();

        try {
            var userQueryResult = userRepository.findUserById(id);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);

                var result = new GetUserByIdResults.Success(user, request.requestId(), request.tracingId());
                events.publishEvent(new GetUserByIdEvent(request, result));
                return result;
            } else {
                var result = new GetUserByIdResults.UserNotFound(id, request.requestId(), request.tracingId());
                events.publishEvent(new GetUserByIdEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = new GetUserByIdResults.SystemError("Failed to retrieve user by ID: " + e.getMessage(), request.requestId(), request.tracingId());
            events.publishEvent(new GetUserByIdEvent(request, result));
            return result;
        }
    }
}