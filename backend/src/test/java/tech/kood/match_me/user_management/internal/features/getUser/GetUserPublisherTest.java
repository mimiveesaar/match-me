package tech.kood.match_me.user_management.internal.features.getUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jms.core.JmsMessagingTemplate;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByEmailRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByIdRequest;
import tech.kood.match_me.user_management.internal.features.getUser.requests.GetUserByUsernameRequest;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByEmailResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByIdResults;
import tech.kood.match_me.user_management.internal.features.getUser.results.GetUserByUsernameResults;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetUserPublisherTest {
    private JmsMessagingTemplate jmsMessagingTemplate;
    private GetUserPublisher publisher;

    @BeforeEach
    void setUp() {
        jmsMessagingTemplate = mock(JmsMessagingTemplate.class);
        publisher = new GetUserPublisher(jmsMessagingTemplate);
    }

    @Test
    void testPublishById() {
        GetUserByIdRequest request = mock(GetUserByIdRequest.class);
        GetUserByIdResults expected = mock(GetUserByIdResults.class);
        when(jmsMessagingTemplate.convertSendAndReceive(eq(GetUserQueues.GET_USER_BY_ID_QUEUE),
                eq(request), eq(GetUserByIdResults.class))).thenReturn(expected);
        GetUserByIdResults result = publisher.publish(request);
        assertSame(expected, result);
        verify(jmsMessagingTemplate).convertSendAndReceive(GetUserQueues.GET_USER_BY_ID_QUEUE,
                request, GetUserByIdResults.class);
    }

    @Test
    void testPublishByEmail() {
        GetUserByEmailRequest request = mock(GetUserByEmailRequest.class);
        GetUserByEmailResults expected = mock(GetUserByEmailResults.class);
        when(jmsMessagingTemplate.convertSendAndReceive(eq(GetUserQueues.GET_USER_BY_EMAIL_QUEUE),
                eq(request), eq(GetUserByEmailResults.class))).thenReturn(expected);
        GetUserByEmailResults result = publisher.publish(request);
        assertSame(expected, result);
        verify(jmsMessagingTemplate).convertSendAndReceive(GetUserQueues.GET_USER_BY_EMAIL_QUEUE,
                request, GetUserByEmailResults.class);
    }

    @Test
    void testPublishByUsername() {
        GetUserByUsernameRequest request = mock(GetUserByUsernameRequest.class);
        GetUserByUsernameResults expected = mock(GetUserByUsernameResults.class);
        when(jmsMessagingTemplate.convertSendAndReceive(
                eq(GetUserQueues.GET_USER_BY_USERNAME_QUEUE), eq(request),
                eq(GetUserByUsernameResults.class))).thenReturn(expected);
        GetUserByUsernameResults result = publisher.publish(request);
        assertSame(expected, result);
        verify(jmsMessagingTemplate).convertSendAndReceive(GetUserQueues.GET_USER_BY_USERNAME_QUEUE,
                request, GetUserByUsernameResults.class);
    }
}
