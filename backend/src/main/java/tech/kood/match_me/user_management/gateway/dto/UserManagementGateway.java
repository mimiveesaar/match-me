package tech.kood.match_me.user_management.gateway.dto;


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
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenCommandHandler;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenResults;


@RestController
@RequestMapping("/api/v1/user-management")
@Tag(name = "User Management", description = "API for managing user information")
public class UserManagementGateway {

    private final CreateAccessTokenCommandHandler createAccessTokenCommandHandler;

    public UserManagementGateway(CreateAccessTokenCommandHandler createAccessTokenCommandHandler)  {
        this.createAccessTokenCommandHandler = createAccessTokenCommandHandler;
    }

    @PostMapping("/access-token")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Access token retrieved successfully.",
            content = @Content(mediaType = "application/json", schema = @Schema(
                    implementation = CreateAccessTokenResults.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = CreateAccessTokenResults.InvalidRequest.class))),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CreateAccessTokenResults.InvalidToken.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = CreateAccessTokenResults.SystemError.class)))})

    public ResponseEntity<CreateAccessTokenResults> getAccessToken(@RequestBody CreateAccessTokenRequest request) {

        var result = createAccessTokenCommandHandler.handle(request);
        return switch (result) {
            case CreateAccessTokenResults.Success success -> ResponseEntity.ok(success);
            case CreateAccessTokenResults.InvalidToken invalidToken -> ResponseEntity.status(401).body(invalidToken);
            case CreateAccessTokenResults.InvalidRequest invalidRequest ->
                    ResponseEntity.status(400).body(invalidRequest);
            case CreateAccessTokenResults.SystemError systemError -> ResponseEntity.status(500).body(systemError);
            case null -> ResponseEntity.status(500).body(new CreateAccessTokenResults.SystemError("Unexpected error occurred"));
        };
    }
}