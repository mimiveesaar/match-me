package tech.kood.match_me.connections.features.acceptedConnection.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;

import java.util.List;

public class GetAcceptedConnections {

    public record Request(
            @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId
    ) {
        public Request withUserId(UserIdDTO userId) {
            return new Request(userId);
        }
    }

    public sealed interface Result permits
            Result.Success,
            Result.InvalidRequest,
            Result.SystemError {

        record Success(
                @NotNull @JsonProperty("accepted_connections") List<AcceptedConnectionDTO> acceptedConnections) implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error
        ) implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {
        }
    }

    public interface Handler {
        Result handle(Request request);
    }
}
