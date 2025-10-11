package tech.kood.match_me.connections.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import tech.kood.match_me.connections.features.acceptedConnection.actions.GetAcceptedConnections;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class GetAcceptedConnectionsController {

    private GetAcceptedConnections.Handler getAcceptedConnectionsHandler;

    public GetAcceptedConnectionsController(GetAcceptedConnections.Handler getAcceptedConnectionsHandler) {
        this.getAcceptedConnectionsHandler = getAcceptedConnectionsHandler;
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Response.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = Response.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = Response.SystemError.class, name = "SYSTEM_ERROR"),
    })

    public sealed interface Response permits
            Response.Success,
            Response.InvalidRequest,
            Response.SystemError {

        record Success(
                @NotNull @JsonProperty("accepted_connections") List<AcceptedConnectionDTO> acceptedConnections) implements Response {
        }

        record InvalidRequest(
                @NotNull @JsonProperty("error") InvalidInputErrorDTO error) implements Response {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Response {
        }
    }

    record Request() {
    }

    @GetMapping("/accepted-connections")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted connections retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.InvalidRequest.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.SystemError.class)))
    })
    public ResponseEntity<Response> getAcceptedConnections() {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var request = new GetAcceptedConnections.Request(userId);
        var result = getAcceptedConnectionsHandler.handle(request);

        if (result instanceof GetAcceptedConnections.Result.Success success) {
            return ResponseEntity.ok(new Response.Success(success.acceptedConnections()));
        } else if (result instanceof GetAcceptedConnections.Result.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(new Response.InvalidRequest(invalidRequest.error()));
        } else if (result instanceof GetAcceptedConnections.Result.SystemError systemError) {
            return ResponseEntity.status(500).body(new Response.SystemError(systemError.message()));
        }

        return ResponseEntity.status(500).body(new Response.SystemError("Unknown error"));
    }
}