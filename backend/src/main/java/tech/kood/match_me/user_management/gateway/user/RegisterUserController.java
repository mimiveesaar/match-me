package tech.kood.match_me.user_management.gateway.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;

@RestController
@RequestMapping("/api/v1/user-management")
@Tag(name = "User Management", description = "API for managing user information")
public class RegisterUserController {

    private final RegisterUser.Handler registerUserHandler;

    public RegisterUserController(RegisterUser.Handler registerUserHandler) {
        this.registerUserHandler = registerUserHandler;
    }

    public record RegisterUserDTO(
            @Valid @JsonProperty("email") EmailDTO email,
            @Valid @JsonProperty("password") PasswordDTO password
    ) {
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RegisterUserResponseDTO.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = RegisterUserResponseDTO.EmailExists.class, name = "EMAIL_EXISTS"),
            @JsonSubTypes.Type(value = RegisterUserResponseDTO.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = RegisterUserResponseDTO.SystemError.class, name = "SYSTEM_ERROR")
    })
    public sealed interface RegisterUserResponseDTO permits
            RegisterUserResponseDTO.Success,
            RegisterUserResponseDTO.EmailExists,
            RegisterUserResponseDTO.InvalidRequest,
            RegisterUserResponseDTO.SystemError {

        record Success(@JsonProperty("user_id") UserIdDTO userId) implements RegisterUserResponseDTO {
        }

        record EmailExists(@JsonProperty("email") EmailDTO email) implements RegisterUserResponseDTO {
        }

        record InvalidRequest(@JsonProperty("data") InvalidInputErrorDTO error) implements RegisterUserResponseDTO {
        }

        record SystemError(@JsonProperty("message") String message) implements RegisterUserResponseDTO {
        }
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RegisterUserResponseDTO.Success.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResponseDTO.EmailExists.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResponseDTO.InvalidRequest.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResponseDTO.SystemError.class)))}
    )

    public ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody @Validated RegisterUserDTO request) {

        var internalRequest = new RegisterUser.Request(request.email, request.password);

        try {
            var result = registerUserHandler.handle(internalRequest);

            if (result instanceof RegisterUser.Result.Success success) {
                return ResponseEntity.ok(new RegisterUserResponseDTO.Success(success.userId()));
            } else if (result instanceof RegisterUser.Result.InvalidRequest invalidRequest) {
                return ResponseEntity.status(400).body(new RegisterUserResponseDTO.InvalidRequest(invalidRequest.error()));
            } else if (result instanceof RegisterUser.Result.EmailExists emailExists) {
                return ResponseEntity.status(409).body(new RegisterUserResponseDTO.EmailExists(emailExists.email()));
            } else if (result instanceof RegisterUser.Result.SystemError systemError) {
                return ResponseEntity.status(500).body(new RegisterUserResponseDTO.SystemError(systemError.message()));
            }

        } catch (CheckedConstraintViolationException e) {
            return ResponseEntity.internalServerError().body(new RegisterUserResponseDTO.SystemError(e.getMessage()));
        }

        return ResponseEntity.internalServerError().body(new RegisterUserResponseDTO.SystemError("System Error"));
    }
}