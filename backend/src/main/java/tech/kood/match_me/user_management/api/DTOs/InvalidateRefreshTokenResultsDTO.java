package tech.kood.match_me.user_management.api.DTOs;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;


@Schema(name = "InvalidateRefreshTokenResultsDTO",
        oneOf = {InvalidateRefreshTokenResultsDTO.Success.class,
                InvalidateRefreshTokenResultsDTO.TokenNotFound.class,
                InvalidateRefreshTokenResultsDTO.SystemError.class})
public sealed interface InvalidateRefreshTokenResultsDTO extends Serializable permits
        InvalidateRefreshTokenResultsDTO.Success, InvalidateRefreshTokenResultsDTO.TokenNotFound,
        InvalidateRefreshTokenResultsDTO.SystemError {

    @Schema(name = "InvalidateRefreshTokenSuccessDTO")
    record Success(@NotEmpty String kind, @Nullable String tracingId)
            implements InvalidateRefreshTokenResultsDTO {
        public Success(String tracingId) {
            this("success", tracingId);
        }
    }

    @Schema(name = "InvalidateRefreshTokenTokenNotFoundDTO")
    record TokenNotFound(@NotEmpty String refreshToken, @NotEmpty String kind,
            @Nullable String tracingId) implements InvalidateRefreshTokenResultsDTO {

        public TokenNotFound(String refreshToken, String tracingId) {
            this(refreshToken, "not_found", tracingId);
        }
    }

    @Schema(name = "InvalidateRefreshTokenSystemErrorDTO")
    record SystemError(@NotEmpty String message, @NotEmpty String kind, @Nullable String tracingId)
            implements InvalidateRefreshTokenResultsDTO {

        public SystemError(String message, String tracingId) {
            this(message, "system_error", tracingId);
        }
    }
}
