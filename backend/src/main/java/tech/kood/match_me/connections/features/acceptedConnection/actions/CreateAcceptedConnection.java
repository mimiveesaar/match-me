package tech.kood.match_me.connections.features.acceptedConnection.actions;

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
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.validation.ValidCreateAcceptedConnection;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;

public class CreateAcceptedConnection {

    @DomainEvent
    public record AcceptedConnectionCreated(@NotNull @Valid AcceptedConnectionDTO acceptedConnectionDTO) {
    }

    @ApplicationLayer
    public interface Handler {
        Result handle(Request request);
    }
    
    @Command
    @ValidCreateAcceptedConnection
    public record Request(
            @NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId,
            @NotNull @Valid @JsonProperty("accepted_by_user") UserIdDTO acceptedByUser,
            @NotNull @Valid @JsonProperty("accepted_user") UserIdDTO acceptedUser) {

        public Request withConnectionId(ConnectionIdDTO connectionId) {
            return new Request(connectionId, acceptedByUser, acceptedUser);
        }

        public Request withAcceptedByUser(UserIdDTO acceptedByUser) {
            return new Request(connectionId, acceptedByUser, acceptedUser);
        }

        public Request withAcceptedUser(UserIdDTO acceptedUser) {
            return new Request(connectionId, acceptedByUser, acceptedUser);
        }
    }

    public sealed interface Result
            permits Result.Success, Result.AlreadyExists,
            Result.SystemError, Result.InvalidRequest {

        record Success(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO) implements Result {
        }

        record AlreadyExists() implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error) implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {
        }
    }
}