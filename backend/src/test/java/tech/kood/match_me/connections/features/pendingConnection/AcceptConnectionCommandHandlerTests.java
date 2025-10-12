package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.CreateAcceptedConnection;
import tech.kood.match_me.connections.features.acceptedConnection.actions.GetAcceptedConnections;
import tech.kood.match_me.connections.features.pendingConnection.actions.AcceptConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(transactionManager = "connectionsTransactionManager")
public class AcceptConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private AcceptConnectionRequest.Handler acceptConnectionHandler;

    @Autowired
    private PendingConnectionRepository pendingConnectionRepository;

    @Autowired
    private PendingConnectionEntityMother pendingConnectionEntityMother;

    @Autowired
    private GetAcceptedConnections.Handler getAcceptedConnectionsHandler;

    @Autowired
    private CreateAcceptedConnection.Handler createAcceptedConnectionHandler;

    @Test
    void testHandleValidRequest_Success() {
        var pendingConnection = pendingConnectionEntityMother.createPendingConnectionEntity();
        pendingConnectionRepository.save(pendingConnection);

        var connectionId = new ConnectionIdDTO(pendingConnection.getId());
        var targetId = new UserIdDTO(pendingConnection.getTargetId());
        var senderId = new UserIdDTO(pendingConnection.getSenderId());

        var request = new AcceptConnectionRequest.Request(connectionId, targetId);

        var result = acceptConnectionHandler.handle(request);

        assertInstanceOf(AcceptConnectionRequest.Result.Success.class, result);

        assertTrue(pendingConnectionRepository.findById(pendingConnection.getId()).isEmpty());

        var getAcceptedConnectionsRequest = new GetAcceptedConnections.Request(new UserIdDTO(targetId.value()));
        var acceptedConnectionsResults = getAcceptedConnectionsHandler.handle(getAcceptedConnectionsRequest);

        assertInstanceOf(GetAcceptedConnections.Result.Success.class, acceptedConnectionsResults);
        var successResult = (GetAcceptedConnections.Result.Success) acceptedConnectionsResults;
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

        var request = new AcceptConnectionRequest.Request(connectionId, acceptedByUserId);

        var result = acceptConnectionHandler.handle(request);

        assertInstanceOf(AcceptConnectionRequest.Result.NotFound.class, result);
    }

    @Test
    void testHandleInvalidRequest_NotTargetUser() {
        var pendingConnection = pendingConnectionEntityMother.createPendingConnectionEntity();
        pendingConnectionRepository.save(pendingConnection);

        var connectionId = new ConnectionIdDTO(pendingConnection.getId());
        var invalidTargetId = new UserIdDTO(UUID.randomUUID()); // A different user than the target

        var request = new AcceptConnectionRequest.Request(connectionId, invalidTargetId);

        var result = acceptConnectionHandler.handle(request);

        assertInstanceOf(AcceptConnectionRequest.Result.InvalidRequest.class, result);
        var invalidRequestResult = (AcceptConnectionRequest.Result.InvalidRequest) result;
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
        assertEquals("acceptedByUserId", invalidRequestResult.error().errors().get(0).field());
    }

    @Test
    void testHandleInvalidRequest_NullConnectionId() {
        var acceptedByUserId = new UserIdDTO(UUID.randomUUID());

        var request = new AcceptConnectionRequest.Request(null, acceptedByUserId);

        var result = acceptConnectionHandler.handle(request);

        assertInstanceOf(AcceptConnectionRequest.Result.InvalidRequest.class, result);
        var invalidRequestResult = (AcceptConnectionRequest.Result.InvalidRequest) result;
        assertNotNull(invalidRequestResult.error());
        assertFalse(invalidRequestResult.error().errors().isEmpty());
    }

    @Test
    void testHandleInvalidRequest_NullAcceptedByUser() {
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());

        var request = new AcceptConnectionRequest.Request(connectionId, null);

        var result = acceptConnectionHandler.handle(request);

        assertInstanceOf(AcceptConnectionRequest.Result.InvalidRequest.class, result);
        var invalidRequestResult = (AcceptConnectionRequest.Result.InvalidRequest) result;
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
        var createAcceptedConnectionRequest = new CreateAcceptedConnection.Request(connectionId, targetId, senderId);
        var createResult = createAcceptedConnectionHandler.handle(createAcceptedConnectionRequest);
        assertInstanceOf(CreateAcceptedConnection.Result.Success.class, createResult);

        // Now, attempt to accept the same pending connection
        var request = new AcceptConnectionRequest.Request(connectionId, targetId);

        var result = acceptConnectionHandler.handle(request);

        assertInstanceOf(AcceptConnectionRequest.Result.AlreadyAccepted.class, result);
    }
}