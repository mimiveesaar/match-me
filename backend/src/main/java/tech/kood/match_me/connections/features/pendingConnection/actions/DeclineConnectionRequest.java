package tech.kood.match_me.connections.features.pendingConnection.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public class DeclineConnectionRequest {

    @DomainEvent
    public record ConnectionRequestDeclined(
            @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
            @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId,
            @NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId
    ) {
    }

    @DomainEvent
    public record ConnectionRequestUndo(
            @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
            @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId,
            @NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId
    ) {
    }

    public interface Handler {
        Result handle(Request request);
    }

    @Command
    public record Request(
            @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
            @NotNull @Valid @JsonProperty("declined_by_user") UserIdDTO declinedByUser
    ) {
        public Request withConnectionId(ConnectionIdDTO connectionIdDTO) {
            return new Request(connectionIdDTO, declinedByUser);
        }
        public Request withDeclinedByUser(UserIdDTO declinedByUser) {
            return new Request(connectionIdDTO, declinedByUser);
        }
    }

    public sealed interface Result permits
            Result.Success, Result.NotFound, Result.InvalidRequest,
            Result.AlreadyDeclined, Result.SystemError {

        record Success() implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error) implements Result {
        }

        record NotFound() implements Result {
        }

        record AlreadyDeclined() implements Result {
        }

        record SystemError(@NotEmpty String message) implements Result {
        }
    }
}
