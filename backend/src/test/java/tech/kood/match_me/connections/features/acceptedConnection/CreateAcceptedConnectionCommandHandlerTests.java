package tech.kood.match_me.connections.features.acceptedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.CreateAcceptedConnection;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(transactionManager = "connectionsTransactionManager")
public class CreateAcceptedConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private CreateAcceptedConnection.Handler createAcceptedConnectionHandler;

    @Autowired
    private AcceptedConnectionRepository repository;

    @Autowired
    private AcceptedConnectionEntityMother acceptedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success() {
        var acceptedByUserId = new UserIdDTO(UUID.randomUUID());
        var acceptedUserId = new UserIdDTO(UUID.randomUUID());
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());

        var request = new CreateAcceptedConnection.Request(connectionId, acceptedByUserId, acceptedUserId);
        var result = createAcceptedConnectionHandler.handle(request);

        assertInstanceOf(CreateAcceptedConnection.Result.Success.class, result);
        var successResult = (CreateAcceptedConnection.Result.Success) result;
        assertNotNull(successResult.connectionIdDTO());
        assertNotNull(successResult.connectionIdDTO().value());

        // Verify the accepted connection was saved to the repository
        var savedConnections = repository.findByAcceptedByUser(acceptedByUserId.value());
        assertFalse(savedConnections.isEmpty());
        assertEquals(acceptedByUserId.value(), savedConnections.getFirst().getAcceptedByUserId());
        assertEquals(acceptedUserId.value(), savedConnections.getFirst().getAcceptedUserId());
    }

    @Test
    void testHandleRequest_AlreadyExists() {
        // First, create an accepted connection
        var entity = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity);

        var acceptedByUserId = new UserIdDTO(entity.getAcceptedByUserId());
        var acceptedUserId = new UserIdDTO(entity.getAcceptedUserId());
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());

        var request = new CreateAcceptedConnection.Request(connectionId, acceptedByUserId, acceptedUserId);
        var result = createAcceptedConnectionHandler.handle(request);

        assertInstanceOf(CreateAcceptedConnection.Result.AlreadyExists.class, result);
        var alreadyExistsResult = (CreateAcceptedConnection.Result.AlreadyExists) result;

        // Verify no duplicate was created
        var connections = repository.findBetweenUsers(entity.getAcceptedByUserId(), entity.getAcceptedUserId());
        assertTrue(connections.isPresent());
        assertEquals(entity.getId(), connections.get().getId());
    }

    @Test
    void testHandleRequest_InvalidRequest_NullAcceptedByUser() {
        var acceptedUserId = new UserIdDTO(UUID.randomUUID());
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());

        var request = new CreateAcceptedConnection.Request(connectionId, null, acceptedUserId);
        var result = createAcceptedConnectionHandler.handle(request);
        assertInstanceOf(CreateAcceptedConnection.Result.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullAcceptedUser() {
        var acceptedByUserId = new UserIdDTO(UUID.randomUUID());
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());

        var request = new CreateAcceptedConnection.Request(connectionId, acceptedByUserId, null);
        var result = createAcceptedConnectionHandler.handle(request);
        assertInstanceOf(CreateAcceptedConnection.Result.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_SameUserAsAcceptedByAndAcceptedUser() {
        var userId = new UserIdDTO(UUID.randomUUID());
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());

        var request = new CreateAcceptedConnection.Request(connectionId, userId, userId);
        var result = createAcceptedConnectionHandler.handle(request);
        assertInstanceOf(CreateAcceptedConnection.Result.InvalidRequest.class, result);
    }
}