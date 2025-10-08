package tech.kood.match_me.user_management.features.refreshToken.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenSecretDTO;

public class GetRefreshToken {

    public record Request(@NotNull @Valid @JsonProperty("shared_secret") RefreshTokenSecretDTO secret) {

        public Request withSecret(RefreshTokenSecretDTO secret) {
            return new Request(secret);
        }
    }

    @QueryModel
    public sealed interface Result
            permits Result.Success,
            Result.InvalidSecret,
            Result.InvalidRequest,
            Result.SystemError {

        record Success(
                @NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken) implements Result {
        }

        record InvalidRequest(
                @NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error) implements Result {
        }

        record InvalidSecret() implements Result {
        }

        record SystemError(
                @NotEmpty @JsonProperty("message") String message) implements Result {
        }
    }

    public interface Handler {
        Result handle(Request request);
    }
}
