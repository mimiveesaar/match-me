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
import tech.kood.match_me.connections.features.acceptedConnection.actions.RejectAcceptedConnection;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class RejectAcceptedConnectionController {

    private final RejectAcceptedConnection.Handler rejectAcceptedConnectionCommandHandler;

    public RejectAcceptedConnectionController(RejectAcceptedConnection.Handler rejectAcceptedConnectionCommandHandler) {
        this.rejectAcceptedConnectionCommandHandler = rejectAcceptedConnectionCommandHandler;
    }

    public sealed interface Response permits
            Response.Success,
            Response.NotFound,
            Response.AlreadyRejected,
            Response.InvalidRequest,
            Response.SystemError {

        record Success()
                implements Response {
        }

        record NotFound()
                implements Response {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
                implements Response {
        }

        record AlreadyRejected()
                implements Response {
        }

        record SystemError(@NotEmpty String message)
                implements Response {
        }
    }

    public record RejectConnectionDTO(
            @NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionId
    ) {}

    @PostMapping("/reject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Accepted connection rejected successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = Response.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            discriminatorProperty = "type",
                            implementation = Response.InvalidRequest.class))),
            @ApiResponse(responseCode = "404", description = "Accepted connection not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = Response.NotFound.class))),
            @ApiResponse(responseCode = "409", description = "Accepted connection already rejected.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = Response.AlreadyRejected.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    discriminatorProperty = "type",
                                    implementation = Response.SystemError.class)))
    })

    public ResponseEntity<Response> rejectAcceptedConnection(RejectConnectionDTO rejectConnection) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var request = new RejectAcceptedConnection.Request(rejectConnection.connectionId(), userId);
        var result = rejectAcceptedConnectionCommandHandler.handle(request);

        if (result instanceof RejectAcceptedConnection.Result.Success success) {
            return ResponseEntity.ok(new Response.Success());
        } else if (result instanceof RejectAcceptedConnection.Result.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(new Response.InvalidRequest(invalidRequest.error()));
        } else if (result instanceof RejectAcceptedConnection.Result.NotFound notFound) {
            return ResponseEntity.status(404).body(new Response.NotFound());
        } else if (result instanceof RejectAcceptedConnection.Result.AlreadyRejected alreadyRejected) {
            return ResponseEntity.status(409).body(new Response.AlreadyRejected());
        } else if (result instanceof  RejectAcceptedConnection.Result.SystemError systemError) {
            return ResponseEntity.status(500).body(new Response.SystemError(systemError.message()));
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}