package tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.internal;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionResults;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class RejectAcceptedConnectionEndpoint {

    private final RejectAcceptedConnectionCommandHandler rejectAcceptedConnectionCommandHandler;

    public RejectAcceptedConnectionEndpoint(
            RejectAcceptedConnectionCommandHandler rejectAcceptedConnectionCommandHandler) {
        this.rejectAcceptedConnectionCommandHandler = rejectAcceptedConnectionCommandHandler;
    }

    @PostMapping("/reject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Accepted connection rejected successfully.",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = RejectAcceptedConnectionResults.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                            discriminatorProperty = "type",
                            implementation = RejectAcceptedConnectionResults.InvalidRequest.class))),
            @ApiResponse(responseCode = "404", description = "Accepted connection not found.",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = RejectAcceptedConnectionResults.NotFound.class))),
            @ApiResponse(responseCode = "409", description = "Accepted connection already rejected.",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = RejectAcceptedConnectionResults.AlreadyRejected.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = RejectAcceptedConnectionResults.SystemError.class)))
    })
    public ResponseEntity<RejectAcceptedConnectionResults> rejectAcceptedConnection(
            RejectAcceptedConnectionRequest request) {
        var result = rejectAcceptedConnectionCommandHandler.handle(request);

        if (result instanceof RejectAcceptedConnectionResults.Success success) {
            return ResponseEntity.ok(success);
        } else if (result instanceof RejectAcceptedConnectionResults.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(invalidRequest);
        } else if (result instanceof RejectAcceptedConnectionResults.NotFound notFound) {
            return ResponseEntity.status(404).body(notFound);
        } else if (result instanceof RejectAcceptedConnectionResults.AlreadyRejected alreadyRejected) {
            return ResponseEntity.status(409).body(alreadyRejected);
        } else if (result instanceof RejectAcceptedConnectionResults.SystemError systemError) {
            return ResponseEntity.status(500).body(systemError);
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}
