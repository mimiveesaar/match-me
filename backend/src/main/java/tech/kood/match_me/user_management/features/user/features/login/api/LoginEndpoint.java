package tech.kood.match_me.user_management.features.user.features.login.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/user-management/auth")
@Tag(name = "User Management", description = "API for user management operations")
public class LoginEndpoint {

    private final LoginCommandHandler loginCommandHandler;

    public LoginEndpoint(LoginCommandHandler loginCommandHandler) {
        this.loginCommandHandler = loginCommandHandler;
    }

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginResults.Success.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginResults.InvalidRequest.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginResults.InvalidCredentials.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginResults.SystemError.class)))})

    public ResponseEntity<LoginResults> loginUser(@RequestBody LoginRequest request) {

        String tracingId = UUID.randomUUID().toString();
        if (request.tracingId() != null && !request.tracingId().isEmpty()) {
            tracingId = request.tracingId();
        }

        var internalRequest = new LoginRequest(
                request.email(),
                request.password(),
                tracingId);

        var loginResult = loginCommandHandler.handle(internalRequest);

        if (loginResult instanceof LoginResults.Success success) {
            return ResponseEntity.ok(success);
        } else if (loginResult instanceof LoginResults.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(invalidRequest);
        } else if (loginResult instanceof LoginResults.InvalidCredentials invalidCredentials) {
            return ResponseEntity.status(401).body(invalidCredentials);
        } else if (loginResult instanceof LoginResults.SystemError systemError) {
            return ResponseEntity.status(500).body(systemError);
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500)
                    .body(new LoginResults.SystemError(request.requestId(), "Unexpected error occurred", request.tracingId()));
        }
    }
}
