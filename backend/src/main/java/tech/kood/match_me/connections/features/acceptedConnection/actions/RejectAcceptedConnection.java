package tech.kood.match_me.connections.features.acceptedConnection.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public class RejectAcceptedConnection {

    @Command
    public record Request(@NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionId,
                          @NotNull @Valid @JsonProperty("user_id") UserIdDTO rejectedBy) {

        public Request withConnectionId(ConnectionIdDTO connectionIdDTO) {
            return new Request(connectionIdDTO, rejectedBy);
        }

        public Request withRejectedBy(UserIdDTO rejectedBy) {
            return new Request(connectionId, rejectedBy);
        }
    }

    public sealed interface Result
            permits Result.Success, Result.NotFound,
            Result.AlreadyRejected,
            Result.InvalidRequest,
            Result.SystemError {

        record Success()
                implements Result {
        }

        record NotFound()
                implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
                implements Result {
        }

        record AlreadyRejected()
                implements Result {
        }

        record SystemError(@NotEmpty String message)
                implements Result {
        }
    }

    @ApplicationLayer
    public interface Handler {
        Result handle(Request request);
    }
}