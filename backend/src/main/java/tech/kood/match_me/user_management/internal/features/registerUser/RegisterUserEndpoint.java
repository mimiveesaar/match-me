package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import tech.kood.match_me.user_management.api.DTOs.RegisterUserRequestDTO;
import tech.kood.match_me.user_management.api.DTOs.RegisterUserResultsDTO;
import tech.kood.match_me.user_management.internal.mappers.RegisterUserResultsMapper;

@RestController
@RequestMapping("/api/v1/user-management/auth")
@Tag(name = "User Management")
public class RegisterUserEndpoint {

    private final RegisterUserHandler registerUserHandler;
    private final RegisterUserResultsMapper registerUserResultsMapper;

    public RegisterUserEndpoint(RegisterUserHandler registerUserHandler,
            RegisterUserResultsMapper registerUserResultsMapper) {
        this.registerUserHandler = registerUserHandler;
        this.registerUserResultsMapper = registerUserResultsMapper;
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RegisterUserResultsDTO.Success.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "kind",
                                    oneOf = {RegisterUserResultsDTO.UsernameExists.class,
                                            RegisterUserResultsDTO.EmailExists.class,
                                            RegisterUserResultsDTO.InvalidEmail.class,
                                            RegisterUserResultsDTO.InvalidPassword.class}))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResultsDTO.class)))})

    public ResponseEntity<RegisterUserResultsDTO> registerUser(
            @RequestBody RegisterUserRequestDTO request) {

        var internalRequest = new RegisterUserRequest(UUID.randomUUID(), request.username(),
                request.password(), request.email(), Optional.of(UUID.randomUUID().toString()));

        var result = registerUserHandler.handle(internalRequest);
        var responseDTO = registerUserResultsMapper.toDTO(result);

        switch (responseDTO) {
            case RegisterUserResultsDTO.Success success -> {
                return ResponseEntity.ok(success);
            }
            case RegisterUserResultsDTO.SystemError systemError -> {
                // For system errors, we return a 500 Internal Server Error
                return ResponseEntity.status(500).body(systemError);
            }
            default -> {
                // For other results, we return a 400 Bad Request with the specific error details
                return ResponseEntity.badRequest().body(responseDTO);
            }
        }
    }
}
