package tech.kood.match_me.connections.internal.features.connectionRequest;

import java.io.Serializable;

public sealed interface ConnectionRequestResults extends Serializable
        permits ConnectionRequestResults.Success, ConnectionRequestResults.Failure,
        ConnectionRequestResults.SystemError {

    public record Success(String requestId, String connectionId, String targetUserId,
            String senderId, String tracingId) implements ConnectionRequestResults {
    }

    public record Failure(String requestId, String message, String tracingId)
            implements ConnectionRequestResults {
    }

    public record SystemError(String requestId, String message, String tracingId)
            implements ConnectionRequestResults {
    }
}
