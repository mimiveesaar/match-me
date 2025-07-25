package tech.kood.match_me.user_management.internal.features.login;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.kood.match_me.user_management.api.DTOs.LoginRequestDTO;
import tech.kood.match_me.user_management.api.DTOs.LoginResultsDTO;
import tech.kood.match_me.user_management.internal.mappers.LoginResultsMapper;

@RestController
@RequestMapping("/api/v1/user-management/auth")
@Tag(name = "User Management", description = "API for user management operations")
public class LoginEndpoint {

    private final LoginHandler loginHandler;

    private final LoginResultsMapper loginResultsMapper;

    public LoginEndpoint(LoginHandler loginHandler, LoginResultsMapper loginResultsMapper) {
        this.loginHandler = loginHandler;
        this.loginResultsMapper = loginResultsMapper;
    }

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResultsDTO.Success.class))),

            @ApiResponse(responseCode = "400", description = "Invalid request.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "kind",
                                    implementation = LoginResultsDTO.InvalidCredentials.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(discriminatorProperty = "kind",
                                    implementation = LoginResultsDTO.InvalidRequest.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResultsDTO.SystemError.class)))})

    public ResponseEntity<LoginResultsDTO> loginUser(@RequestBody LoginRequestDTO request) {

        var internalRequest = new LoginRequest(UUID.randomUUID(), request.email(),
                request.password(), Optional.of(UUID.randomUUID().toString()));

        var result = loginHandler.handle(internalRequest);
        var responseDTO = loginResultsMapper.toDTO(result);


        switch (responseDTO) {
            case LoginResultsDTO.Success success -> {
                return ResponseEntity.ok(success);
            }
            case LoginResultsDTO.InvalidCredentials invalid -> {
                return ResponseEntity.status(401).body(invalid);
            }
            case LoginResultsDTO.InvalidRequest invalid -> {
                return ResponseEntity.status(400).body(invalid);
            }
            case LoginResultsDTO.SystemError error -> {
                return ResponseEntity.status(500).body(error);
            }
        }
    }
}
