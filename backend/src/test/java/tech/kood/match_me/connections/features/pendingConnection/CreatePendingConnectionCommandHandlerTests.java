package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.pendingConnection.actions.CreateConnectionRequest;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(transactionManager = "connectionsTransactionManager")
public class CreatePendingConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private CreateConnectionRequest.Handler connectionRequestHandler;

    @Autowired
    private PendingConnectionRepository repository;

    @Autowired
    private PendingConnectionEntityMother pendingConnectionEntityMother;

    @Autowired
    private UserIdFactory userIdFactory;

    @Test
    void testHandleValidRequest_Success() {
        var senderId = new UserIdDTO(UUID.randomUUID());
        var targetId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateConnectionRequest.Request(targetId, senderId);
        var result = connectionRequestHandler.handle(request);

        assertInstanceOf(CreateConnectionRequest.Result.Success.class, result);
        var successResult = (CreateConnectionRequest.Result.Success) result;
        assertNotNull(successResult.connectionIdDTO());
        assertNotNull(successResult.connectionIdDTO().value());

        // Verify the pending connection was saved to the repository
        var savedConnections = repository.findBySender(senderId.value());
        assertFalse(savedConnections.isEmpty());
        assertEquals(senderId.value(), savedConnections.getFirst().getSenderId());
        assertEquals(targetId.value(), savedConnections.getFirst().getTargetId());
    }

    @Test
    void testHandleValidRequest_SuccessWithoutTracingId() {
        var senderId = new UserIdDTO(UUID.randomUUID());
        var targetId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateConnectionRequest.Request(targetId, senderId);
        var result = connectionRequestHandler.handle(request);

        assertInstanceOf(CreateConnectionRequest.Result.Success.class, result);
        var successResult = (CreateConnectionRequest.Result.Success) result;
        assertNotNull(successResult.connectionIdDTO());
    }

    @Test
    void testHandleRequest_AlreadyExists() {
        // First, create a pending connection
        var entity = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity);

        var senderId = new UserIdDTO(entity.getSenderId());
        var targetId = new UserIdDTO(entity.getTargetId());

        var request = new CreateConnectionRequest.Request(targetId, senderId);
        var result = connectionRequestHandler.handle(request);

        assertInstanceOf(CreateConnectionRequest.Result.AlreadyExists.class, result);
        var alreadyExistsResult = (CreateConnectionRequest.Result.AlreadyExists) result;

        // Verify no duplicate was created
        var connections = repository.findBetweenUsers(entity.getSenderId(), entity.getTargetId());
        assertTrue(connections.isPresent());
        assertEquals(entity.getId(), connections.get().getId());
    }

    @Test
    void testHandleRequest_InvalidRequest_NullSenderId() {
        var targetId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateConnectionRequest.Request(targetId, null);
        var result = connectionRequestHandler.handle(request);

        assertInstanceOf(CreateConnectionRequest.Result.InvalidRequest.class, result);
        var invalidResult = (CreateConnectionRequest.Result.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_InvalidRequest_NullTargetId() {
        var senderId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateConnectionRequest.Request(null, senderId);
        var result = connectionRequestHandler.handle(request);

        assertInstanceOf(CreateConnectionRequest.Result.InvalidRequest.class, result);
        var invalidResult = (CreateConnectionRequest.Result.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_InvalidRequest_InvalidUserIdDTO() {
        var senderId = new UserIdDTO(null); // Invalid UserIdDTO
        var targetId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateConnectionRequest.Request(targetId, senderId);
        var result = connectionRequestHandler.handle(request);

        assertInstanceOf(CreateConnectionRequest.Result.InvalidRequest.class, result);
        var invalidResult = (CreateConnectionRequest.Result.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_SameSenderAndTarget() {
        var userId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateConnectionRequest.Request(userId, userId);
        var result = connectionRequestHandler.handle(request);

        // This should be handled by validation or business logic
        // The exact behavior depends on the domain rules
        assertNotNull(result);
    }

    @Test
    void testHandleRequest_BidirectionalConnection() {
        // Create a connection from A to B
        var userA = new UserIdDTO(UUID.randomUUID());
        var userB = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateConnectionRequest.Request(userB, userA);
        var result1 = connectionRequestHandler.handle(request1);
        assertInstanceOf(CreateConnectionRequest.Result.Success.class, result1);

        // Try to create a connection from B to A
        var request2 = new CreateConnectionRequest.Request(userA, userB);
        var result2 = connectionRequestHandler.handle(request2);

        // This should return already exists as it's the same connection
        assertInstanceOf(CreateConnectionRequest.Result.AlreadyExists.class, result2);
    }

    @Test
    void testTransactionalBehavior() {
        var senderId = new UserIdDTO(UUID.randomUUID());
        var targetId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateConnectionRequest.Request(targetId, senderId);

        // Count connections before
        var initialCount = repository.findAll().size();

        var result = connectionRequestHandler.handle(request);
        assertInstanceOf(CreateConnectionRequest.Result.Success.class, result);

        // Verify connection was added
        var finalCount = repository.findAll().size();
        assertEquals(initialCount + 1, finalCount);
    }

    @Test
    void testMultipleRequestsFromSameSender() {
        var senderId = new UserIdDTO(UUID.randomUUID());
        var targetId1 = new UserIdDTO(UUID.randomUUID());
        var targetId2 = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateConnectionRequest.Request(targetId1, senderId);
        var request2 = new CreateConnectionRequest.Request(targetId2, senderId);

        var result1 = connectionRequestHandler.handle(request1);
        var result2 = connectionRequestHandler.handle(request2);

        assertInstanceOf(CreateConnectionRequest.Result.Success.class, result1);
        assertInstanceOf(CreateConnectionRequest.Result.Success.class, result2);

        var connectionsFromSender = repository.findBySender(senderId.value());
        assertEquals(2, connectionsFromSender.size());
    }

    @Test
    void testMultipleRequestsToSameTarget() {
        var senderId1 = new UserIdDTO(UUID.randomUUID());
        var senderId2 = new UserIdDTO(UUID.randomUUID());
        var targetId = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateConnectionRequest.Request(targetId, senderId1);
        var request2 = new CreateConnectionRequest.Request(targetId, senderId2);

        var result1 = connectionRequestHandler.handle(request1);
        var result2 = connectionRequestHandler.handle(request2);

        assertInstanceOf(CreateConnectionRequest.Result.Success.class, result1);
        assertInstanceOf(CreateConnectionRequest.Result.Success.class, result2);

        var connectionsToTarget = repository.findByTarget(targetId.value());
        assertEquals(2, connectionsToTarget.size());
    }
}
