package tech.kood.match_me.connections.features.rejectedConnection.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.validation.ValidGetRejectionBetweenUsers;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;

import java.io.Serializable;

public class GetRejectionBetweenUsers {

    @ValidGetRejectionBetweenUsers
    public record Request(@NotNull @Valid @JsonProperty("user1") UserIdDTO user1,
                          @NotNull @Valid @JsonProperty("user2") UserIdDTO user2) implements Serializable {

        public Request withUser1(UserIdDTO user1) {
            return new Request(user1, user2);
        }

        public Request withUser2(UserIdDTO user2) {
            return new Request(user1, user2);
        }
    }

    public sealed interface Result
            permits Result.Success, Result.NotFound,
            Result.SystemError,
            Result.InvalidRequest {

        record Success(@NotNull @Valid @JsonProperty("rejection") RejectedConnectionDTO rejection)
                implements Result {
        }

        record NotFound()
                implements Result {
        }

        record InvalidRequest(@NotNull @JsonProperty("error") InvalidInputErrorDTO error)
                implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message)
                implements Result {
        }
    }

    @ApplicationLayer
    public interface Handler {
        Result handle(Request request);
    }
}
