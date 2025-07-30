package tech.kood.match_me.user_management.internal.features.getUser;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;

@Component
public class GetUserPublisher {
    private final JmsMessagingTemplate jmsMessaging;

    public GetUserPublisher(JmsMessagingTemplate jmsTemplate) {
        this.jmsMessaging = jmsTemplate;
    }

    public GetUserByIdResults publish(GetUserByIdRequest request) {
        return jmsMessaging.convertSendAndReceive(GetUserQueues.GET_USER_BY_ID_QUEUE, request,
                GetUserByIdResults.class);
    }

    public GetUserByEmailResults publish(GetUserByEmailRequest request) {
        return jmsMessaging.convertSendAndReceive(GetUserQueues.GET_USER_BY_EMAIL_QUEUE, request,
                GetUserByEmailResults.class);

    }

    public GetUserByUsernameResults publish(GetUserByUsernameRequest request) {
        return jmsMessaging.convertSendAndReceive(GetUserQueues.GET_USER_BY_USERNAME_QUEUE, request,
                GetUserByUsernameResults.class);
    }
}
