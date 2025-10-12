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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.AcceptConnectionRequest;
import tech.kood.match_me.user_management.features.user.domain.UserDTO;


@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class AcceptConnectionsRequestController {

    private final AcceptConnectionRequest.Handler acceptConnectionCommandHandler;

    public AcceptConnectionsRequestController(AcceptConnectionRequest.Handler acceptConnectionCommandHandler) {
        this.acceptConnectionCommandHandler = acceptConnectionCommandHandler;
    }

    public sealed interface Response permits Response.Success,
            Response.NotFound, Response.InvalidRequest,
            Response.AlreadyAccepted, Response.SystemError {

        record Success() implements Response {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
                implements Response {
        }

        record NotFound() implements Response {
        }

        record AlreadyAccepted() implements Response {
        }

        record SystemError(@NotEmpty String message) implements Response {
        }
    }

    public record AcceptConnectionDTO(
            @NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO
    ) {}

    @PostMapping("/accept-request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Connection request accepted successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = Response.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            discriminatorProperty = "type",
                            implementation = Response.InvalidRequest.class))),
            @ApiResponse(responseCode = "404", description = "Connection request not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = Response.NotFound.class))),
            @ApiResponse(responseCode = "409", description = "Connection request already accepted.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = Response.AlreadyAccepted.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = Response.SystemError.class)))})

    public ResponseEntity<Response> acceptConnectionRequest(AcceptConnectionDTO request) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var internalRequest = new AcceptConnectionRequest.Request(request.connectionIdDTO(), userId);
        var result = acceptConnectionCommandHandler.handle(internalRequest);

        if (result instanceof AcceptConnectionRequest.Result.Success success) {
            return ResponseEntity.ok(new Response.Success());
        } else if (result instanceof AcceptConnectionRequest.Result.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(new Response.InvalidRequest(invalidRequest.error()));
        } else if (result instanceof AcceptConnectionRequest.Result.NotFound notFound) {
            return ResponseEntity.status(404).body(new Response.NotFound());
        } else if (result instanceof AcceptConnectionRequest.Result.AlreadyAccepted alreadyAccepted) {
            return ResponseEntity.status(409).body(new Response.AlreadyAccepted());
        } else if (result instanceof AcceptConnectionRequest.Result.SystemError systemError) {
            return ResponseEntity.status(500).body(new Response.SystemError(systemError.message()));
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500)
                    .body(new Response.SystemError("Unknown error"));
        }
    }
}