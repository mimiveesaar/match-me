package tech.kood.match_me.connections.features.declineConnection;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record DeclineConnectionRequest(@Nonnull String requestId, String connectionId,
                @Nullable String tracingId) {

        public DeclineConnectionRequest {
                if (requestId == null || connectionId == null) {
                        throw new IllegalArgumentException(
                                        "Request ID and Connection ID must not be null");
                }
        }

}
