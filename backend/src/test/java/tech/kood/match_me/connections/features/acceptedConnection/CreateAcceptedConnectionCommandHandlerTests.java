package tech.kood.match_me.connections.features.acceptedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createAcceptedConnection.api.CreateAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createAcceptedConnection.api.CreateAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createAcceptedConnection.api.CreateAcceptedConnectionResults;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CreateAcceptedConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private CreateAcceptedConnectionCommandHandler createAcceptedConnectionHandler;

    @Autowired
    private AcceptedConnectionRepository repository;

    @Autowired
    private AcceptedConnectionEntityMother acceptedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success() {
        var acceptedByUserId = new UserIdDTO(UUID.randomUUID());
        var acceptedUserId = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new CreateAcceptedConnectionRequest(requestId, acceptedByUserId, acceptedUserId, tracingId);
        var result = createAcceptedConnectionHandler.handle(request);

        assertInstanceOf(CreateAcceptedConnectionResults.Success.class, result);
        var successResult = (CreateAcceptedConnectionResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertEquals(tracingId, successResult.tracingId());
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
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new CreateAcceptedConnectionRequest(requestId, acceptedByUserId, acceptedUserId, tracingId);
        var result = createAcceptedConnectionHandler.handle(request);

        assertInstanceOf(CreateAcceptedConnectionResults.AlreadyExists.class, result);
        var alreadyExistsResult = (CreateAcceptedConnectionResults.AlreadyExists) result;
        assertEquals(requestId, alreadyExistsResult.requestId());
        assertEquals(tracingId, alreadyExistsResult.tracingId());

        // Verify no duplicate was created
        var connections = repository.findBetweenUsers(entity.getAcceptedByUserId(), entity.getAcceptedUserId());
        assertTrue(connections.isPresent());
        assertEquals(entity.getId(), connections.get().getId());
    }



    @Test
    void testHandleRequest_InvalidRequest_NullRequestId() {
        var acceptedByUserId = new UserIdDTO(UUID.randomUUID());
        var acceptedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateAcceptedConnectionRequest(null, acceptedByUserId, acceptedUserId, "tracingId");
        var result = createAcceptedConnectionHandler.handle(request);
        assertInstanceOf(CreateAcceptedConnectionResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullAcceptedByUser() {
        var acceptedUserId = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();

        var request = new CreateAcceptedConnectionRequest(requestId, null, acceptedUserId, "tracingId");
        var result = createAcceptedConnectionHandler.handle(request);
        assertInstanceOf(CreateAcceptedConnectionResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullAcceptedUser() {
        var acceptedByUserId = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();

        var request = new CreateAcceptedConnectionRequest(requestId, acceptedByUserId, null, "tracingId");
        var result = createAcceptedConnectionHandler.handle(request);
        assertInstanceOf(CreateAcceptedConnectionResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_SameUserAsAcceptedByAndAcceptedUser() {
        var userId = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id-same-user";

        var request = new CreateAcceptedConnectionRequest(requestId, userId, userId, tracingId);
        var result = createAcceptedConnectionHandler.handle(request);
        assertInstanceOf(CreateAcceptedConnectionResults.InvalidRequest.class, result);
    }
}
