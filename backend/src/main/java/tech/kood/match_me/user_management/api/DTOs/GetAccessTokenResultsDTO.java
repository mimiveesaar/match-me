package tech.kood.match_me.user_management.api.DTOs;

public sealed interface GetAccessTokenResultsDTO {

    record Success(String jwt, String kind, String tracingId) implements GetAccessTokenResultsDTO {
        public Success(String jwt, String tracingId) {
            this(jwt, "success", tracingId);
        }
    }

    record InvalidToken(String token, String kind, String tracingId)
            implements GetAccessTokenResultsDTO {
        public InvalidToken(String token, String tracingId) {
            this(token, "invalid_token", tracingId);
        }
    }

    record InvalidRequest(String message, String kind, String tracingId)
            implements GetAccessTokenResultsDTO {
        public InvalidRequest(String message, String tracingId) {
            this(message, "invalid_request", tracingId);
        }
    }

    record SystemError(String message, String kind, String tracingId)
            implements GetAccessTokenResultsDTO {
        public SystemError(String message, String tracingId) {
            this(message, "system_error", tracingId);
        }
    }
}
