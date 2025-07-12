package tech.kood.match_me.user_management.internal.features.getUser;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import tech.kood.match_me.user_management.internal.database.repostitories.UserRepository;
import tech.kood.match_me.user_management.internal.features.getUser.commands.GetUserByEmailCommand;
import tech.kood.match_me.user_management.internal.features.getUser.commands.GetUserByIdCommand;
import tech.kood.match_me.user_management.internal.features.getUser.commands.GetUserByUsernameCommand;
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



public class GetUserHandler {
    
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public GetUserHandler(UserRepository userRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = applicationEventPublisher;
    }

    @EventListener
    public void handle(GetUserByUsernameCommand command) {
        GetUserByUsernameRequest request = command.getRequest();
        String username = request.username();

        try {
            var userQueryResult = userRepository.findUserByUsername(username);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var userDTO = UserMapper.toUserDTO(userEntity);
                var user = UserMapper.toUser(userEntity);

                command.getResultFuture().complete(new GetUserByUsernameResults.Success(userDTO, request.tracingId()));
                eventPublisher.publishEvent(
                    new GetUserByUsernameEvent(user, request.tracingId())
                );

            } else {
                command.getResultFuture().complete(new GetUserByUsernameResults.UserNotFound(username, request.tracingId()));
            }
        } catch (Exception e) {
            command.getResultFuture().complete(
                new GetUserByUsernameResults.SystemError("Failed to retrieve user by username: " + e.getMessage(), request.tracingId())
            );
        }
    }

    @EventListener
    public void handle(GetUserByEmailCommand command) {
        GetUserByEmailRequest request = command.getRequest();
        String email = request.email();


        try {
            var userQueryResult = userRepository.findUserByEmail(email);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var userDTO = UserMapper.toUserDTO(userEntity);

                command.getResultFuture().complete(new GetUserByEmailResults.Success(userDTO, request.requestId(), request.tracingId()));
                eventPublisher.publishEvent(
                    new GetUserByEmailEvent(UserMapper.toUser(userEntity), request.tracingId())
                );
            } else {
                command.getResultFuture().complete(new GetUserByEmailResults.UserNotFound(email, request.requestId(), request.tracingId()));
            }
        } catch (Exception e) {
            command.getResultFuture().complete(
                new GetUserByEmailResults.SystemError("Failed to retrieve user by email: " + e.getMessage(), request.requestId(), request.tracingId())
            );
        }
    }

    @EventListener
    public void handle(GetUserByIdCommand command) {
        GetUserByIdRequest request = command.getRequest();
        UUID id = request.userId();

        try {
            var userQueryResult = userRepository.findUserById(id);
            if (userQueryResult.isPresent()) {
                var userEntity = userQueryResult.get();
                var userDTO = UserMapper.toUserDTO(userEntity);

                command.getResultFuture().complete(new GetUserByIdResults.Success(userDTO, request.requestId(), request.tracingId()));
                eventPublisher.publishEvent(
                    new GetUserByIdEvent(UserMapper.toUser(userEntity), request.tracingId())
                );
            } else {
                command.getResultFuture().complete(new GetUserByIdResults.UserNotFound(id, request.requestId(), request.tracingId()));
            }
        } catch (Exception e) {
            command.getResultFuture().complete(new GetUserByIdResults.SystemError("Failed to retrieve user by ID: " + e.getMessage(), request.requestId(), request.tracingId()));
        }
    }
}