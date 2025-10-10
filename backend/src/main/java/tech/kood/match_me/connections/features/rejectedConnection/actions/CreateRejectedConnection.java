package tech.kood.match_me.connections.features.rejectedConnection.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.validation.ValidCreateRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;

import java.io.Serializable;

public class CreateRejectedConnection {

    public sealed interface Result
            permits Result.Success,
            Result.AlreadyExists, Result.SystemError,
            Result.InvalidRequest {

        record Success(@NotNull @JsonProperty("connection_id") ConnectionIdDTO connectionIdDTO)
                implements Result {
        }

        record AlreadyExists()
                implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
                implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message)
                implements Result {
        }
    }

    @Command
    @ValidCreateRejectedConnection
    public record Request(@NotNull @Valid @JsonProperty("rejected_by_user") UserIdDTO rejectedByUser,
                          @NotNull @Valid @JsonProperty("rejected_user") UserIdDTO rejectedUser,
                          @NotNull @JsonProperty("reason") RejectedConnectionReasonDTO reason) implements Serializable {


        public Request withRejectedByUser(UserIdDTO rejectedByUser) {
            return new Request(rejectedByUser, rejectedUser, reason);
        }

        public Request withRejectedUser(UserIdDTO rejectedUser) {
            return new Request(rejectedByUser, rejectedUser, reason);
        }

        public Request withReason(RejectedConnectionReasonDTO reason) {
            return new Request(rejectedByUser, rejectedUser, reason);
        }
    }

    @ApplicationLayer
    public interface Handler {
        Result handle(Request request);
    }
}
