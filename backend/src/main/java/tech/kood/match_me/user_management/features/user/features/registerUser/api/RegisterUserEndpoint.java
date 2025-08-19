package tech.kood.match_me.user_management.features.user.features.registerUser.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;

@RestController
@RequestMapping("/api/v1/user-management/auth")
@Tag(name = "User Management")
public class RegisterUserEndpoint {


    private final RegisterUserCommandHandler registerUserHandler;
    public RegisterUserEndpoint(RegisterUserCommandHandler registerUserHandler) {
        this.registerUserHandler = registerUserHandler;
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RegisterUserResults.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    oneOf = {
                                            RegisterUserResults.EmailExists.class,
                                            RegisterUserResults.InvalidRequest.class
                                    }))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResults.SystemError.class)))})

    public ResponseEntity<RegisterUserResults> registerUser(@RequestBody RegisterUserRequest request) {

        if (request.tracingId() == null || request.tracingId().isEmpty()) {
           request = request.withTracingId(UUID.randomUUID().toString());
        }

        try {
            var result = registerUserHandler.handle(request);

            if (result instanceof RegisterUserResults.Success success) {
                return ResponseEntity.ok(success);
            } else if (result instanceof RegisterUserResults.InvalidRequest invalidRequest) {
                return ResponseEntity.status(400).body(invalidRequest);
            } else if (result instanceof RegisterUserResults.EmailExists emailExists) {
                return ResponseEntity.status(400).body(emailExists);
            } else if (result instanceof RegisterUserResults.SystemError systemError) {
                return ResponseEntity.status(500).body(systemError);
            }

        } catch (CheckedConstraintViolationException e) {
            //This should never happen.
            return ResponseEntity.internalServerError().body(new RegisterUserResults.SystemError(request.requestId(),
                    e.getMessage(), request.tracingId()));
        }

        //This should never happen.
        return ResponseEntity.internalServerError().body(new RegisterUserResults.SystemError(request.requestId(),
                "Internal server error", request.tracingId()));
    }
}
