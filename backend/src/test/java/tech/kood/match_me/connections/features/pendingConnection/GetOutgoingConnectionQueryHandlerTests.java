package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api.GetOutgoingConnectionQueryHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api.GetOutgoingConnectionsRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api.GetOutgoingConnectionsResults;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntityFactory;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetOutgoingConnectionQueryHandlerTests extends ConnectionsTestBase {

    @Autowired
    private GetOutgoingConnectionQueryHandler queryHandler;

    @Autowired
    private PendingConnectionRepository repository;

    @Autowired
    private PendingConnectionEntityFactory pendingConnectionEntityFactory;

    @Test
    void testHandleValidRequest_ReturnsOutgoingConnections() throws CheckedConstraintViolationException {
        // Arrange
        var senderId = new UserIdDTO(UUID.randomUUID());
        var targetId1 = new UserIdDTO(UUID.randomUUID());
        var targetId2 = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        // Create test data - two pending connections with the same sender
        var pendingConnection1 = pendingConnectionEntityFactory.create(UUID.randomUUID(), senderId.value(), targetId1.value(), Instant.now());
        var pendingConnection2 = pendingConnectionEntityFactory.create(UUID.randomUUID(), senderId.value(), targetId2.value(), Instant.now());
        repository.save(pendingConnection1);
        repository.save(pendingConnection2);

        // Act
        var request = new GetOutgoingConnectionsRequest(requestId, senderId, tracingId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetOutgoingConnectionsResults.Success.class, result);
        var successResult = (GetOutgoingConnectionsResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertEquals(tracingId, successResult.tracingId());
        assertNotNull(successResult.outgoingRequests());
        assertEquals(2, successResult.outgoingRequests().size());
        
        // Verify that the correct connections are returned
        var outgoingRequests = successResult.outgoingRequests();
        assertTrue(outgoingRequests.stream().anyMatch(connection ->
            connection.senderId().value().equals(senderId.value()) &&
            connection.targetId().value().equals(targetId1.value())
        ));
    }

    @Test
    void testHandleValidRequest_NoOutgoingConnections_ReturnsEmptyList() {
        // Arrange
        var senderId = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        // Act
        var request = new GetOutgoingConnectionsRequest(requestId, senderId, tracingId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetOutgoingConnectionsResults.Success.class, result);
        var successResult = (GetOutgoingConnectionsResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertEquals(tracingId, successResult.tracingId());
        assertNotNull(successResult.outgoingRequests());
        assertTrue(successResult.outgoingRequests().isEmpty());
    }

    @Test
    void testHandleInvalidRequest_ReturnsInvalidRequest() {
        // Arrange
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        // Act - create request with null userId which should fail validation
        var request = new GetOutgoingConnectionsRequest(requestId, null, tracingId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetOutgoingConnectionsResults.InvalidRequest.class, result);
        var invalidRequestResult = (GetOutgoingConnectionsResults.InvalidRequest) result;
        assertEquals(requestId, invalidRequestResult.requestId());
        assertEquals(tracingId, invalidRequestResult.tracingId());
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
    }
}
