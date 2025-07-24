package tech.kood.match_me.user_management.internal.features.registerUser;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.user_management.api.DTOs.RegisterUserRequestDTO;
import tech.kood.match_me.user_management.api.DTOs.RegisterUserResultsDTO;
import tech.kood.match_me.user_management.internal.mappers.RegisterUserResultsMapper;

@RestController
@RequestMapping("/api/user-management")
public class RegisterUserEndpoint {

    private final RegisterUserHandler registerUserHandler;
    private final RegisterUserResultsMapper registerUserResultsMapper;

    public RegisterUserEndpoint(RegisterUserHandler registerUserHandler,
            RegisterUserResultsMapper registerUserResultsMapper) {
        this.registerUserHandler = registerUserHandler;
        this.registerUserResultsMapper = registerUserResultsMapper;
    }

    @PostMapping("/register")
    public RegisterUserResultsDTO registerUser(@RequestBody RegisterUserRequestDTO request) {

        var internalRequest = new RegisterUserRequest(
                UUID.randomUUID(),
                request.username(),
                request.password(),
                request.email(),
                Optional.of(UUID.randomUUID().toString()));

        var result = registerUserHandler.handle(internalRequest);
        return registerUserResultsMapper.toDTO(result);
    }
}