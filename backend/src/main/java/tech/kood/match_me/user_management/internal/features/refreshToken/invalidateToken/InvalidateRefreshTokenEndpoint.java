package tech.kood.match_me.user_management.internal.features.refreshToken.invalidateToken;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import tech.kood.match_me.user_management.api.DTOs.InvalidateRefreshTokenRequestDTO;
import tech.kood.match_me.user_management.api.DTOs.InvalidateRefreshTokenResultsDTO;
import tech.kood.match_me.user_management.internal.mappers.InvalidateRefreshTokenMapper;

@RestController
@RequestMapping("/api/user-management")
@Tag(name = "User Management")
public final class InvalidateRefreshTokenEndpoint {

    private final InvalidateRefreshTokenHandler invalidateRefreshTokenHandler;

    public InvalidateRefreshTokenEndpoint(
            InvalidateRefreshTokenHandler invalidateRefreshTokenHandler) {
        this.invalidateRefreshTokenHandler = invalidateRefreshTokenHandler;
    }

    @PostMapping("/invalidate")
    @ApiResponses(value = {@io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Refresh token invalidated successfully.",
            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = InvalidateRefreshTokenResultsDTO.Success.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                    description = "Refresh token not found.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = InvalidateRefreshTokenResultsDTO.TokenNotFound.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Invalid request.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = InvalidateRefreshTokenResultsDTO.InvalidRequest.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                    description = "Internal server error.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = InvalidateRefreshTokenResultsDTO.SystemError.class)))})

    public ResponseEntity<InvalidateRefreshTokenResultsDTO> invalidateRefreshToken(
            @RequestBody InvalidateRefreshTokenRequestDTO request) {

        var internalRequest = new InvalidateRefreshTokenRequest(UUID.randomUUID(),
                request.refreshToken(), Optional.of(UUID.randomUUID().toString()));

        var result = invalidateRefreshTokenHandler.handle(internalRequest);
        var responseDTO = new InvalidateRefreshTokenMapper().toDTO(result);

        return switch (responseDTO) {
            case InvalidateRefreshTokenResultsDTO.Success success -> ResponseEntity.ok(success);
            case InvalidateRefreshTokenResultsDTO.TokenNotFound tokenNotFound -> ResponseEntity
                    .status(401).body(tokenNotFound);
            case InvalidateRefreshTokenResultsDTO.InvalidRequest invalidRequest -> ResponseEntity
                    .status(400).body(invalidRequest);
            case InvalidateRefreshTokenResultsDTO.SystemError systemError -> ResponseEntity
                    .status(500).body(systemError);
        };
    }
}
