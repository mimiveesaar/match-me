package tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-management/auth")
@Tag(name = "User Management")
public final class InvalidateRefreshTokenEndpoint {

    private final InvalidateRefreshTokenCommandHandler invalidateRefreshTokenCommandHandler;

    public InvalidateRefreshTokenEndpoint(InvalidateRefreshTokenCommandHandler invalidateRefreshTokenCommandHandler) {
        this.invalidateRefreshTokenCommandHandler = invalidateRefreshTokenCommandHandler;
    }

    @PostMapping("/invalidate")
    @ApiResponses(value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Refresh token invalidated successfully.",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                            discriminatorProperty = "type",
                            implementation = InvalidateRefreshTokenResults.Success.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                    description = "Refresh token not found.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = InvalidateRefreshTokenResults.TokenNotFound.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Invalid request.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = InvalidateRefreshTokenResults.InvalidRequest.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                    description = "Internal server error.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    discriminatorProperty = "type",
                                    implementation = InvalidateRefreshTokenResults.SystemError.class)))})

    public ResponseEntity<InvalidateRefreshTokenResults> invalidateRefreshToken(
            @Valid @RequestBody InvalidateRefreshTokenRequest request) {


        String tracingId = UUID.randomUUID().toString();
        if (request.tracingId() != null && !request.tracingId().isEmpty()) {
            tracingId = request.tracingId();
        }

        var internalRequest = new InvalidateRefreshTokenRequest(request.secret(), tracingId);
        var result = invalidateRefreshTokenCommandHandler.handle(internalRequest);

        return switch (result) {
            case InvalidateRefreshTokenResults.Success success -> ResponseEntity.ok(success);
            case InvalidateRefreshTokenResults.TokenNotFound tokenNotFound ->
                    ResponseEntity.status(401).body(tokenNotFound);
            case InvalidateRefreshTokenResults.InvalidRequest invalidRequest ->
                    ResponseEntity.status(400).body(invalidRequest);
            case InvalidateRefreshTokenResults.SystemError systemError -> ResponseEntity.status(500).body(systemError);
            case null ->
                    ResponseEntity.status(500).body(new InvalidateRefreshTokenResults.SystemError(request.requestId(), "Unexpected error occurred", tracingId));
        };
    }
}