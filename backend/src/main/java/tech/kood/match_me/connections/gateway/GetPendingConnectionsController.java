package tech.kood.match_me.connections.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.GetPendingConnections;
import tech.kood.match_me.connections.features.pendingConnection.domain.PendingConnectionDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class GetPendingConnectionsController {

    private final GetPendingConnections.Handler handler;

    public GetPendingConnectionsController(GetPendingConnections.Handler handler) {
        this.handler = handler;
    }

    public sealed interface Response permits
            Response.Success,
            Response.InvalidRequest,
            Response.SystemError {

        record Success(
                @NotNull @JsonProperty("incoming_requests") List<PendingConnectionDTO> incomingRequests,
                @NotNull @JsonProperty("outgoing_requests") List<PendingConnectionDTO> outgoingRequests
        ) implements Response {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error) implements Response {
        }

        record SystemError(@NotEmpty String message) implements Response {
        }
    }

    record RequestDTO() {
    }

    @GetMapping("/pending-requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pending connection requests retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.class)))
    })
    public ResponseEntity<Response> getPendingConnections() {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var getPendingConnectionsRequest = new GetPendingConnections.Request(userId);
        var result = handler.handle(getPendingConnectionsRequest);

        if (result instanceof GetPendingConnections.Result.Success success) {
            return ResponseEntity.ok(new Response.Success(success.incomingRequests(), success.outgoingRequests()));
        } else if (result instanceof GetPendingConnections.Result.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(new Response.InvalidRequest(invalidRequest.error()));
        } else if (result instanceof GetPendingConnections.Result.SystemError systemError) {
            return ResponseEntity.status(500).body(new Response.SystemError(systemError.message()));
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500).body(new Response.SystemError("Unknown error"));
        }
    }
}