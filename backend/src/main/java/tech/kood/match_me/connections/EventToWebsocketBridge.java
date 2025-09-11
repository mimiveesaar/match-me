package tech.kood.match_me.connections;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.AcceptedConnectionCreatedEvent;

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
        //Connection was accepted, we need to notify both parties.
        //We only send the notification if the user is connected to the websocket.
        var sender = event.acceptedConnectionDTO().acceptedUser();
        var receiver = event.acceptedConnectionDTO().acceptedByUser();

        SimpUser senderRegistryUser = userRegistry.getUser(sender.value().toString());
        SimpUser receiverRegistryUser = userRegistry.getUser(receiver.value().toString());

        if (senderRegistryUser != null) {
            messaging.convertAndSendToUser(sender.value().toString(), "/queue/connections/accepted", event.acceptedConnectionDTO());
        }

        if (receiverRegistryUser != null) {
            messaging.convertAndSendToUser(receiver.value().toString(), "/queue/connections/accepted", event.acceptedConnectionDTO());
        }
    }
}
