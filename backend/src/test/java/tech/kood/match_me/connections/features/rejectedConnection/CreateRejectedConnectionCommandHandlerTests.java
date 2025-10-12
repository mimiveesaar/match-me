package tech.kood.match_me.connections.features.rejectedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.rejectedConnection.actions.CreateRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(transactionManager = "connectionsTransactionManager")
public class CreateRejectedConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private CreateRejectedConnection.Handler createRejectedConnectionHandler;

    @Autowired
    private RejectedConnectionRepository repository;

    @Autowired
    private RejectedConnectionEntityMother rejectedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUserId, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result);
        var successResult = (CreateRejectedConnection.Result.Success) result;
        assertNotNull(successResult.connectionIdDTO());
        assertNotNull(successResult.connectionIdDTO().value());

        // Verify the rejected connection was saved to the repository
        var savedConnections = repository.findByRejectedByUser(rejectedByUserId.value());
        assertFalse(savedConnections.isEmpty());
        assertEquals(rejectedByUserId.value(), savedConnections.getFirst().getRejectedByUserId());
        assertEquals(rejectedUserId.value(), savedConnections.getFirst().getRejectedUserId());
    }

    @Test
    void testHandleValidRequest_SuccessWithoutTracingId() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_REMOVED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result);
        var successResult = (CreateRejectedConnection.Result.Success) result;
        assertNotNull(successResult.connectionIdDTO());
    }

    @Test
    void testHandleRequest_AlreadyExists() {
        // First, create a rejected connection
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);

        var rejectedByUserId = new UserIdDTO(entity.getRejectedByUserId());
        var rejectedUserId = new UserIdDTO(entity.getRejectedUserId());

        var request = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnection.Result.AlreadyExists.class, result);
        var alreadyExistsResult = (CreateRejectedConnection.Result.AlreadyExists) result;

        // Verify no duplicate was created
        var connections = repository.findBetweenUsers(entity.getRejectedByUserId(), entity.getRejectedUserId());
        assertTrue(connections.isPresent());
        assertEquals(entity.getId(), connections.get().getId());
    }

    @Test
    void testHandleRequest_InvalidRequest_NullRejectedByUserId() {
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnection.Request(null, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnection.Result.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullRejectedUserId() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnection.Request(rejectedByUserId, null,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnection.Result.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullReason() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUserId, null);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnection.Result.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_InvalidUserIdDTO() {
        var rejectedByUserId = new UserIdDTO(null); // Invalid UserIdDTO
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUserId, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnection.Result.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_SameRejectedByAndRejectedUser() {
        var userId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnection.Request(userId, userId, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnection.Result.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_BidirectionalRejection() {
        // Create a rejection from A to B
        var userA = new UserIdDTO(UUID.randomUUID());
        var userB = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateRejectedConnection.Request(userA, userB, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result1 = createRejectedConnectionHandler.handle(request1);
        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result1);

        // Try to create a rejection from B to A
        var request2 = new CreateRejectedConnection.Request(userB, userA,
                RejectedConnectionReasonDTO.CONNECTION_REMOVED);
        var result2 = createRejectedConnectionHandler.handle(request2);

        assertInstanceOf(CreateRejectedConnection.Result.AlreadyExists.class, result2);
    }

    @Test
    void testHandleRequest_WithDifferentReasons() {
        var rejectedByUserId1 = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId1 = new UserIdDTO(UUID.randomUUID());
        var rejectedByUserId2 = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId2 = new UserIdDTO(UUID.randomUUID());

        // Test CONNECTION_DECLINED reason
        var request1 = new CreateRejectedConnection.Request(rejectedByUserId1, rejectedUserId1,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result1 = createRejectedConnectionHandler.handle(request1);
        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result1);

        // Test CONNECTION_REMOVED reason
        var request2 = new CreateRejectedConnection.Request(rejectedByUserId2, rejectedUserId2,
                RejectedConnectionReasonDTO.CONNECTION_REMOVED);
        var result2 = createRejectedConnectionHandler.handle(request2);
        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result2);

        // Verify both rejections were created with correct reasons
        var rejections1 = repository.findByRejectedByUser(rejectedByUserId1.value());
        var rejections2 = repository.findByRejectedByUser(rejectedByUserId2.value());

        assertEquals(1, rejections1.size());
        assertEquals(1, rejections2.size());
        assertNotEquals(rejections1.getFirst().getReason(), rejections2.getFirst().getReason());
    }

    @Test
    void testTransactionalBehavior() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);

        // Count rejections before
        var initialCount = repository.findAll().size();

        var result = createRejectedConnectionHandler.handle(request);
        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result);

        // Verify rejection was added
        var finalCount = repository.findAll().size();
        assertEquals(initialCount + 1, finalCount);
    }

    @Test
    void testMultipleRejectionsFromSameUser() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId1 = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId2 = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUserId1, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var request2 = new CreateRejectedConnection.Request(rejectedByUserId, rejectedUserId2, RejectedConnectionReasonDTO.CONNECTION_REMOVED);

        var result1 = createRejectedConnectionHandler.handle(request1);
        var result2 = createRejectedConnectionHandler.handle(request2);

        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result1);
        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result2);

        var rejectionsFromUser = repository.findByRejectedByUser(rejectedByUserId.value());
        assertEquals(2, rejectionsFromUser.size());
    }

    @Test
    void testMultipleRejectionsToSameUser() {
        var rejectedByUserId1 = new UserIdDTO(UUID.randomUUID());
        var rejectedByUserId2 = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateRejectedConnection.Request(rejectedByUserId1, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var request2 = new CreateRejectedConnection.Request(rejectedByUserId2, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_REMOVED);

        var result1 = createRejectedConnectionHandler.handle(request1);
        var result2 = createRejectedConnectionHandler.handle(request2);

        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result1);
        assertInstanceOf(CreateRejectedConnection.Result.Success.class, result2);

        var rejectionsToUser = repository.findByRejectedUser(rejectedUserId.value());
        assertEquals(2, rejectionsToUser.size());
    }
}
