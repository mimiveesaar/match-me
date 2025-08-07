package tech.kood.match_me.connections.internal.features.connectionRequest;

import java.io.Serializable;
import jakarta.annotation.Nullable;

public sealed interface ConnectionRequestResults extends Serializable
                permits ConnectionRequestResults.Success, ConnectionRequestResults.AlreadyExists,
                ConnectionRequestResults.SystemError, ConnectionRequestResults.InvalidRequest {

        public record Success(String requestId, String connectionId, String targetUserId,
                        String senderId, @Nullable String tracingId)
                        implements ConnectionRequestResults {
        }

        public record AlreadyExists(String requestId,
                        @Nullable String tracingId) implements ConnectionRequestResults {
        }

        public record InvalidRequest(String requestId,  @Nullable String tracingId)
                        implements ConnectionRequestResults {
        }

        public record SystemError(String requestId, String message, @Nullable String tracingId)
                        implements ConnectionRequestResults {
        }
}
