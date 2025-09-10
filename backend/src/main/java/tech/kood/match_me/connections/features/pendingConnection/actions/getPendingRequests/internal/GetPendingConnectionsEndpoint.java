package tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.internal;


import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsQueryHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsResults;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class GetPendingConnectionsEndpoint {

    private final GetPendingConnectionsQueryHandler getPendingConnectionsQueryHandler;

    public GetPendingConnectionsEndpoint(GetPendingConnectionsQueryHandler getPendingConnectionsQueryHandler) {
        this.getPendingConnectionsQueryHandler = getPendingConnectionsQueryHandler;
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
    public ResponseEntity<GetPendingConnectionsResults> getPendingConnections(GetPendingConnectionsRequest getPendingConnectionsRequest) {
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
}
