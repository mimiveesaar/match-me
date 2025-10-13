package tech.kood.match_me.user_management.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.AccessTokenDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;
import tech.kood.match_me.user_management.features.user.actions.LoginUser;

@RestController
@RequestMapping("/api/v1/user-management")
@Tag(name = "User Management", description = "API for managing user information")
public class UserLoginController {

    private final LoginUser.Handler loginUserHandler;

    public UserLoginController(LoginUser.Handler loginUserHandler) {
        this.loginUserHandler = loginUserHandler;
    }

    public record LoginUserDTO(
            @NotNull @Valid @JsonProperty("email") EmailDTO email,
            @NotNull @Valid @JsonProperty("password") PasswordDTO password
    ) {
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = LoginUserResponseDTO.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = LoginUserResponseDTO.InvalidCredentials.class, name = "INVALID_CREDENTIALS"),
            @JsonSubTypes.Type(value = LoginUserResponseDTO.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = LoginUserResponseDTO.SystemError.class, name = "SYSTEM_ERROR")
    })
    public sealed interface LoginUserResponseDTO
            permits
            LoginUserResponseDTO.Success,
            LoginUserResponseDTO.InvalidCredentials,
            LoginUserResponseDTO.InvalidRequest,
            LoginUserResponseDTO.SystemError {

        record Success(@JsonProperty("refresh_token") RefreshTokenDTO refreshToken,
                       @JsonProperty("access_token") AccessTokenDTO accessToken) implements LoginUserResponseDTO {
        }

        record InvalidRequest(@JsonProperty("data") InvalidInputErrorDTO error) implements LoginUserResponseDTO {
        }

        record InvalidCredentials() implements LoginUserResponseDTO {
        }

        record SystemError(@JsonProperty("message") String message) implements LoginUserResponseDTO {
        }
    }

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginUserResponseDTO.Success.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginUserResponseDTO.InvalidRequest.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginUserResponseDTO.InvalidCredentials.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "type",
                                    implementation = LoginUserResponseDTO.SystemError.class)))})

    public ResponseEntity<LoginUserResponseDTO> loginUser(@RequestBody @Validated LoginUserDTO request) {

        var internalRequest = new LoginUser.Request(request.email(), request.password());
        var loginResult = loginUserHandler.handle(internalRequest);

        if (loginResult instanceof LoginUser.Result.Success success) {
            return ResponseEntity.ok(new LoginUserResponseDTO.Success(success.refreshToken(), success.accessToken()));

        } else if (loginResult instanceof LoginUser.Result.InvalidRequest(InvalidInputErrorDTO error)) {
            return ResponseEntity.badRequest().body(new LoginUserResponseDTO.InvalidRequest(error));
        } else if (loginResult instanceof LoginUser.Result.InvalidCredentials) {
            return ResponseEntity.status(401).body(new LoginUserResponseDTO.InvalidCredentials());
        } else if (loginResult instanceof LoginUser.Result.SystemError(String message)) {
            return ResponseEntity.status(500).body(new LoginUserResponseDTO.SystemError(message));
        } else {
            // This should never happen, but just in case
            return ResponseEntity.status(500).body(new LoginUserResponseDTO.SystemError("Unexpected error occurred"));
        }
    }
}
