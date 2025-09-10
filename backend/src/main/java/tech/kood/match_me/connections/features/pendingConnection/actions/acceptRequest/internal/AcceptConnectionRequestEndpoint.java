package tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.internal;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionCommandHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionResults;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class AcceptConnectionRequestEndpoint {
    private final AcceptConnectionCommandHandler acceptConnectionCommandHandler;

    public AcceptConnectionRequestEndpoint(
            AcceptConnectionCommandHandler acceptConnectionCommandHandler) {
        this.acceptConnectionCommandHandler = acceptConnectionCommandHandler;
    }

    @PostMapping("/accept-request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Connection request accepted successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = AcceptConnectionResults.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                            discriminatorProperty = "type",
                            implementation = AcceptConnectionResults.InvalidRequest.class))),
            @ApiResponse(responseCode = "404", description = "Connection request not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = AcceptConnectionResults.NotFound.class))),
            @ApiResponse(responseCode = "409", description = "Connection request already accepted.",
                    content = @Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = AcceptConnectionResults.AlreadyAccepted.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = AcceptConnectionResults.SystemError.class)))})
    public ResponseEntity<AcceptConnectionResults> acceptConnectionRequest(
            AcceptConnectionRequest request) {
        var result = acceptConnectionCommandHandler.handle(request);

        if (result instanceof AcceptConnectionResults.Success success) {
            return ResponseEntity.ok(success);
        } else if (result instanceof AcceptConnectionResults.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(invalidRequest);
        } else if (result instanceof AcceptConnectionResults.NotFound notFound) {
            return ResponseEntity.status(404).body(notFound);
        } else if (result instanceof AcceptConnectionResults.AlreadyAccepted alreadyAccepted) {
            return ResponseEntity.status(409).body(alreadyAccepted);
        } else if (result instanceof AcceptConnectionResults.SystemError systemError) {
            return ResponseEntity.status(500).body(systemError);
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500)
                    .body(new AcceptConnectionResults.SystemError("Unknown error"));
        }
    }
}
