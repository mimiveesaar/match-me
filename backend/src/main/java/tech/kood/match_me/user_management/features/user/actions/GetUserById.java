package tech.kood.match_me.user_management.features.user.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.user.domain.api.UserDTO;

public class GetUserById {

    @QueryModel
    @ApplicationLayer
    public record Request(@NotNull @Valid @JsonProperty("user_id") UserIdDTO userId) {

        public Request withUserId(UserIdDTO userId) {
            return new Request(userId);
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Result.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = Result.UserNotFound.class, name = "USER_NOT_FOUND"),
            @JsonSubTypes.Type(value = Result.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = Result.SystemError.class, name = "SYSTEM_ERROR")
    })
    public sealed interface Result permits
            Result.Success,
            Result.UserNotFound,
            Result.InvalidRequest,
            Result.SystemError
    {

        record Success(@NotNull @Valid @JsonProperty("user_id") UserDTO user) implements Result {}
        record InvalidRequest(@NotNull @Valid @JsonProperty("data") InvalidInputErrorDTO error) implements Result {}
        record UserNotFound(@NotNull @Valid @JsonProperty("user_id") UserIdDTO userId) implements Result {}
        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {}
    }

    public interface Handler {
        @Transactional
        Result handle(Request request);
    }
}