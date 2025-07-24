package tech.kood.match_me.user_management.internal.mappers;

import org.springframework.stereotype.Component;

import tech.kood.match_me.user_management.api.DTOs.LoginResultsDTO;
import tech.kood.match_me.user_management.internal.features.login.LoginResults;

@Component
public final class LoginResultsMapper {

    public LoginResultsDTO toDTO(LoginResults loginResult) {
        return switch (loginResult) {
            case LoginResults.Success success -> new LoginResultsDTO.Success(success.refreshToken().token());
            case LoginResults.InvalidCredentials invalid ->
                new LoginResultsDTO.InvalidCredentials(invalid.username(), invalid.password());
            case LoginResults.InvalidRequest invalid -> new LoginResultsDTO.InvalidRequest(invalid.message());
            case LoginResults.SystemError error -> new LoginResultsDTO.SystemError(error.message());
        };
    }
}