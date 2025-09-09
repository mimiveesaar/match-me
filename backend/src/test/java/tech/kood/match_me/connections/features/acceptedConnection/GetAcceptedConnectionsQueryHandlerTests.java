package tech.kood.match_me.connections.features.acceptedConnection;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsQueryHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsResults;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntityFactory;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetAcceptedConnectionsQueryHandlerTests extends ConnectionsTestBase {

    @Autowired
    private GetAcceptedConnectionsQueryHandler queryHandler;

    @Autowired
    private AcceptedConnectionRepository repository;

    @Autowired
    private AcceptedConnectionEntityFactory acceptedConnectionEntityFactory;

    @Test
    void testHandleValidRequest_ReturnsAcceptedConnections() throws CheckedConstraintViolationException {
        // Arrange
        var userId = new UserIdDTO(UUID.randomUUID());
        var otherUser1Id = new UserIdDTO(UUID.randomUUID());
        var otherUser2Id = new UserIdDTO(UUID.randomUUID());

        // Create test data - connections where user is both accepter and accepted
        var connection1 = acceptedConnectionEntityFactory.create(
            UUID.randomUUID(), userId.value(), otherUser1Id.value(), Instant.now());
        var connection2 = acceptedConnectionEntityFactory.create(
            UUID.randomUUID(), otherUser2Id.value(), userId.value(), Instant.now());
        
        repository.save(connection1);
        repository.save(connection2);

        // Act
        var request = new GetAcceptedConnectionsRequest(userId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetAcceptedConnectionsResults.Success.class, result);
        var successResult = (GetAcceptedConnectionsResults.Success) result;
        assertNotNull(successResult.acceptedConnections());
        assertEquals(2, successResult.acceptedConnections().size());
        
        // Verify that the correct connections are returned
        var connections = successResult.acceptedConnections();
        assertTrue(connections.stream().anyMatch(connection ->
            (connection.acceptedByUser().value().equals(userId.value()) && 
             connection.acceptedUser().value().equals(otherUser1Id.value())) ||
            (connection.acceptedByUser().value().equals(otherUser2Id.value()) && 
             connection.acceptedUser().value().equals(userId.value()))
        ));
    }

    @Test
    void testHandleValidRequest_NoAcceptedConnections_ReturnsEmptyList() {
        // Arrange
        var userId = new UserIdDTO(UUID.randomUUID());

        // Act
        var request = new GetAcceptedConnectionsRequest(userId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetAcceptedConnectionsResults.Success.class, result);
        var successResult = (GetAcceptedConnectionsResults.Success) result;
        assertNotNull(successResult.acceptedConnections());
        assertTrue(successResult.acceptedConnections().isEmpty());
    }

    @Test
    void testHandleInvalidRequest_NullUserId_ReturnsInvalidRequest() {

        // Act - create request with null userId which should fail validation
        var request = new GetAcceptedConnectionsRequest(null);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetAcceptedConnectionsResults.InvalidRequest.class, result);
        var invalidRequestResult = (GetAcceptedConnectionsResults.InvalidRequest) result;
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
    }

    @Test
    void testHandleValidRequest_OnlyConnectionsAsAccepter_ReturnsCorrectConnections() throws CheckedConstraintViolationException {
        // Arrange
        var userId = new UserIdDTO(UUID.randomUUID());
        var otherUserId = new UserIdDTO(UUID.randomUUID());

        // Create test data - connection where user is only the accepter
        var connection = acceptedConnectionEntityFactory.create(
            UUID.randomUUID(), userId.value(), otherUserId.value(), Instant.now());
        repository.save(connection);

        // Act
        var request = new GetAcceptedConnectionsRequest(userId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetAcceptedConnectionsResults.Success.class, result);
        var successResult = (GetAcceptedConnectionsResults.Success) result;
        assertEquals(1, successResult.acceptedConnections().size());
        
        var connection1 = successResult.acceptedConnections().get(0);
        assertEquals(userId.value(), connection1.acceptedByUser().value());
        assertEquals(otherUserId.value(), connection1.acceptedUser().value());
    }

    @Test
    void testHandleValidRequest_OnlyConnectionsAsAccepted_ReturnsCorrectConnections() throws CheckedConstraintViolationException {
        // Arrange
        var userId = new UserIdDTO(UUID.randomUUID());
        var otherUserId = new UserIdDTO(UUID.randomUUID());

        // Create test data - connection where user is only the accepted one
        var connection = acceptedConnectionEntityFactory.create(
            UUID.randomUUID(), otherUserId.value(), userId.value(), Instant.now());
        repository.save(connection);

        // Act
        var request = new GetAcceptedConnectionsRequest(userId);
        var result = queryHandler.handle(request);

        // Assert
        assertInstanceOf(GetAcceptedConnectionsResults.Success.class, result);
        var successResult = (GetAcceptedConnectionsResults.Success) result;
        assertEquals(1, successResult.acceptedConnections().size());
        
        var connection1 = successResult.acceptedConnections().get(0);
        assertEquals(otherUserId.value(), connection1.acceptedByUser().value());
        assertEquals(userId.value(), connection1.acceptedUser().value());
    }
}
