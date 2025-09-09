package tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api;

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
import tech.kood.match_me.user_management.features.user.actions.login.api.LoginRequest;
import tech.kood.match_me.user_management.features.user.actions.login.api.LoginResults;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class ConnectionRequestEndpoint {

    private final ConnectionRequestCommandHandler connectionRequestCommandHandler;

    public ConnectionRequestEndpoint(ConnectionRequestCommandHandler connectionRequestCommandHandler) {
        this.connectionRequestCommandHandler = connectionRequestCommandHandler;
    }

    @PostMapping("/request")
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

    public ResponseEntity<ConnectionRequestResults> loginUser(@RequestBody ConnectionRequest request) {

        String tracingId = UUID.randomUUID().toString();
        if (request.tracingId() != null && !request.tracingId().isEmpty()) {
            tracingId = request.tracingId();
        }


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
