package tech.kood.match_me.user_management.api.DTOs;

import java.io.Serializable;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "GetAccessTokenResultsDTO", oneOf = {GetAccessTokenResultsDTO.Success.class,
        GetAccessTokenResultsDTO.InvalidToken.class, GetAccessTokenResultsDTO.SystemError.class})
public sealed interface GetAccessTokenResultsDTO extends Serializable
        permits GetAccessTokenResultsDTO.Success, GetAccessTokenResultsDTO.InvalidToken,
        GetAccessTokenResultsDTO.SystemError {


    @Schema(name = "GetAccessTokenSuccessDTO")
    record Success(@NotEmpty String jwt, @NotEmpty String kind, @Nullable String tracingId)
            implements GetAccessTokenResultsDTO {
        public Success(String jwt, String tracingId) {
            this(jwt, "success", tracingId);
        }
    }

    @Schema(name = "GetAccessTokenInvalidTokenDTO")
    record InvalidToken(@NotEmpty String token, @NotEmpty String kind, @Nullable String tracingId)
            implements GetAccessTokenResultsDTO {
        public InvalidToken(String token, String tracingId) {
            this(token, "invalid_token", tracingId);
        }
    }

    @Schema(name = "GetAccessTokenSystemErrorDTO")
    record SystemError(@NotEmpty String message, @NotEmpty String kind, @Nullable String tracingId)
            implements GetAccessTokenResultsDTO {
        public SystemError(String message, String tracingId) {
            this(message, "system_error", tracingId);
        }
    }
}
