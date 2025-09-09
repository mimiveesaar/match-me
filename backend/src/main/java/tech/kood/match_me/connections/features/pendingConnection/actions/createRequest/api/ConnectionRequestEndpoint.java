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
            @ApiResponse(responseCode = "200", description = "Connection request created successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = ConnectionRequestResults.Success.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = ConnectionRequestResults.InvalidRequest.class))),

            @ApiResponse(responseCode = "409", description = "Connection already exists.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = ConnectionRequestResults.AlreadyExists.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = ConnectionRequestResults.SystemError.class)))})

    public ResponseEntity<ConnectionRequestResults> createConnectionRequest(@RequestBody ConnectionRequest request) {

        var result = connectionRequestCommandHandler.handle(request);

        if (result instanceof ConnectionRequestResults.Success success) {
            return ResponseEntity.ok(success);
        } else if (result instanceof ConnectionRequestResults.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(invalidRequest);
        } else if (result instanceof ConnectionRequestResults.AlreadyExists alreadyExists) {
            return ResponseEntity.status(409).body(alreadyExists);
        } else if (result instanceof ConnectionRequestResults.SystemError systemError) {
            return ResponseEntity.status(500).body(systemError);
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500)
                    .body(new ConnectionRequestResults.SystemError("Unexpected error occurred"));
        }
    }
}
