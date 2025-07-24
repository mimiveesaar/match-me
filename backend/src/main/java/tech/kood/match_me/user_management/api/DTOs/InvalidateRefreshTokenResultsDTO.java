package tech.kood.match_me.user_management.api.DTOs;


public sealed interface InvalidateRefreshTokenResultsDTO permits
        InvalidateRefreshTokenResultsDTO.Success, InvalidateRefreshTokenResultsDTO.TokenNotFound,
        InvalidateRefreshTokenResultsDTO.InvalidRequest,
        InvalidateRefreshTokenResultsDTO.SystemError {
    record Success(String kind) implements InvalidateRefreshTokenResultsDTO {
        public Success() {
            this("success");
        }
    }

    record TokenNotFound(String token, String kind, String tracingId)
            implements InvalidateRefreshTokenResultsDTO {

        public TokenNotFound(String token, String tracingId) {
            this(token, "not_found", tracingId);
        }
    }

    record InvalidRequest(String message, String kind, String tracingId)
            implements InvalidateRefreshTokenResultsDTO {

        public InvalidRequest(String message, String tracingId) {
            this(message, "invalid_request", tracingId);
        }
    }

    record SystemError(String message, String kind, String tracingId)
            implements InvalidateRefreshTokenResultsDTO {

        public SystemError(String message, String tracingId) {
            this(message, "system_error", tracingId);
        }
    }
}
