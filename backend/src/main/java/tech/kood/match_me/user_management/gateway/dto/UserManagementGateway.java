package tech.kood.match_me.user_management.gateway.dto;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserRequest;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserResults;

@RestController
@RequestMapping("/api/v1/user-management")
@Tag(name = "User Management", description = "API for managing user information")
public class UserManagementGateway {

    private final RegisterUserCommandHandler registerUserHandler;

    public UserManagementGateway(RegisterUserCommandHandler registerUserHandler) {
        this.registerUserHandler = registerUserHandler;
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RegisterUserResults.Success.class))),

            @ApiResponse(responseCode = "409", description = "Email already exists.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResults.EmailExists.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =  RegisterUserResults.InvalidRequest.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResults.SystemError.class)))}
    )

    public ResponseEntity<RegisterUserResults> registerUser(@RequestBody RegisterDTO request) {

        var internalRequest = new RegisterUserRequest(request.email(), request.password());

        try {
            var result = registerUserHandler.handle(internalRequest);

            if (result instanceof RegisterUserResults.Success success) {
                return ResponseEntity.ok(success);
            } else if (result instanceof RegisterUserResults.InvalidRequest invalidRequest) {
                return ResponseEntity.status(400).body(invalidRequest);
            } else if (result instanceof RegisterUserResults.EmailExists emailExists) {
                return ResponseEntity.status(409).body(emailExists);
            } else if (result instanceof RegisterUserResults.SystemError systemError) {
                return ResponseEntity.status(500).body(systemError);
            }

        } catch (CheckedConstraintViolationException e) {
            return ResponseEntity.internalServerError().body(new RegisterUserResults.SystemError(e.getMessage()));
        }

        //This should never happen.
        return ResponseEntity.internalServerError().body(new RegisterUserResults.SystemError("Internal server error"));
    }
}
