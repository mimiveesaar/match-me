package tech.kood.match_me.connections.features.rejectedConnection.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionDTO;

import java.io.Serializable;
import java.util.List;

public class GetRejectionsByUser {

    public record Request(@NotNull @Valid @JsonProperty("rejected_user") UserIdDTO rejectedUser) implements Serializable {

        public Request withRejectedUser(UserIdDTO rejectedUser) {
            return new Request(rejectedUser);
        }
    }

    public sealed interface Result permits Result.Success,
            Result.SystemError, Result.InvalidRequest {

        record Success(@NotNull @JsonProperty("rejections") List<@Valid RejectedConnectionDTO> rejections)
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
