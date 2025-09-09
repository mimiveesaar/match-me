package tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class DeclineConnectionRequestEndpoint {
    private final DeclineConnectionCommandHandler declineConnectionCommandHandler;

    public DeclineConnectionRequestEndpoint(DeclineConnectionCommandHandler declineConnectionCommandHandler) {
        this.declineConnectionCommandHandler = declineConnectionCommandHandler;
    }

    @PostMapping("/decline-request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection request declined successfully.", content = @Content(mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.InvalidRequest.class))),
            @ApiResponse(responseCode = "404", description = "Connection request not found.", content = @Content(mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.NotFound.class))),
            @ApiResponse(responseCode = "409", description = "Connection request already declined.", content = @Content(mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.AlreadyDeclined.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.SystemError.class)))
    })
    public ResponseEntity<DeclineConnectionResults> declineConnectionRequest(DeclineConnectionRequest request) {
        var result = declineConnectionCommandHandler.handle(request);

        if (result instanceof DeclineConnectionResults.Success success) {
            return ResponseEntity.ok(success);
        } else if (result instanceof DeclineConnectionResults.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(invalidRequest);
        } else if (result instanceof DeclineConnectionResults.NotFound notFound) {
            return ResponseEntity.status(404).body(notFound);
        } else if (result instanceof DeclineConnectionResults.AlreadyDeclined alreadyDeclined) {
            return ResponseEntity.status(409).body(alreadyDeclined);
        } else if (result instanceof DeclineConnectionResults.SystemError systemError) {
            return ResponseEntity.status(500).body(systemError);
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500).body(new DeclineConnectionResults.SystemError("Unknown error"));
        }
    }
}
