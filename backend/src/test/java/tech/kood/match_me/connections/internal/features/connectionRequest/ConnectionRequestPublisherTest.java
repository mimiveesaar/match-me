package tech.kood.match_me.connections.internal.features.connectionRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsMessagingTemplate;

import jakarta.jms.JMSException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionRequestPublisherTest {

    @Mock
    private JmsMessagingTemplate jmsMessagingTemplate;

    private ConnectionRequestPublisher connectionRequestPublisher;

    private static final String QUEUE_NAME =
            "tech.kood.match_me.connections.connectionRequest.queue";

    @BeforeEach
    void setUp() {
        connectionRequestPublisher = new ConnectionRequestPublisher(jmsMessagingTemplate);
    }

    @Test
    void shouldPublishConnectionRequestAndReturnSuccess() throws JMSException {
        // Given
        String requestId = UUID.randomUUID().toString();
        String targetUserId = UUID.randomUUID().toString();
        String senderId = UUID.randomUUID().toString();
        String tracingId = UUID.randomUUID().toString();
        String connectionId = UUID.randomUUID().toString();

        ConnectionRequest request =
                new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
        ConnectionRequestResults.Success expectedResult = new ConnectionRequestResults.Success(
                requestId, connectionId, targetUserId, senderId, tracingId);

        when(jmsMessagingTemplate.convertSendAndReceive(eq(QUEUE_NAME), eq(request),
                eq(ConnectionRequestResults.class))).thenReturn(expectedResult);

        // When
        ConnectionRequestResults result = connectionRequestPublisher.publish(request);

        // Then
        assertNotNull(result);
        assertInstanceOf(ConnectionRequestResults.Success.class, result);

        ConnectionRequestResults.Success successResult = (ConnectionRequestResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertEquals(connectionId, successResult.connectionId());
        assertEquals(targetUserId, successResult.targetUserId());
        assertEquals(senderId, successResult.senderId());
        assertEquals(tracingId, successResult.tracingId());

        verify(jmsMessagingTemplate).convertSendAndReceive(QUEUE_NAME, request,
                ConnectionRequestResults.class);
    }

    @Test
    void shouldPublishConnectionRequestAndReturnFailure() throws JMSException {
        // Given
        String requestId = UUID.randomUUID().toString();
        String targetUserId = UUID.randomUUID().toString();
        String senderId = UUID.randomUUID().toString();
        String tracingId = UUID.randomUUID().toString();
        String errorMessage = "User not found";

        ConnectionRequest request =
                new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
        ConnectionRequestResults.Failure expectedResult =
                new ConnectionRequestResults.Failure(requestId, errorMessage, tracingId);

        when(jmsMessagingTemplate.convertSendAndReceive(eq(QUEUE_NAME), eq(request),
                eq(ConnectionRequestResults.class))).thenReturn(expectedResult);

        // When
        ConnectionRequestResults result = connectionRequestPublisher.publish(request);

        // Then
        assertNotNull(result);
        assertInstanceOf(ConnectionRequestResults.Failure.class, result);

        ConnectionRequestResults.Failure failureResult = (ConnectionRequestResults.Failure) result;
        assertEquals(requestId, failureResult.requestId());
        assertEquals(errorMessage, failureResult.message());
        assertEquals(tracingId, failureResult.tracingId());

        verify(jmsMessagingTemplate).convertSendAndReceive(QUEUE_NAME, request,
                ConnectionRequestResults.class);
    }

    @Test
    void shouldPublishConnectionRequestAndReturnSystemError() throws JMSException {
        // Given
        String requestId = UUID.randomUUID().toString();
        String targetUserId = UUID.randomUUID().toString();
        String senderId = UUID.randomUUID().toString();
        String tracingId = UUID.randomUUID().toString();
        String errorMessage = "Database connection failed";

        ConnectionRequest request =
                new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
        ConnectionRequestResults.SystemError expectedResult =
                new ConnectionRequestResults.SystemError(requestId, errorMessage, tracingId);

        when(jmsMessagingTemplate.convertSendAndReceive(eq(QUEUE_NAME), eq(request),
                eq(ConnectionRequestResults.class))).thenReturn(expectedResult);

        // When
        ConnectionRequestResults result = connectionRequestPublisher.publish(request);

        // Then
        assertNotNull(result);
        assertInstanceOf(ConnectionRequestResults.SystemError.class, result);

        ConnectionRequestResults.SystemError systemErrorResult =
                (ConnectionRequestResults.SystemError) result;
        assertEquals(requestId, systemErrorResult.requestId());
        assertEquals(errorMessage, systemErrorResult.message());
        assertEquals(tracingId, systemErrorResult.tracingId());

        verify(jmsMessagingTemplate).convertSendAndReceive(QUEUE_NAME, request,
                ConnectionRequestResults.class);
    }

    @Test
    void shouldPropagateJMSExceptionWhenJmsTemplateThrows() throws JMSException {
        // Given
        String requestId = UUID.randomUUID().toString();
        String targetUserId = UUID.randomUUID().toString();
        String senderId = UUID.randomUUID().toString();
        String tracingId = UUID.randomUUID().toString();

        ConnectionRequest request =
                new ConnectionRequest(requestId, targetUserId, senderId, tracingId);
        JMSException jmsException = new JMSException("JMS connection failed");

        when(jmsMessagingTemplate.convertSendAndReceive(eq(QUEUE_NAME), eq(request),
                eq(ConnectionRequestResults.class))).thenThrow(jmsException);

        // When & Then
        JMSException exception = assertThrows(JMSException.class, () -> {
            connectionRequestPublisher.publish(request);
        });

        assertEquals("JMS connection failed", exception.getMessage());
        verify(jmsMessagingTemplate).convertSendAndReceive(QUEUE_NAME, request,
                ConnectionRequestResults.class);
    }

    @Test
    void shouldUseCorrectQueueName() throws JMSException {
        // Given
        ConnectionRequest request = new ConnectionRequest("test-id", "target", "sender", "trace");
        ConnectionRequestResults.Success mockResult = new ConnectionRequestResults.Success(
                "test-id", "conn-id", "target", "sender", "trace");

        when(jmsMessagingTemplate.convertSendAndReceive(anyString(), any(ConnectionRequest.class),
                eq(ConnectionRequestResults.class))).thenReturn(mockResult);

        // When
        connectionRequestPublisher.publish(request);

        // Then
        verify(jmsMessagingTemplate).convertSendAndReceive(
                eq("tech.kood.match_me.connections.connectionRequest.queue"), eq(request),
                eq(ConnectionRequestResults.class));
    }

    @Test
    void shouldHandleNullResponse() throws JMSException {
        // Given
        ConnectionRequest request = new ConnectionRequest("test-id", "target", "sender", "trace");

        when(jmsMessagingTemplate.convertSendAndReceive(eq(QUEUE_NAME), eq(request),
                eq(ConnectionRequestResults.class))).thenReturn(null);

        // When
        ConnectionRequestResults result = connectionRequestPublisher.publish(request);

        // Then
        assertNull(result);
        verify(jmsMessagingTemplate).convertSendAndReceive(QUEUE_NAME, request,
                ConnectionRequestResults.class);
    }
}
