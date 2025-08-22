package tech.kood.match_me.connections.features.connectionRequest;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public sealed interface ConnectionRequestResults extends Serializable
                permits ConnectionRequestResults.Success, ConnectionRequestResults.AlreadyExists,
                ConnectionRequestResults.SystemError, ConnectionRequestResults.InvalidRequest {

        public record Success(@NotNull UUID requestId, String connectionId, String targetUserId,
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
