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
import tech.kood.match_me.user_management.DTOs.UserDTO;
import tech.kood.match_me.user_management.internal.common.Command;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserRequest;
import tech.kood.match_me.user_management.internal.features.registerUser.RegisterUserResults;
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
    public RegisterUserResults registerUser(@RequestBody RegisterUserRequestDTO request) {
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
            return command.getResultFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new RegisterUserResults.SystemError("Failed to register user: " + e.getMessage(), internalRequest.tracingId());
        }

    }

}
