package tech.kood.match_me.connections.internal.features.connectionRequest;

import java.io.Serializable;
import jakarta.annotation.Nullable;

public record ConnectionRequest(String requestId, String targetUserId, String senderId,
                @Nullable String tracingId) implements Serializable {
}
