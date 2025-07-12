package tech.kood.match_me.user_management;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.user_management.DTOs.RegisterUserRequestDTO;
import tech.kood.match_me.user_management.DTOs.RegisterUserResultsDTO;
import tech.kood.match_me.user_management.internal.common.Command;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserRequest;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
import tech.kood.match_me.user_management.internal.mappers.UserMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user-management")
public class UserManagementController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/ping")
    public String ping() {
        return "User Management Service is running.";
    }

    @PostMapping("/register")
    public RegisterUserResultsDTO registerUser(@RequestBody RegisterUserRequestDTO request) {
        // Publish the RegisterUserRequest event to handle user registration

        var internalRequest = new RegisterUserRequest(
                UUID.randomUUID(),
                request.username(),
                request.password(),
                request.email(),
                Optional.ofNullable(request.tracingId())
        );

        Command<RegisterUserRequest, RegisterUserResults> command = new Command<RegisterUserRequest, RegisterUserResults>(internalRequest);
        applicationEventPublisher.publishEvent(request);

        try {
            var result = command.getResultFuture().get();

            if (result instanceof RegisterUserResults.Success success) {
                return new RegisterUserResultsDTO.Success(UserMapper.toUserDTO(success.user()), success.tracingId());
            } else if (result instanceof RegisterUserResults.UsernameExists usernameExists) {
                return new RegisterUserResultsDTO.UsernameExists(usernameExists.username(), usernameExists.tracingId());
            } else if (result instanceof RegisterUserResults.EmailExists emailExists) {
                return new RegisterUserResultsDTO.EmailExists(emailExists.email(), emailExists.tracingId());
            } else if (result instanceof RegisterUserResults.InvalidEmail invalidEmail) {
                return new RegisterUserResultsDTO.InvalidEmail(invalidEmail.email(), invalidEmail.tracingId());
            } else if (result instanceof RegisterUserResults.InvalidPasswordLength invalidPasswordLength) {
                return new RegisterUserResultsDTO.InvalidPasswordLength(invalidPasswordLength.password(), invalidPasswordLength.tracingId());
            } else if (result instanceof RegisterUserResults.SystemError systemError) {
                return new RegisterUserResultsDTO.SystemError(systemError.message(), systemError.tracingId());
            } else {
                throw new IllegalStateException("Unexpected result type: " + result.getClass().getName());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new RegisterUserResultsDTO.SystemError("Failed to register user: " + e.getMessage(), internalRequest.tracingId());
        }
    }

}