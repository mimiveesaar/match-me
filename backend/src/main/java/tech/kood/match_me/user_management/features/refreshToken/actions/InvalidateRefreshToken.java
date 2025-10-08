package tech.kood.match_me.user_management.features.refreshToken.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;

public class InvalidateRefreshToken {

    @Command
    public record Request(@NotNull @Valid @JsonProperty("secret") RefreshTokenSecretDTO secret) {

        public Request withSecret(RefreshTokenSecretDTO secret) {
            return new Request(secret);
        }
    }

    public sealed interface Result permits
            Result.Success,
            Result.TokenNotFound,
            Result.InvalidRequest,
            Result.SystemError {

        record Success() implements Result {
        }
        record TokenNotFound() implements Result {
        }
        record InvalidRequest(@NotEmpty @JsonProperty("data") InvalidInputErrorDTO error) implements Result {
        }
        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {
        }
    }

    @DomainEvent
    public record RefreshTokenInvalidated(RefreshTokenDTO invalidatedRefreshToken) {}


    public interface Handler {
        Result handle(Request request);
    }
}