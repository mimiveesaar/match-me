package tech.kood.match_me.user_management.gateway.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.actions.InvalidateRefreshToken;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;

@RestController
@RequestMapping("/api/v1/user-management")
@Tag(name = "User Management", description = "API for managing user information")
public class InvalidateRefreshTokenController {

    private final InvalidateRefreshToken.Handler invalidateRefreshTokenHandler;

    public InvalidateRefreshTokenController(InvalidateRefreshToken.Handler invalidateRefreshTokenHandler) {
        this.invalidateRefreshTokenHandler = invalidateRefreshTokenHandler;
    }

    public record InvalidateRefreshTokenDTO(@NotNull @Valid @JsonProperty("secret") RefreshTokenSecretDTO secret) {}


    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = InvalidateRefreshTokenResponseDTO.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = InvalidateRefreshTokenResponseDTO.TokenNotFound.class, name = "TOKEN_NOT_FOUND"),
            @JsonSubTypes.Type(value = InvalidateRefreshTokenResponseDTO.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = InvalidateRefreshTokenResponseDTO.SystemError.class, name = "SYSTEM_ERROR")
    })
    public sealed interface InvalidateRefreshTokenResponseDTO permits
            InvalidateRefreshTokenResponseDTO.Success,
            InvalidateRefreshTokenResponseDTO.TokenNotFound,
            InvalidateRefreshTokenResponseDTO.InvalidRequest,
            InvalidateRefreshTokenResponseDTO.SystemError {

        record Success() implements InvalidateRefreshTokenResponseDTO {
        }
        record TokenNotFound() implements InvalidateRefreshTokenResponseDTO {
        }
        record InvalidRequest(@NotEmpty @JsonProperty("data") InvalidInputErrorDTO error) implements InvalidateRefreshTokenResponseDTO {
        }
        record SystemError(@NotEmpty @JsonProperty("message") String message) implements InvalidateRefreshTokenResponseDTO {
        }
    }

    @PostMapping("/invalidate")
    @ApiResponses(value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Refresh token invalidated successfully.",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                            discriminatorProperty = "type",
                            implementation = InvalidateRefreshToken.Result.Success.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                    description = "Refresh token not found.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = InvalidateRefreshToken.Result.TokenNotFound.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Invalid request.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = InvalidateRefreshToken.Result.InvalidRequest.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                    description = "Internal server error.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = InvalidateRefreshToken.Result.SystemError.class)))})

    public ResponseEntity<InvalidateRefreshTokenResponseDTO> invalidateRefreshToken(@RequestBody @Valid InvalidateRefreshTokenDTO request) {

        var internalRequest = new InvalidateRefreshToken.Request(request.secret());
        var result = invalidateRefreshTokenHandler.handle(internalRequest);

        return switch (result) {
            case InvalidateRefreshToken.Result.Success r -> ResponseEntity.ok(new InvalidateRefreshTokenResponseDTO.Success());
            case InvalidateRefreshToken.Result.TokenNotFound r -> ResponseEntity.status(401).body(new InvalidateRefreshTokenResponseDTO.TokenNotFound());
            case InvalidateRefreshToken.Result.InvalidRequest r -> ResponseEntity.badRequest().body(new InvalidateRefreshTokenResponseDTO.InvalidRequest(r.error()));
            case InvalidateRefreshToken.Result.SystemError r -> ResponseEntity.status(500).body(new InvalidateRefreshTokenResponseDTO.SystemError(r.message()));
        };
    }
}