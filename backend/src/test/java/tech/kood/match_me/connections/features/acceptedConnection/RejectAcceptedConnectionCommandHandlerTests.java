package tech.kood.match_me.connections.features.acceptedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.acceptedConnection.actions.RejectAcceptedConnection;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional(transactionManager = "connectionsTransactionManager")
public class RejectAcceptedConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private RejectAcceptedConnection.Handler rejectAcceptedConnectionHandler;

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

        var request = new RejectAcceptedConnection.Request(connectionId, rejectedBy);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnection.Result.Success.class, result);
        var successResult = (RejectAcceptedConnection.Result.Success) result;

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

        var request = new RejectAcceptedConnection.Request(connectionId, rejectedBy);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnection.Result.Success.class, result);
        var successResult = (RejectAcceptedConnection.Result.Success) result;

        // Verify the accepted connection was deleted from the repository
        var deleted = repository.findById(connectionId.value());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testHandleRequest_NotFound() {
        var connectionId = new ConnectionIdDTO(UUID.randomUUID());
        var rejectedBy = new UserIdDTO(UUID.randomUUID());

        var request = new RejectAcceptedConnection.Request(connectionId, rejectedBy);
        var result = rejectAcceptedConnectionHandler.handle(request);

        assertInstanceOf(RejectAcceptedConnection.Result.NotFound.class, result);
        var notFoundResult = (RejectAcceptedConnection.Result.NotFound) result;
    }
}
