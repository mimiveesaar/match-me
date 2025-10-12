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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.DeclineConnectionRequest;
import tech.kood.match_me.user_management.features.user.domain.UserDTO;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class DeclineConnectionRequestController {

    private final DeclineConnectionRequest.Handler declineConnectionRequestHandler;

    public DeclineConnectionRequestController(DeclineConnectionRequest.Handler declineConnectionRequestHandler) {
        this.declineConnectionRequestHandler = declineConnectionRequestHandler;
    }

    public sealed interface Response permits
            Response.Success, Response.NotFound, Response.InvalidRequest,
            Response.AlreadyDeclined, Response.SystemError {

        record Success() implements Response {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error) implements Response {
        }

        record NotFound() implements Response {
        }

        record AlreadyDeclined() implements Response {
        }

        record SystemError(@NotEmpty String message) implements Response {
        }
    }

    public record DeclineConnectionDTO(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionId) {}

    @PostMapping("/decline-request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection request declined successfully.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionRequest.Result.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionRequest.Result.InvalidRequest.class))),
            @ApiResponse(responseCode = "404", description = "Connection request not found.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation = DeclineConnectionRequest.Result.NotFound.class))),
            @ApiResponse(responseCode = "409", description = "Connection request already declined.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation =  DeclineConnectionRequest.Result.AlreadyDeclined.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json",
                    schema = @Schema(discriminatorProperty = "type",
                            implementation =  DeclineConnectionRequest.Result.SystemError.class)))
    })
    public ResponseEntity<Response> declineConnectionRequest(@RequestBody @Validated DeclineConnectionDTO declineConnection) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var request = new DeclineConnectionRequest.Request(declineConnection.connectionId, userId);
        var result = declineConnectionRequestHandler.handle(request);

        if (result instanceof DeclineConnectionRequest.Result.Success success) {
            return ResponseEntity.ok(new Response.Success());
        } else if (result instanceof DeclineConnectionRequest.Result.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(new Response.InvalidRequest(invalidRequest.error()));
        } else if (result instanceof DeclineConnectionRequest.Result.NotFound notFound) {
            return ResponseEntity.status(404).body(new Response.NotFound());
        } else if (result instanceof DeclineConnectionRequest.Result.AlreadyDeclined alreadyDeclined) {
            return ResponseEntity.status(409).body(new Response.AlreadyDeclined());
        } else if (result instanceof DeclineConnectionRequest.Result.SystemError systemError) {
            return ResponseEntity.status(500).body(new Response.SystemError(systemError.message()));
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500).body(new Response.SystemError("Unknown error"));
        }
    }
}