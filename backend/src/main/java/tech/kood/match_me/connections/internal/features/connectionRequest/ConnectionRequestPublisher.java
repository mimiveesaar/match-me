package tech.kood.match_me.connections.internal.features.connectionRequest;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import jakarta.jms.JMSException;

@Component
public class ConnectionRequestPublisher {

    private final JmsMessagingTemplate jmsMessaging;

    public ConnectionRequestPublisher(JmsMessagingTemplate jmsTemplate) {
        this.jmsMessaging = jmsTemplate;
    }

    public ConnectionRequestResults publish(ConnectionRequest request) throws JMSException {
        return jmsMessaging.convertSendAndReceive(
                "tech.kood.match_me.connections.connectionRequest.queue", request,
                ConnectionRequestResults.class);
    }
}
