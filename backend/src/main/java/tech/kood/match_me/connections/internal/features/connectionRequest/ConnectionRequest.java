package tech.kood.match_me.connections.internal.features.connectionRequest;

import java.io.Serializable;

public record ConnectionRequest(String requestId, String targetUserId, String senderId,
        String tracingId) implements Serializable {
}
