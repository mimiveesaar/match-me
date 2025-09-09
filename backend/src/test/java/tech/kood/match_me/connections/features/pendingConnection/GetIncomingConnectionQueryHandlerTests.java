package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionQueryHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionsRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionsResults;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntityFactory;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetIncomingConnectionQueryHandlerTests extends ConnectionsTestBase {

    @Autowired
    private GetIncomingConnectionQueryHandler queryHandler;

    @Autowired
    private PendingConnectionRepository repository;

    @Autowired
    private PendingConnectionEntityFactory pendingConnectionEntityFactory;

    @Test
    void testHandleValidRequest_ReturnsIncomingConnections() throws CheckedConstraintViolationException {
        // Arrange
        var targetId = new UserIdDTO(UUID.randomUUID());
        var senderId1 = new UserIdDTO(UUID.randomUUID());
        var senderId2 = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        // Create test data - two pending connections with the same target (incoming)
        var pendingConnection1 = pendingConnectionEntityFactory.create(UUID.randomUUID(), senderId1.value(), targetId.value(), Instant.now());
        var pendingConnection2 = pendingConnectionEntityFactory.create(UUID.randomUUID(), senderId2.value(), targetId.value(), Instant.now());
        repository.save(pendingConnection1);
        repository.save(pendingConnection2);

        // Act
        var request = new GetIncomingConnectionsRequest(requestId, targetId, tracingId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetIncomingConnectionsResults.Success.class, result);
        var successResult = (GetIncomingConnectionsResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertEquals(tracingId, successResult.tracingId());
        assertNotNull(successResult.incomingRequests());
        assertEquals(2, successResult.incomingRequests().size());
        
        // Verify that the correct connections are returned
        var incomingRequests = successResult.incomingRequests();
        assertTrue(incomingRequests.stream().anyMatch(connection ->
            connection.senderId().value().equals(senderId1.value()) &&
            connection.targetId().value().equals(targetId.value())
        ));
        assertTrue(incomingRequests.stream().anyMatch(connection ->
            connection.senderId().value().equals(senderId2.value()) &&
            connection.targetId().value().equals(targetId.value())
        ));
    }

    @Test
    void testHandleValidRequest_NoIncomingConnections_ReturnsEmptyList() {
        // Arrange
        var targetId = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        // Act
        var request = new GetIncomingConnectionsRequest(requestId, targetId, tracingId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetIncomingConnectionsResults.Success.class, result);
        var successResult = (GetIncomingConnectionsResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertEquals(tracingId, successResult.tracingId());
        assertNotNull(successResult.incomingRequests());
        assertTrue(successResult.incomingRequests().isEmpty());
    }

    @Test
    void testHandleInvalidRequest_ReturnsInvalidRequest() {
        // Arrange
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        // Act - create request with null userId which should fail validation
        var request = new GetIncomingConnectionsRequest(requestId, null, tracingId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetIncomingConnectionsResults.InvalidRequest.class, result);
        var invalidRequestResult = (GetIncomingConnectionsResults.InvalidRequest) result;
        assertEquals(requestId, invalidRequestResult.requestId());
        assertEquals(tracingId, invalidRequestResult.tracingId());
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
    }
}
