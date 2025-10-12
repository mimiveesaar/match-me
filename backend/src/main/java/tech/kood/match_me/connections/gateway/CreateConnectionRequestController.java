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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.CreateConnectionRequest;
import tech.kood.match_me.user_management.features.user.domain.UserDTO;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections", description = "API for managing user connections")
public class CreateConnectionRequestController {

    private final CreateConnectionRequest.Handler connectionRequestHandler;

    public CreateConnectionRequestController(CreateConnectionRequest.Handler connectionRequestHandler) {
        this.connectionRequestHandler = connectionRequestHandler;
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Response.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = Response.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = Response.AlreadyExists.class, name = "ALREADY_EXISTS"),
            @JsonSubTypes.Type(value = Response.SystemError.class, name = "SYSTEM_ERROR"),
    })
    public sealed interface Response permits
            Response.Success,
            Response.InvalidRequest,
            Response.AlreadyExists,
            Response.SystemError {

        record Success(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO) implements Response {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error) implements Response {
        }

        record AlreadyExists() implements Response {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Response {
        }
    }

    public record ConnectionRequestDTO(@NotNull @JsonProperty("target_id") UserIdDTO targetId) {
    }


    @PostMapping("/request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection request created successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.Success.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.InvalidRequest.class))),

            @ApiResponse(responseCode = "409", description = "Connection already exists.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.AlreadyExists.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = Response.SystemError.class)))})


    public ResponseEntity<Response> createConnectionRequest(@RequestBody ConnectionRequestDTO request) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = ((UserDTO) principal).id();

        var internalRequest = new CreateConnectionRequest.Request (request.targetId(), userId);
        var result = connectionRequestHandler.handle(internalRequest);

        if (result instanceof CreateConnectionRequest.Result.Success success) {
            return ResponseEntity.ok(new Response.Success(success.connectionIdDTO()));
        } else if (result instanceof CreateConnectionRequest.Result.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(new Response.InvalidRequest(invalidRequest.error()));
        } else if (result instanceof CreateConnectionRequest.Result.AlreadyExists alreadyExists) {
            return ResponseEntity.status(409).body(new Response.AlreadyExists());
        } else if (result instanceof CreateConnectionRequest.Result.SystemError systemError) {
            return ResponseEntity.status(500).body(new Response.SystemError(systemError.message()));
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500).body(new Response.SystemError("Unexpected error occurred"));
        }
    }
}