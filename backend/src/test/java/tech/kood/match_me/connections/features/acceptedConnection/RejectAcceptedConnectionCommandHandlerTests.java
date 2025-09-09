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

        var request = new RejectAcceptedConnectionRequest(connectionId, rejectedBy);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnectionResults.Success.class, result);
        var successResult = (RejectAcceptedConnectionResults.Success) result;

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

        var request = new RejectAcceptedConnectionRequest(connectionId, rejectedBy);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnectionResults.Success.class, result);
        var successResult = (RejectAcceptedConnectionResults.Success) result;

        // Verify the accepted connection was deleted from the repository
        var deleted = repository.findById(connectionId.value());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testHandleRequest_NotFound() {
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());
        var rejectedBy = new UserIdDTO(UUID.randomUUID());

        var request = new RejectAcceptedConnectionRequest(connectionId, rejectedBy);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnectionResults.NotFound.class, result);
        var notFoundResult = (RejectAcceptedConnectionResults.NotFound) result;
    }
}
