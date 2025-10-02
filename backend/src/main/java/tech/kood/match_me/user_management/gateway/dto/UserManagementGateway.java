package tech.kood.match_me.user_management.gateway.dto;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenCommandHandler;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenRequest;
import tech.kood.match_me.user_management.features.accessToken.actions.createAccessToken.api.CreateAccessTokenResults;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenCommandHandler;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenRequest;
import tech.kood.match_me.user_management.features.refreshToken.actions.invalidateToken.api.InvalidateRefreshTokenResults;
import tech.kood.match_me.user_management.features.user.actions.LoginUser;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;


@RestController
@RequestMapping("/api/v1/user-management")
@Tag(name = "User Management", description = "API for managing user information")
public class UserManagementGateway {

    private final RegisterUser.Handler registerUserHandler;
    private final LoginUser.Handler loginUserHandler;
    private final CreateAccessTokenCommandHandler createAccessTokenCommandHandler;
    private final InvalidateRefreshTokenCommandHandler invalidateRefreshTokenCommandHandler;

    public UserManagementGateway(RegisterUser.Handler registerUserHandler, LoginUser.Handler loginUserHandler, CreateAccessTokenCommandHandler createAccessTokenCommandHandler, InvalidateRefreshTokenCommandHandler invalidateRefreshTokenCommandHandler) {
        this.registerUserHandler = registerUserHandler;
        this.loginUserHandler = loginUserHandler;
        this.createAccessTokenCommandHandler = createAccessTokenCommandHandler;
        this.invalidateRefreshTokenCommandHandler = invalidateRefreshTokenCommandHandler;
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RegisterUser.Result.Success.class))),

            @ApiResponse(responseCode = "409", description = "Email already exists.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUser.Result.EmailExists.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =  RegisterUser.Result.InvalidRequest.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUser.Result.SystemError.class)))}
    )

    public ResponseEntity<RegisterUser.Result> registerUser(@RequestBody RegisterDTO request) {

        var internalRequest = new RegisterUser.Request(request.email(), request.password());

        try {
            var result = registerUserHandler.handle(internalRequest);

            if (result instanceof RegisterUser.Result.Success success) {
                return ResponseEntity.ok(success);
            } else if (result instanceof RegisterUser.Result.InvalidRequest invalidRequest) {
                return ResponseEntity.status(400).body(invalidRequest);
            } else if (result instanceof RegisterUser.Result.EmailExists emailExists) {
                return ResponseEntity.status(409).body(emailExists);
            } else if (result instanceof RegisterUser.Result.SystemError systemError) {
                return ResponseEntity.status(500).body(systemError);
            }

        } catch (CheckedConstraintViolationException e) {
            return ResponseEntity.internalServerError().body(new RegisterUser.Result.SystemError(e.getMessage()));
        }

        //This should never happen.
        return ResponseEntity.internalServerError().body(new RegisterUser.Result.SystemError("Internal server error"));
    }


    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginUser.Result.Success.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginUser.Result.InvalidRequest.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginUser.Result.InvalidCredentials.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginUser.Result.SystemError.class)))})

    public ResponseEntity<LoginUser.Result> loginUser(@RequestBody LoginUser.Request request) {

        var internalRequest = new LoginUser.Request(request.email(), request.password());
        var loginResult = loginUserHandler.handle(internalRequest);

        if (loginResult instanceof LoginUser.Result.Success success) {
            return ResponseEntity.ok(success);
        } else if (loginResult instanceof LoginUser.Result.InvalidRequest invalidRequest) {
            return ResponseEntity.badRequest().body(invalidRequest);
        } else if (loginResult instanceof LoginUser.Result.InvalidCredentials invalidCredentials) {
            return ResponseEntity.status(401).body(invalidCredentials);
        } else if (loginResult instanceof LoginUser.Result.SystemError systemError) {
            return ResponseEntity.status(500).body(systemError);
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500)
                    .body(new LoginUser.Result.SystemError("Unexpected error occurred"));
        }
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

        var internalRequest = new InvalidateRefreshTokenRequest(request.secret());
        var result = invalidateRefreshTokenCommandHandler.handle(internalRequest);

        return switch (result) {
            case InvalidateRefreshTokenResults.Success success -> ResponseEntity.ok(success);
            case InvalidateRefreshTokenResults.TokenNotFound tokenNotFound ->
                    ResponseEntity.status(401).body(tokenNotFound);
            case InvalidateRefreshTokenResults.InvalidRequest invalidRequest ->
                    ResponseEntity.status(400).body(invalidRequest);
            case InvalidateRefreshTokenResults.SystemError systemError -> ResponseEntity.status(500).body(systemError);
            case null ->
                    ResponseEntity.status(500).body(new InvalidateRefreshTokenResults.SystemError("Unexpected error occurred"));
        };
    }
}