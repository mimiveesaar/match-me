package tech.kood.match_me.connections.features.acceptConnection;

import java.io.Serializable;

public record AcceptConnectionRequest(String requestId, String connectionRequestId, String tracingId)
        implements Serializable {
}
