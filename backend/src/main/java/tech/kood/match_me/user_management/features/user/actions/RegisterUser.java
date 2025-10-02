package tech.kood.match_me.user_management.features.user.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.cqrs.Command;
import org.jmolecules.architecture.cqrs.QueryModel;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.jmolecules.event.annotation.DomainEvent;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.common.domain.api.EmailDTO;
import tech.kood.match_me.user_management.common.domain.api.PasswordDTO;

public class RegisterUser {

    @Command
    @ApplicationLayer
    public record Request(@Valid @NotNull @JsonProperty("email") EmailDTO email, @Valid @NotNull @JsonProperty("password") PasswordDTO password) {

        public Request withPassword(PasswordDTO password) {
            return new Request(email, password);
        }

        public Request withEmail(EmailDTO email) {
            return new Request(email, password);
        }
    }

    @QueryModel
    @ApplicationLayer
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Result.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = Result.EmailExists.class, name = "EMAIL_EXISTS"),
            @JsonSubTypes.Type(value = Result.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = Result.SystemError.class, name = "SYSTEM_ERROR")
    })

    public sealed interface Result permits
            Result.Success,
            Result.EmailExists,
            Result.InvalidRequest,
            Result.SystemError {

        record Success(@NotNull @JsonProperty("user_id") UserIdDTO userId) implements Result{
        }

        record EmailExists(@NotNull @JsonProperty("email") EmailDTO email) implements Result {
        }

        record InvalidRequest(@NotNull @Valid @JsonProperty("data") InvalidInputErrorDTO error) implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {
        }
    }

    @DomainEvent
    public record UserRegistered(
            @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId
    ) {}

    @ApplicationLayer
    public interface Handler {
        Result handle(Request request) throws CheckedConstraintViolationException;
    }
}
