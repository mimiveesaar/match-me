package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.pendingConnection.actions.GetOutgoingConnectionRequests;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntityFactory;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(transactionManager = "connectionsTransactionManager")
public class GetOutgoingConnectionQueryHandlerTests extends ConnectionsTestBase {

    @Autowired
    private GetOutgoingConnectionRequests.Handler getOutgoingConnectionRequestsHandler;

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

        // Create test data - two pending connections with the same sender
        var pendingConnection1 = pendingConnectionEntityFactory.create(UUID.randomUUID(), senderId.value(), targetId1.value(), Instant.now());
        var pendingConnection2 = pendingConnectionEntityFactory.create(UUID.randomUUID(), senderId.value(), targetId2.value(), Instant.now());
        repository.save(pendingConnection1);
        repository.save(pendingConnection2);

        // Act
        var request = new GetOutgoingConnectionRequests.Request(senderId);
        var result = getOutgoingConnectionRequestsHandler.handle(request);

        // Assert
        assertInstanceOf(GetOutgoingConnectionRequests.Result.Success.class, result);
        var successResult = (GetOutgoingConnectionRequests.Result.Success) result;
        assertNotNull(successResult.outgoingRequests());
        assertEquals(2, successResult.outgoingRequests().size());
        
        // Verify that the correct connections are returned
        var outgoingRequests = successResult.outgoingRequests();
        assertTrue(outgoingRequests.stream().anyMatch(connection ->
            connection.senderId().value().equals(senderId.value()) &&
            connection.targetId().value().equals(targetId1.value())
        ));
        assertTrue(outgoingRequests.stream().anyMatch(connection ->
            connection.senderId().value().equals(senderId.value()) &&
            connection.targetId().value().equals(targetId2.value())
        ));
    }

    @Test
    void testHandleValidRequest_NoOutgoingConnections_ReturnsEmptyList() {
        // Arrange
        var senderId = new UserIdDTO(UUID.randomUUID());

        // Act
        var request = new GetOutgoingConnectionRequests.Request(senderId);
        var result = getOutgoingConnectionRequestsHandler.handle(request);

        // Assert
        assertInstanceOf(GetOutgoingConnectionRequests.Result.Success.class, result);
        var successResult = (GetOutgoingConnectionRequests.Result.Success) result;
        assertNotNull(successResult.outgoingRequests());
        assertTrue(successResult.outgoingRequests().isEmpty());
    }

    @Test
    void testHandleInvalidRequest_ReturnsInvalidRequest() {
        // Act - create request with null userId which should fail validation
        var request = new GetOutgoingConnectionRequests.Request(null);
        var result = getOutgoingConnectionRequestsHandler.handle(request);

        // Assert
        assertInstanceOf(GetOutgoingConnectionRequests.Result.InvalidRequest.class, result);
        var invalidRequestResult = (GetOutgoingConnectionRequests.Result.InvalidRequest) result;
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
    }
}