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
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsQueryHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsResults;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionResults;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionCommandHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionResults;
import tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api.ConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api.ConnectionRequestCommandHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api.ConnectionRequestResults;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.DeclineConnectionCommandHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.DeclineConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.DeclineConnectionResults;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsQueryHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsResults;
import tech.kood.match_me.connections.gateway.dto.*;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;


@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class ConnectionsGateway {


    private final GetPendingConnectionsQueryHandler getPendingConnectionsQueryHandler;
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


    @GetMapping("/pending-requests")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pending connection requests retrieved successfully.",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type",
                                    implementation = GetPendingConnectionsResults.Success.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type",
                                    implementation = GetPendingConnectionsResults.InvalidRequest.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type",
                                    implementation = GetPendingConnectionsResults.SystemError.class)))
    })
    public ResponseEntity<GetPendingConnectionsResults> getPendingConnections(GetPendingConnectionsDTO getPendingConnections) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var getPendingConnectionsRequest = new GetPendingConnectionsRequest(userId);
        var result = getPendingConnectionsQueryHandler.handle(getPendingConnectionsRequest);

        if (result instanceof GetPendingConnectionsResults.Success success) {
            return ResponseEntity.ok(success);
        } else if (result instanceof GetPendingConnectionsResults.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(invalidRequest);
        } else if (result instanceof GetPendingConnectionsResults.SystemError systemError) {
            return ResponseEntity.status(500).body(systemError);
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500).body(new GetPendingConnectionsResults.SystemError("Unknown error"));
        }
    }

    @GetMapping("/")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted connections retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = GetAcceptedConnectionsResults.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = GetAcceptedConnectionsResults.InvalidRequest.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = GetAcceptedConnectionsResults.SystemError.class)))
    })
    public ResponseEntity<GetAcceptedConnectionsResults> getAcceptedConnections() {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var request = new GetAcceptedConnectionsRequest(userId);
        var result = getAcceptedConnectionsQueryHandler.handle(request);

        if (result instanceof GetAcceptedConnectionsResults.Success success) {
            return ResponseEntity.ok(success);
        } else if (result instanceof GetAcceptedConnectionsResults.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(invalidRequest);
        } else if (result instanceof GetAcceptedConnectionsResults.SystemError systemError) {
            return ResponseEntity.status(500).body(systemError);
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500).body(new GetAcceptedConnectionsResults.SystemError("Unknown error"));
        }
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
    public ResponseEntity<AcceptConnectionResults> acceptConnectionRequest(AcceptConnectionDTO request) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var internalRequest = new AcceptConnectionRequest(request.connectionIdDTO(), userId);
        var result = acceptConnectionCommandHandler.handle(internalRequest);

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
    }
}