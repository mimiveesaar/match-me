package tech.kood.match_me.user_management.api.DTOs;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "InvalidateRefreshTokenResultsDTO",
        oneOf = {InvalidateRefreshTokenResultsDTO.Success.class,
                InvalidateRefreshTokenResultsDTO.TokenNotFound.class,
                InvalidateRefreshTokenResultsDTO.InvalidRequest.class,
                InvalidateRefreshTokenResultsDTO.SystemError.class})
public sealed interface InvalidateRefreshTokenResultsDTO extends Serializable permits
        InvalidateRefreshTokenResultsDTO.Success, InvalidateRefreshTokenResultsDTO.TokenNotFound,
        InvalidateRefreshTokenResultsDTO.InvalidRequest,
        InvalidateRefreshTokenResultsDTO.SystemError {

    @Schema(name = "InvalidateRefreshTokenSuccessDTO")
    record Success(String kind, String tracingId) implements InvalidateRefreshTokenResultsDTO {
        public Success(String tracingId) {
            this("success", tracingId);
        }
    }

    @Schema(name = "InvalidateRefreshTokenTokenNotFoundDTO")
    record TokenNotFound(String refreshToken, String kind, String tracingId)
            implements InvalidateRefreshTokenResultsDTO {

        public TokenNotFound(String refreshToken, String tracingId) {
            this(refreshToken, "not_found", tracingId);
        }
    }

    @Schema(name = "InvalidateRefreshTokenInvalidRequestDTO")
    record InvalidRequest(String message, String kind, String tracingId)
            implements InvalidateRefreshTokenResultsDTO {

        public InvalidRequest(String message, String tracingId) {
            this(message, "invalid_request", tracingId);
        }
    }

    @Schema(name = "InvalidateRefreshTokenSystemErrorDTO")
    record SystemError(String message, String kind, String tracingId)
            implements InvalidateRefreshTokenResultsDTO {

        public SystemError(String message, String tracingId) {
            this(message, "system_error", tracingId);
        }
    }
}
