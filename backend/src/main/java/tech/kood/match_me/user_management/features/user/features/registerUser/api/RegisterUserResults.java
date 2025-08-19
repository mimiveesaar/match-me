package tech.kood.match_me.user_management.features.user.features.registerUser.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.UserIdDTO;

@QueryModel
@ApplicationLayer
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
            @JsonSubTypes.Type(value = RegisterUserResults.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = RegisterUserResults.EmailExists.class, name = "EMAIL_EXISTS"),
            @JsonSubTypes.Type(value = RegisterUserResults.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = RegisterUserResults.SystemError.class, name = "SYSTEM_ERROR")
})
public sealed interface RegisterUserResults permits
        RegisterUserResults.Success,
        RegisterUserResults.EmailExists,
        RegisterUserResults.InvalidRequest,
        RegisterUserResults.SystemError {

    record Success(@NotNull @JsonProperty("request_id") UUID requestId,
                   @NotNull @JsonProperty("user_id") UserIdDTO userId,
                   @Nullable @JsonProperty("tracing_id") String tracingId) implements RegisterUserResults {
    }

    record EmailExists(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotNull @JsonProperty("email") EmailDTO email,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements RegisterUserResults {
    }

    record InvalidRequest(@NotNull @JsonProperty("request_id") UUID requestId,
                          @NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error,
                          @Nullable @JsonProperty("tracing_id") String tracingId) implements RegisterUserResults {
    }

    record SystemError(@NotNull @JsonProperty("request_id") UUID requestId,
                       @NotEmpty @JsonProperty("message") String message,
                       @Nullable @JsonProperty("tracing_id") String tracingId) implements RegisterUserResults {
    }
}