package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionResults;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsQueryHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsResults;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionCommandHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.acceptRequest.api.AcceptConnectionResults;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
public class AcceptConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private AcceptConnectionCommandHandler acceptConnectionCommandHandler;

    @Autowired
    private PendingConnectionRepository pendingConnectionRepository;

    @Autowired
    private PendingConnectionEntityMother pendingConnectionEntityMother;

    @Autowired
    private GetAcceptedConnectionsQueryHandler getAcceptedConnectionsQueryHandler;

    @Autowired
    private CreateAcceptedConnectionCommandHandler createAcceptedConnectionHandler;

    @Test
    void testHandleValidRequest_Success() {
        var pendingConnection = pendingConnectionEntityMother.createPendingConnectionEntity();
        pendingConnectionRepository.save(pendingConnection);

        var connectionId = new ConnectionIdDTO(pendingConnection.getId());
        var targetId = new UserIdDTO(pendingConnection.getTargetId());
        var senderId = new UserIdDTO(pendingConnection.getSenderId());

        var request = new AcceptConnectionRequest(connectionId, targetId);

        var result = acceptConnectionCommandHandler.handle(request);

        assertInstanceOf(AcceptConnectionResults.Success.class, result);

        assertTrue(pendingConnectionRepository.findById(pendingConnection.getId()).isEmpty());

        var getAcceptedConnectionsRequest = new GetAcceptedConnectionsRequest(new UserIdDTO(targetId.value()));
        var acceptedConnectionsResults = getAcceptedConnectionsQueryHandler.handle(getAcceptedConnectionsRequest);

        assertInstanceOf(GetAcceptedConnectionsResults.Success.class, acceptedConnectionsResults);
        var successResult = (GetAcceptedConnectionsResults.Success) acceptedConnectionsResults;
        var acceptedConnections = successResult.acceptedConnections();
        assertFalse(acceptedConnections.isEmpty());
        var acceptedConnection = acceptedConnections.getFirst();
        assertEquals(targetId.value(), acceptedConnection.acceptedByUserId().value());
        assertEquals(senderId.value(), acceptedConnection.acceptedUserId().value());
    }

    @Test
    void testHandleInvalidRequest_NotFound() {
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());
        var acceptedByUserId = new UserIdDTO(UUID.randomUUID());

        var request = new AcceptConnectionRequest(connectionId, acceptedByUserId);

        var result = acceptConnectionCommandHandler.handle(request);

        assertInstanceOf(AcceptConnectionResults.NotFound.class, result);
    }

    @Test
    void testHandleInvalidRequest_NotTargetUser() {
        var pendingConnection = pendingConnectionEntityMother.createPendingConnectionEntity();
        pendingConnectionRepository.save(pendingConnection);

        var connectionId = new ConnectionIdDTO(pendingConnection.getId());
        var invalidTargetId = new UserIdDTO(UUID.randomUUID()); // A different user than the target

        var request = new AcceptConnectionRequest(connectionId, invalidTargetId);

        var result = acceptConnectionCommandHandler.handle(request);

        assertInstanceOf(AcceptConnectionResults.InvalidRequest.class, result);
        var invalidRequestResult = (AcceptConnectionResults.InvalidRequest) result;
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
        assertEquals("acceptedByUserId", invalidRequestResult.error().errors().get(0).field());
    }

    @Test
    void testHandleInvalidRequest_NullConnectionId() {
        var acceptedByUserId = new UserIdDTO(UUID.randomUUID());

        var request = new AcceptConnectionRequest(null, acceptedByUserId);

        var result = acceptConnectionCommandHandler.handle(request);

        assertInstanceOf(AcceptConnectionResults.InvalidRequest.class, result);
        var invalidRequestResult = (AcceptConnectionResults.InvalidRequest) result;
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
    }

    @Test
    void testHandleInvalidRequest_NullAcceptedByUser() {
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());

        var request = new AcceptConnectionRequest(connectionId, null);

        var result = acceptConnectionCommandHandler.handle(request);

        assertInstanceOf(AcceptConnectionResults.InvalidRequest.class, result);
        var invalidRequestResult = (AcceptConnectionResults.InvalidRequest) result;
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
    }

    @Test
    void testHandleRequest_AlreadyAccepted() {

        var pendingConnection = pendingConnectionEntityMother.createPendingConnectionEntity();
        pendingConnectionRepository.save(pendingConnection);

        var connectionId = new ConnectionIdDTO(pendingConnection.getId());
        var targetId = new UserIdDTO(pendingConnection.getTargetId());
        var senderId = new UserIdDTO(pendingConnection.getSenderId());

        // Create an accepted connection between the same users
        var createAcceptedConnectionRequest = new CreateAcceptedConnectionRequest(connectionId, targetId, senderId);
        var createResult = createAcceptedConnectionHandler.handle(createAcceptedConnectionRequest);
        assertInstanceOf(CreateAcceptedConnectionResults.Success.class, createResult);

        // Now, attempt to accept the same pending connection
        var request = new AcceptConnectionRequest(connectionId, targetId);

        var result = acceptConnectionCommandHandler.handle(request);

        assertInstanceOf(AcceptConnectionResults.AlreadyAccepted.class, result);
    }
}
