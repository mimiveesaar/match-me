package tech.kood.match_me.user_management.features.accessToken.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.AccessTokenDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;

public class CreateAccessToken {

    @DomainEvent
    public record AccessTokenCreated(@NotNull @Valid @JsonProperty("access_token") AccessTokenDTO accessToken) {
    }

    @Command
    public record Request(@NotNull @Valid @JsonProperty("secret") RefreshTokenSecretDTO secret) {

        public Request withSecret(RefreshTokenSecretDTO secret) {
            return new Request(secret);
        }
    }

    @ApplicationLayer
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Result.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = Result.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = Result.InvalidToken.class, name = "INVALID_TOKEN"),
            @JsonSubTypes.Type(value = Result.SystemError.class, name = "SYSTEM_ERROR")
    })
    public sealed interface Result
            permits Result.Success,
            Result.InvalidToken,
            Result.InvalidRequest,
            Result.SystemError {


        record Success(@NotEmpty @JsonProperty("jwt") String jwt) implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("data") InvalidInputErrorDTO error) implements Result {
        }

        record InvalidToken() implements Result {
        }

        record SystemError(@NotEmpty String message) implements Result {
        }
    }

    @ApplicationLayer
    public interface Handler {
        @Transactional
        Result handle(Request request);
    }
}
