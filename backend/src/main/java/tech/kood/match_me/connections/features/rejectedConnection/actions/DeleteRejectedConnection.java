package tech.kood.match_me.connections.features.rejectedConnection.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;

public class DeleteRejectedConnection {

    @ApplicationLayer
    public interface Handler {
        Result handle(Request request);
    }

    @Command
    public record Request(@NotNull @Valid @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO) {

        public Request withConnectionId(ConnectionIdDTO connectionIdDTO) {
            return new Request(connectionIdDTO);
        }
    }

    public sealed interface Result
            permits Result.Success, Result.NotFound,
            Result.AlreadyDeleted,
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

        record AlreadyDeleted()
                implements Result {
        }

        record SystemError(@NotEmpty String message)
                implements Result {
        }
    }
}
