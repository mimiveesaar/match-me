package tech.kood.match_me.user_management.internal.features.login;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import tech.kood.match_me.user_management.api.DTOs.LoginRequestDTO;
import tech.kood.match_me.user_management.api.DTOs.LoginResultsDTO;
import tech.kood.match_me.user_management.internal.mappers.LoginResultsMapper;

@RestController
@RequestMapping("/api/user-management")
@Tag(name = "User Management", description = "API for user management operations")
public class LoginEndpoint {

    private final LoginHandler loginHandler;

    private final LoginResultsMapper loginResultsMapper;

    public LoginEndpoint(LoginHandler loginHandler, LoginResultsMapper loginResultsMapper) {
        this.loginHandler = loginHandler;
        this.loginResultsMapper = loginResultsMapper;
    }

    @PostMapping("/login")
    public LoginResultsDTO loginUser(@RequestBody LoginRequestDTO request) {

        var internalRequest = new LoginRequest(UUID.randomUUID(), request.email(),
                request.password(), Optional.of(UUID.randomUUID().toString()) // Assuming tracingId
                                                                              // is generated here
        );

        var result = loginHandler.handle(internalRequest);

        return loginResultsMapper.toDTO(result);
    }
}
