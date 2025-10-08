package tech.kood.match_me.user_management.features.user.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;

public class GetUserByEmail {

    @ApplicationLayer
    public record Request(@NotNull @Valid @JsonProperty("email") EmailDTO email) {

        public Request withEmail(EmailDTO email) {
            return new Request(email);
        }
    }

    public sealed interface Result
            permits
            Result.Success,
            Result.UserNotFound,
            Result.InvalidRequest,
            Result.SystemError  {

        record Success(@NotNull @Valid @JsonProperty("user_id") UserDTO user) implements Result {}
        record InvalidRequest(@NotNull @Valid @JsonProperty("data") InvalidInputErrorDTO error) implements Result {}
        record UserNotFound(@NotNull @Valid @JsonProperty("email") EmailDTO email) implements Result {}
        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {}
    }


    public interface Handler {
        @Transactional
        Result handle(Request request);
    }
}