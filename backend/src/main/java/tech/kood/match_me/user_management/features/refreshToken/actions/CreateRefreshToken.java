package tech.kood.match_me.user_management.features.refreshToken.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.features.refreshToken.domain.api.RefreshTokenDTO;

public class CreateRefreshToken {

    @Command
    public record Request(@NotNull @Valid @JsonProperty("userId") UserIdDTO userId) {
    }

    @DomainEvent
    public record RefreshTokenCreated(@NotNull @Valid RefreshTokenDTO refreshToken) {}

    @ApplicationLayer
    @QueryModel
    public sealed interface Result permits Result.Success, Result.UserNotFound, Result.InvalidRequest, Result.SystemError {

        record Success(@NotNull @Valid @JsonProperty("refresh_token") RefreshTokenDTO refreshToken) implements Result {
        }

        record InvalidRequest(@NotNull @Valid @JsonProperty("data") InvalidInputErrorDTO error) implements Result {
        }

        record UserNotFound(@NotEmpty @Valid @JsonProperty("user_id") UserIdDTO userId) implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {
        }
    }

    public interface Handler {
        Result handle(Request request);
    }
}