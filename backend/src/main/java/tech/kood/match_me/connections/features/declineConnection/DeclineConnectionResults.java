package tech.kood.match_me.connections.features.declineConnection;

import jakarta.annotation.Nullable;

public sealed interface DeclineConnectionResults permits DeclineConnectionResults.Success,
        DeclineConnectionResults.NotFound, DeclineConnectionResults.InvalidRequest,
        DeclineConnectionResults.AlreadyExists, DeclineConnectionResults.SystemError {

    record Success(String requestId, String connectionId, String targetUserId, String senderId,
            @Nullable String tracingId) implements DeclineConnectionResults {
    }

    record NotFound(String requestId, @Nullable String tracingId)
            implements DeclineConnectionResults {
    }

    record AlreadyExists(String requestId, String connectionId, @Nullable String tracingId)
            implements DeclineConnectionResults {
    }

    record InvalidRequest(String requestId, @Nullable String tracingId)
            implements DeclineConnectionResults {
    }

    record SystemError(String requestId, String errorMessage, @Nullable String tracingId)
            implements DeclineConnectionResults {
    }
}
