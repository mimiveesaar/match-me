package tech.kood.match_me.connections.features.pendingConnection.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

import java.io.Serializable;

public class CreateRequest {

    @Command
    public record Request(@NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId,
                          @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId) implements Serializable {

        public Request withTargetUserId(UserIdDTO targetUserId) {
            return new Request(targetUserId, senderId);
        }

        public Request withSenderId(UserIdDTO senderId) {
            return new Request(targetId, senderId);
        }
    }

    @ApplicationLayer
    public interface Handler {
        Result handle(Request request);
    }

    @DomainEvent
    public record ConnectionRequestCreated(
            @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
            @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId,
            @NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId
    ) {
    }

    public sealed interface Result
            permits Result.Success, Result.AlreadyExists,
            Result.SystemError, Result.InvalidRequest {

        record Success(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO)
                implements Result {
        }

        record AlreadyExists() implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
                implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message)
                implements Result {
        }
    }

}
