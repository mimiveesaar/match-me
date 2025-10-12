package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.pendingConnection.actions.GetIncomingConnectionRequests;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntityFactory;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(transactionManager = "connectionsTransactionManager")
public class GetIncomingConnectionQueryHandlerTests extends ConnectionsTestBase {

    @Autowired
    private GetIncomingConnectionRequests.Handler GetIncomingConnections;

    @Autowired
    private PendingConnectionRepository repository;

    @Autowired
    private PendingConnectionEntityFactory pendingConnectionEntityFactory;

    @Test
    void testHandleValidRequest_ReturnsIncomingConnections()
            throws CheckedConstraintViolationException {
        // Arrange
        var targetId = new UserIdDTO(UUID.randomUUID());
        var senderId1 = new UserIdDTO(UUID.randomUUID());
        var senderId2 = new UserIdDTO(UUID.randomUUID());

        // Create test data - two pending connections with the same target (incoming)
        var pendingConnection1 = pendingConnectionEntityFactory.create(UUID.randomUUID(),
                senderId1.value(), targetId.value(), Instant.now());
        var pendingConnection2 = pendingConnectionEntityFactory.create(UUID.randomUUID(),
                senderId2.value(), targetId.value(), Instant.now());
        repository.save(pendingConnection1);
        repository.save(pendingConnection2);

        // Act
        var request = new GetIncomingConnectionRequests.Request(targetId);
        var result = GetIncomingConnections.handle(request);

        // Assert
        assertInstanceOf(GetIncomingConnectionRequests.Result.Success.class, result);
        var successResult = (GetIncomingConnectionRequests.Result.Success) result;
        assertNotNull(successResult.incomingRequests());
        assertEquals(2, successResult.incomingRequests().size());

        // Verify that the correct connections are returned
        var incomingRequests = successResult.incomingRequests();
        assertTrue(incomingRequests.stream()
                .anyMatch(connection -> connection.senderId().value().equals(senderId1.value())
                        && connection.targetId().value().equals(targetId.value())));
        assertTrue(incomingRequests.stream()
                .anyMatch(connection -> connection.senderId().value().equals(senderId2.value())
                        && connection.targetId().value().equals(targetId.value())));
    }

    @Test
    void testHandleValidRequest_NoIncomingConnections_ReturnsEmptyList() {
        // Arrange
        var targetId = new UserIdDTO(UUID.randomUUID());

        // Act
        var request = new GetIncomingConnectionRequests.Request(targetId);
        var result = GetIncomingConnections.handle(request);

        // Assert
        assertInstanceOf(GetIncomingConnectionRequests.Result.Success.class, result);
        var successResult = (GetIncomingConnectionRequests.Result.Success) result;
        assertNotNull(successResult.incomingRequests());
        assertTrue(successResult.incomingRequests().isEmpty());
    }

    @Test
    void testHandleInvalidRequest_ReturnsInvalidRequest() {
        // Act - create request with null userId which should fail validation
        var request = new GetIncomingConnectionRequests.Request(null);
        var result = GetIncomingConnections.handle(request);

        // Assert
        assertInstanceOf(GetIncomingConnectionRequests.Result.InvalidRequest.class, result);
        var invalidRequestResult = (GetIncomingConnectionRequests.Result.InvalidRequest) result;
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
    }
}
