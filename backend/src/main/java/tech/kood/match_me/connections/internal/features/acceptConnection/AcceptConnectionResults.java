package tech.kood.match_me.connections.internal.features.acceptConnection;

import java.io.Serializable;
import org.jspecify.annotations.Nullable;

public sealed interface AcceptConnectionResults extends Serializable
                permits AcceptConnectionResults.Success, AcceptConnectionResults.DoesNotExist,
                AcceptConnectionResults.SystemError, AcceptConnectionResults.InvalidRequest,
                AcceptConnectionResults.AlreadyExists {

        record Success(String requestId, String connectionRequestId, @Nullable String tracingId)
                        implements AcceptConnectionResults {
        }

        record InvalidRequest(String requestId, @Nullable String tracingId)
                        implements AcceptConnectionResults {
        }

        record DoesNotExist(String requestId, String connectionRequestId,
                        @Nullable String tracingId) implements AcceptConnectionResults {
        }

        public record AlreadyExists(String requestId, String connectionRequestId,
                        @Nullable String tracingId) implements AcceptConnectionResults {
        }

        record SystemError(String requestId, String connectionRequestId, @Nullable String tracingId,
                        String errorMessage) implements AcceptConnectionResults {

        }
}
