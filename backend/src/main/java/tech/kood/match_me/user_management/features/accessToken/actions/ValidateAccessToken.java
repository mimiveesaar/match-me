package tech.kood.match_me.user_management.features.accessToken.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.common.domain.api.AccessTokenDTO;

public class ValidateAccessToken {

    public record Request(@NotBlank @JsonProperty("jwt") String jwtToken) {

        public Request withJwtToken(String jwtToken) {
            return new Request(jwtToken);
        }
    }

    @ApplicationLayer
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Result.Success.class, name = "SUCCESS"),
            @JsonSubTypes.Type(value = Result.InvalidRequest.class, name = "INVALID_REQUEST"),
            @JsonSubTypes.Type(value = Result.InvalidToken.class, name = "INVALID_TOKEN"),
            @JsonSubTypes.Type(value = Result.SystemError.class, name = "SYSTEM_ERROR")
    })

    public sealed interface Result permits
            Result.Success,
            Result.InvalidToken,
            Result.InvalidRequest,
            Result.SystemError {

        record Success(@NotNull @Valid @JsonProperty("access_token") AccessTokenDTO accessToken,
                       @NotNull @Valid @JsonProperty("user_id") UserIdDTO userId) implements Result {
        }

        record InvalidRequest(@NotNull @Valid @JsonProperty("error") InvalidInputErrorDTO error) implements Result {
        }

        record InvalidToken() implements Result {
        }

        record SystemError(@NotEmpty @JsonProperty("message") String message) implements Result {
        }
    }

    public interface Handler {
        Result handle(Request request);
    }
}
