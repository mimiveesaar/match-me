package tech.kood.match_me.user_management.api.DTOs;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "GetAccessTokenResultsDTO", oneOf = {GetAccessTokenResultsDTO.Success.class,
        GetAccessTokenResultsDTO.InvalidToken.class, GetAccessTokenResultsDTO.InvalidRequest.class,
        GetAccessTokenResultsDTO.SystemError.class})
public sealed interface GetAccessTokenResultsDTO extends Serializable
        permits GetAccessTokenResultsDTO.Success, GetAccessTokenResultsDTO.InvalidToken,
        GetAccessTokenResultsDTO.InvalidRequest, GetAccessTokenResultsDTO.SystemError {


    @Schema(name = "GetAccessTokenSuccessDTO")
    record Success(String jwt, String kind, String tracingId) implements GetAccessTokenResultsDTO {
        public Success(String jwt, String tracingId) {
            this(jwt, "success", tracingId);
        }
    }

    @Schema(name = "GetAccessTokenInvalidTokenDTO")
    record InvalidToken(String token, String kind, String tracingId)
            implements GetAccessTokenResultsDTO {
        public InvalidToken(String token, String tracingId) {
            this(token, "invalid_token", tracingId);
        }
    }

    @Schema(name = "GetAccessTokenInvalidRequestDTO")
    record InvalidRequest(String message, String kind, String tracingId)
            implements GetAccessTokenResultsDTO {
        public InvalidRequest(String message, String tracingId) {
            this(message, "invalid_request", tracingId);
        }
    }

    @Schema(name = "GetAccessTokenSystemErrorDTO")
    record SystemError(String message, String kind, String tracingId)
            implements GetAccessTokenResultsDTO {
        public SystemError(String message, String tracingId) {
            this(message, "system_error", tracingId);
        }
    }
}
