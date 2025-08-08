package tech.kood.match_me.user_management.internal.domain.features.getUser;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.domain.features.getUser.events.GetUserByEmailEvent;
import tech.kood.match_me.user_management.internal.domain.features.getUser.events.GetUserByIdEvent;
import tech.kood.match_me.user_management.internal.domain.features.getUser.events.GetUserByUsernameEvent;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByEmailQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByIdQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.requests.GetUserByUsernameQuery;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByEmailQueryResults;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByIdQueryResults;
import tech.kood.match_me.user_management.internal.domain.features.getUser.results.GetUserByUsernameResults;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;

@Service
public class GetUserHandler {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher events;

    public GetUserHandler(UserRepository userRepository, UserMapper userMapper,
            ApplicationEventPublisher events) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.events = events;
    }

    public GetUserByUsernameResults handle(GetUserByUsernameQuery request) {
        String username = request.username;

        try {
            var userQueryResult = userRepository.findUserByUsername(username);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);
                var result = GetUserByUsernameResults.Success.of(user, request.requestId,
                        request.tracingId);

                events.publishEvent(new GetUserByUsernameEvent(request, result));
                return result;
            } else {
                var result = GetUserByUsernameResults.UserNotFound.of(username, request.requestId,
                        request.tracingId);
                events.publishEvent(new GetUserByUsernameEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = GetUserByUsernameResults.SystemError.of(
                    "Failed to retrieve user by username: " + e.getMessage(), request.requestId,
                    request.tracingId);
            events.publishEvent(new GetUserByUsernameEvent(request, result));
            return result;
        }
    }

    public GetUserByEmailQueryResults handle(GetUserByEmailQuery request) {
        String email = request.email;

        try {
            var userQueryResult = userRepository.findUserByEmail(email);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);

                var result = GetUserByEmailQueryResults.Success.of(user, request.requestId,
                        request.tracingId);
                events.publishEvent(new GetUserByEmailEvent(request, result));
                return result;
            } else {
                var result = GetUserByEmailQueryResults.UserNotFound.of(email, request.requestId,
                        request.tracingId);
                events.publishEvent(new GetUserByEmailEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = GetUserByEmailQueryResults.SystemError.of(
                    "Failed to retrieve user by email: " + e.getMessage(), request.requestId,
                    request.tracingId);
            events.publishEvent(new GetUserByEmailEvent(request, result));
            return result;
        }
    }

    public GetUserByIdQueryResults handle(GetUserByIdQuery request) {

        try {
            var userQueryResult = userRepository.findUserById(request.requestId);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var user = userMapper.toUser(userEntity);

                var result = GetUserByIdQueryResults.Success.of(user, request.requestId,
                        request.tracingId);
                events.publishEvent(new GetUserByIdEvent(request, result));
                return result;
            } else {
                var result = GetUserByIdQueryResults.UserNotFound.of(request.userId,
                        request.requestId, request.tracingId);
                events.publishEvent(new GetUserByIdEvent(request, result));
                return result;
            }
        } catch (Exception e) {
            var result = GetUserByIdQueryResults.SystemError.of(
                    "Failed to retrieve user by ID: " + e.getMessage(), request.requestId,
                    request.tracingId);
            events.publishEvent(new GetUserByIdEvent(request, result));
            return result;
        }
    }
}
