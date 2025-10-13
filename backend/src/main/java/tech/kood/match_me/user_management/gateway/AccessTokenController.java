package tech.kood.match_me.user_management.gateway;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.user_management.features.accessToken.actions.CreateAccessToken;

@RestController
@RequestMapping("/api/v1/user-management")
@Tag(name = "User Management", description = "API for managing user information")
public class AccessTokenController {

    private final CreateAccessToken.Handler createAccessTokenCommandHandler;

    public AccessTokenController(CreateAccessToken.Handler createAccessTokenCommandHandler) {
        this.createAccessTokenCommandHandler = createAccessTokenCommandHandler;
    }

    @PostMapping("/access-token")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Access token retrieved successfully.",
            content = @Content(mediaType = "application/json", schema = @Schema(
                    implementation = CreateAccessToken.Result.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = CreateAccessToken.Result.InvalidRequest.class))),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CreateAccessToken.Result.InvalidToken.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CreateAccessToken.Result.SystemError.class)))})
    public ResponseEntity<CreateAccessToken.Result> getAccessToken(@RequestBody CreateAccessToken.Request request) {

        var result = createAccessTokenCommandHandler.handle(request);
        return switch (result) {
            case CreateAccessToken.Result.Success success -> ResponseEntity.ok(success);
            case CreateAccessToken.Result.InvalidToken invalidToken -> ResponseEntity.status(401).body(invalidToken);
            case CreateAccessToken.Result.InvalidRequest invalidRequest ->
                    ResponseEntity.status(400).body(invalidRequest);
            case CreateAccessToken.Result.SystemError systemError -> ResponseEntity.status(500).body(systemError);
            case null -> ResponseEntity.status(500).body(new CreateAccessToken.Result.SystemError("Unexpected error occurred"));
        };
    }
}