package tech.kood.match_me.connections.features.acceptedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionCommandHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.rejectConnection.api.RejectAcceptedConnectionResults;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntity;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RejectAcceptedConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private RejectAcceptedConnectionCommandHandler rejectAcceptedConnectionHandler;

    @Autowired
    private AcceptedConnectionRepository repository;

    @Autowired
    private AcceptedConnectionEntityMother acceptedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success() {
        // Create an accepted connection to be rejected
        AcceptedConnectionEntity entity = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity);

        var connectionId = new ConnectionIdDTO(entity.getId());
        var rejectedBy = new UserIdDTO(entity.getAcceptedByUserId());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new RejectAcceptedConnectionRequest(requestId, connectionId, rejectedBy, tracingId);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnectionResults.Success.class, result);
        var successResult = (RejectAcceptedConnectionResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertEquals(tracingId, successResult.tracingId());

        // Verify the accepted connection was deleted from the repository
        var deleted = repository.findById(connectionId.value());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testHandleValidRequest_SuccessWithoutTracingId() {
        // Create an accepted connection to be rejected
        AcceptedConnectionEntity entity = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity);

        var connectionId = new ConnectionIdDTO(entity.getId());
        var rejectedBy = new UserIdDTO(entity.getAcceptedByUserId());
        var requestId = UUID.randomUUID();

        var request = new RejectAcceptedConnectionRequest(requestId, connectionId, rejectedBy, null);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnectionResults.Success.class, result);
        var successResult = (RejectAcceptedConnectionResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertNull(successResult.tracingId());

        // Verify the accepted connection was deleted from the repository
        var deleted = repository.findById(connectionId.value());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testHandleRequest_NotFound() {
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());
        var rejectedBy = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new RejectAcceptedConnectionRequest(requestId, connectionId, rejectedBy, tracingId);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnectionResults.NotFound.class, result);
        var notFoundResult = (RejectAcceptedConnectionResults.NotFound) result;
        assertEquals(requestId, notFoundResult.requestId());
        assertEquals(tracingId, notFoundResult.tracingId());
    }

    @Test
    void testHandleRequest_InvalidRequest_NullRequestId() {
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());
        var rejectedBy = new UserIdDTO(UUID.randomUUID());

        var exception = assertThrows(Exception.class, () -> {
            new RejectAcceptedConnectionRequest(null, connectionId, rejectedBy, "tracingId");
        });
        
        assertTrue(exception.getMessage().contains("requestId") || 
                  exception instanceof NullPointerException);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullConnectionId() {
        var rejectedBy = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();

        var exception = assertThrows(Exception.class, () -> {
            new RejectAcceptedConnectionRequest(requestId, null, rejectedBy, "tracingId");
        });
        
        assertTrue(exception.getMessage().contains("connectionId") || 
                  exception instanceof NullPointerException);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullRejectedBy() {
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();

        var exception = assertThrows(Exception.class, () -> {
            new RejectAcceptedConnectionRequest(requestId, connectionId, null, "tracingId");
        });
        
        assertTrue(exception.getMessage().contains("rejectedBy") || 
                  exception instanceof NullPointerException);
    }
}
