package tech.kood.match_me.user_management.features.user.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.AccessTokenDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;

public class LoginUser {

    @Command
    public record Request(@NotNull @Valid @JsonProperty("email") EmailDTO email,
                               @NotNull @Valid @JsonProperty("password") PasswordDTO password){

        public Request withEmail(EmailDTO email) {
            return new Request(email, password);
        }

        public Request withPassword(PasswordDTO password) {
            return new Request(email, password);
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes(
            {
                    @JsonSubTypes.Type(value = Result.Success.class, name = "SUCCESS"),
                    @JsonSubTypes.Type(value = Result.InvalidRequest.class, name = "INVALID_REQUEST"),
                    @JsonSubTypes.Type(value = Result.InvalidCredentials.class, name = "INVALID_CREDENTIALS"),
                    @JsonSubTypes.Type(value = Result.SystemError.class, name = "SYSTEM_ERROR")
            }
    )
    public sealed interface Result
            permits
            Result.Success,
            Result.InvalidCredentials,
            Result.InvalidRequest,
            Result.SystemError {

        record Success(@NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken, @NotNull @Valid @JsonProperty("access_token") AccessTokenDTO accessToken) implements Result {
        }

        record InvalidRequest(@NotNull @Valid @JsonProperty("data") InvalidInputErrorDTO error) implements Result {
        }

        record InvalidCredentials() implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {
        }
    }

    @DomainEvent
    public record UserLoggedIn(UserIdDTO userId) {
    }


    @ApplicationLayer
    public interface Handler {
        Result handle(Request request);
    }

}
