package tech.kood.match_me.connections;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.AcceptedConnectionCreatedEvent;
import tech.kood.match_me.connections.features.pendingConnection.actions.createRequest.api.ConnectionRequestCreatedEvent;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.ConnectionRequestDeclinedEvent;
import tech.kood.match_me.connections.features.pendingConnection.actions.declineRequest.api.ConnectionRequestUndoEvent;

@Controller
public class EventToWebsocketBridge {

    private final SimpMessagingTemplate messaging;
    private final SimpUserRegistry userRegistry;

    public EventToWebsocketBridge(SimpMessagingTemplate messaging, SimpUserRegistry userRegistry) {
        this.messaging = messaging;
        this.userRegistry = userRegistry;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onConnectionAccepted(AcceptedConnectionCreatedEvent event) {
        var sender = event.acceptedConnectionDTO().acceptedUserId();
        var receiver = event.acceptedConnectionDTO().acceptedByUserId();

        SimpUser senderRegistryUser = userRegistry.getUser(sender.value().toString());
        SimpUser receiverRegistryUser = userRegistry.getUser(receiver.value().toString());

        if (senderRegistryUser != null) {
            messaging.convertAndSendToUser(sender.value().toString(), "/queue/connections/accepted", event.acceptedConnectionDTO());
        }

        if (receiverRegistryUser != null) {
            messaging.convertAndSendToUser(receiver.value().toString(), "/queue/connections/accepted", event.acceptedConnectionDTO());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onConnectionRequestCreated(ConnectionRequestCreatedEvent event) {
        var receiverId = event.targetId();
        var senderId = event.senderId();

        SimpUser receiverRegistryUser = userRegistry.getUser(receiverId.value().toString());
        SimpUser senderRegistryUser = userRegistry.getUser(senderId.value().toString());

        if (receiverRegistryUser != null) {
            messaging.convertAndSendToUser(receiverId.value().toString(), "/queue/connections/requests", event);
        }

        if (senderRegistryUser != null) {
            messaging.convertAndSendToUser(senderId.value().toString(), "/queue/connections/requests", event);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onConnectionRequestUndo(ConnectionRequestUndoEvent event) {
        var receiverId = event.targetId();
        var senderId = event.senderId();
        SimpUser receiverRegistryUser = userRegistry.getUser(receiverId.value().toString());
        SimpUser senderRegistryUser = userRegistry.getUser(senderId.value().toString());

        if (receiverRegistryUser != null) {
            messaging.convertAndSendToUser(receiverId.value().toString(), "/queue/connections/requests/undo", event);
        }

        if (senderRegistryUser != null) {
            messaging.convertAndSendToUser(senderId.value().toString(), "/queue/connections/requests/undo", event);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onConnectionRequestDeclined(ConnectionRequestDeclinedEvent event) {
        var receiverId = event.targetId();
        var senderId = event.senderId();
        SimpUser receiverRegistryUser = userRegistry.getUser(receiverId.value().toString());
        SimpUser senderRegistryUser = userRegistry.getUser(senderId.value().toString());

        if (receiverRegistryUser != null) {
            messaging.convertAndSendToUser(receiverId.value().toString(), "/queue/connections/requests/declined", event);
        }

        if (senderRegistryUser != null) {
            messaging.convertAndSendToUser(senderId.value().toString(), "/queue/connections/requests/declined", event);
        }
    }
}