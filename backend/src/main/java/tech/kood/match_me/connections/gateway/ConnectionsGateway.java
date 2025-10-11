package tech.kood.match_me.connections.gateway;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;


@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class ConnectionsGateway {


    /*private final GetPendingConnectionsQueryHandler getPendingConnectionsQueryHandler;
    private final GetAcceptedConnectionsQueryHandler getAcceptedConnectionsQueryHandler;
    private final AcceptConnectionCommandHandler acceptConnectionCommandHandler;
    private final ConnectionRequestCommandHandler connectionRequestCommandHandler;
    private final RejectAcceptedConnectionCommandHandler rejectAcceptedConnectionCommandHandler;
    private final DeclineConnectionCommandHandler declineConnectionCommandHandler;

    public ConnectionsGateway(GetPendingConnectionsQueryHandler getPendingConnectionsQueryHandler,
                              GetAcceptedConnectionsQueryHandler getAcceptedConnectionsQueryHandler,
                              AcceptConnectionCommandHandler acceptConnectionCommandHandler,
                              ConnectionRequestCommandHandler connectionRequestCommandHandler, RejectAcceptedConnectionCommandHandler rejectAcceptedConnectionCommandHandler, DeclineConnectionCommandHandler declineConnectionCommandHandler) {
        this.getPendingConnectionsQueryHandler = getPendingConnectionsQueryHandler;
        this.getAcceptedConnectionsQueryHandler = getAcceptedConnectionsQueryHandler;
        this.acceptConnectionCommandHandler = acceptConnectionCommandHandler;
        this.connectionRequestCommandHandler = connectionRequestCommandHandler;
        this.rejectAcceptedConnectionCommandHandler = rejectAcceptedConnectionCommandHandler;
        this.declineConnectionCommandHandler = declineConnectionCommandHandler;
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

    public ResponseEntity<ConnectionRequestResults> createConnectionRequest(@RequestBody ConnectionRequestDTO request) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var internalRequest = new ConnectionRequest(request.targetId(), userId);
        var result = connectionRequestCommandHandler.handle(internalRequest);

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

    @PostMapping("/reject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Accepted connection rejected successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = RejectAcceptedConnectionResults.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            discriminatorProperty = "type",
                            implementation = RejectAcceptedConnectionResults.InvalidRequest.class))),
            @ApiResponse(responseCode = "404", description = "Accepted connection not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = RejectAcceptedConnectionResults.NotFound.class))),
            @ApiResponse(responseCode = "409", description = "Accepted connection already rejected.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = RejectAcceptedConnectionResults.AlreadyRejected.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = RejectAcceptedConnectionResults.SystemError.class)))
    })
    public ResponseEntity<RejectAcceptedConnectionResults> rejectAcceptedConnection(
            RejectConnectionDTO rejectConnection) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var request = new RejectAcceptedConnectionRequest(rejectConnection.connectionId(), userId);
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

    @PostMapping("/decline-request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection request declined successfully.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.InvalidRequest.class))),
            @ApiResponse(responseCode = "404", description = "Connection request not found.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.NotFound.class))),
            @ApiResponse(responseCode = "409", description = "Connection request already declined.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.AlreadyDeclined.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionResults.SystemError.class)))
    })
    public ResponseEntity<DeclineConnectionResults> declineConnectionRequest(@Validated DeclineConnectionDTO declineConnection) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var request = new DeclineConnectionRequest(declineConnection.connectionId(), userId);
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
    }*/
}