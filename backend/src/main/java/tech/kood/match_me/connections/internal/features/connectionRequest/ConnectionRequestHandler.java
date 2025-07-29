package tech.kood.match_me.connections.internal.features.connectionRequest;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ConnectionRequestHandler {

    @JmsListener(destination = "tech.kood.match_me.connections.connectionRequest.queue",
            concurrency = "1-1")
    public ConnectionRequestResults handle(ConnectionRequest request) {
        // Logic to handle the connection request
        // This is a placeholder for actual business logic
        String requestId = request.requestId();
        String targetUserId = request.targetUserId();
        String senderId = request.senderId();
        String tracingId = request.tracingId();

        // Simulate processing and return a success result
        return new ConnectionRequestResults.Success(requestId, "connection-id", targetUserId,
                senderId, tracingId);
    }
}
