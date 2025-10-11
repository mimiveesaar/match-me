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

public class AcceptConnectionRequest {

    @DomainEvent
    public record ConnectionRequestAccepted(
            @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
            @NotNull @Valid @JsonProperty("sender_id") UserIdDTO senderId,
            @NotNull @Valid @JsonProperty("target_id") UserIdDTO targetId) {
    }

    @Command
    public record Request(
            @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO,
            @NotNull @Valid @JsonProperty("accepted_by_user") UserIdDTO acceptedByUser) {
        public Request withConnectionId(ConnectionIdDTO connectionIdDTO) {
            return new Request(connectionIdDTO, acceptedByUser);
        }

        public Request withAcceptedByUser(UserIdDTO acceptedByUser) {
            return new Request(connectionIdDTO, acceptedByUser);
        }
    }

    public sealed interface Result permits Result.Success,
            Result.NotFound, Result.InvalidRequest,
            Result.AlreadyAccepted, Result.SystemError {

        record Success() implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
                implements Result {
        }

        record NotFound() implements Result {
        }

        record AlreadyAccepted() implements Result {
        }

        record SystemError(@NotEmpty String message) implements Result {
        }
    }

    public interface Handler {
        Result handle(Request request);
    }
}