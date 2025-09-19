package tech.kood.match_me.connections.features.rejectedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.api.RejectedConnectionReasonDTO;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CreateRejectedConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private CreateRejectedConnectionCommandHandler createRejectedConnectionHandler;

    @Autowired
    private RejectedConnectionRepository repository;

    @Autowired
    private RejectedConnectionEntityMother rejectedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUserId, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result);
        var successResult = (CreateRejectedConnectionResults.Success) result;
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

        var request = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_REMOVED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result);
        var successResult = (CreateRejectedConnectionResults.Success) result;
        assertNotNull(successResult.connectionIdDTO());
    }

    @Test
    void testHandleRequest_AlreadyExists() {
        // First, create a rejected connection
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);

        var rejectedByUserId = new UserIdDTO(entity.getRejectedByUserId());
        var rejectedUserId = new UserIdDTO(entity.getRejectedUserId());

        var request = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnectionResults.AlreadyExists.class, result);
        var alreadyExistsResult = (CreateRejectedConnectionResults.AlreadyExists) result;

        // Verify no duplicate was created
        var connections = repository.findBetweenUsers(entity.getRejectedByUserId(), entity.getRejectedUserId());
        assertTrue(connections.isPresent());
        assertEquals(entity.getId(), connections.get().getId());
    }

    @Test
    void testHandleRequest_InvalidRequest_NullRejectedByUserId() {
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnectionRequest(null, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnectionResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullRejectedUserId() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnectionRequest(rejectedByUserId, null,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnectionResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullReason() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUserId, null);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnectionResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_InvalidUserIdDTO() {
        var rejectedByUserId = new UserIdDTO(null); // Invalid UserIdDTO
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUserId, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnectionResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_SameRejectedByAndRejectedUser() {
        var userId = new UserIdDTO(UUID.randomUUID());

        var request = new CreateRejectedConnectionRequest(userId, userId, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result = createRejectedConnectionHandler.handle(request);

        assertInstanceOf(CreateRejectedConnectionResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_BidirectionalRejection() {
        // Create a rejection from A to B
        var userA = new UserIdDTO(UUID.randomUUID());
        var userB = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateRejectedConnectionRequest(userA, userB, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result1 = createRejectedConnectionHandler.handle(request1);
        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result1);

        // Try to create a rejection from B to A
        var request2 = new CreateRejectedConnectionRequest(userB, userA,
                RejectedConnectionReasonDTO.CONNECTION_REMOVED);
        var result2 = createRejectedConnectionHandler.handle(request2);

        assertInstanceOf(CreateRejectedConnectionResults.AlreadyExists.class, result2);
    }

    @Test
    void testHandleRequest_WithDifferentReasons() {
        var rejectedByUserId1 = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId1 = new UserIdDTO(UUID.randomUUID());
        var rejectedByUserId2 = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId2 = new UserIdDTO(UUID.randomUUID());

        // Test CONNECTION_DECLINED reason
        var request1 = new CreateRejectedConnectionRequest(rejectedByUserId1, rejectedUserId1,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var result1 = createRejectedConnectionHandler.handle(request1);
        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result1);

        // Test CONNECTION_REMOVED reason
        var request2 = new CreateRejectedConnectionRequest(rejectedByUserId2, rejectedUserId2,
                RejectedConnectionReasonDTO.CONNECTION_REMOVED);
        var result2 = createRejectedConnectionHandler.handle(request2);
        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result2);

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

        var request = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);

        // Count rejections before
        var initialCount = repository.findAll().size();

        var result = createRejectedConnectionHandler.handle(request);
        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result);

        // Verify rejection was added
        var finalCount = repository.findAll().size();
        assertEquals(initialCount + 1, finalCount);
    }

    @Test
    void testMultipleRejectionsFromSameUser() {
        var rejectedByUserId = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId1 = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId2 = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUserId1, RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var request2 = new CreateRejectedConnectionRequest(rejectedByUserId, rejectedUserId2, RejectedConnectionReasonDTO.CONNECTION_REMOVED);

        var result1 = createRejectedConnectionHandler.handle(request1);
        var result2 = createRejectedConnectionHandler.handle(request2);

        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result1);
        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result2);

        var rejectionsFromUser = repository.findByRejectedByUser(rejectedByUserId.value());
        assertEquals(2, rejectionsFromUser.size());
    }

    @Test
    void testMultipleRejectionsToSameUser() {
        var rejectedByUserId1 = new UserIdDTO(UUID.randomUUID());
        var rejectedByUserId2 = new UserIdDTO(UUID.randomUUID());
        var rejectedUserId = new UserIdDTO(UUID.randomUUID());

        var request1 = new CreateRejectedConnectionRequest(rejectedByUserId1, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_DECLINED);
        var request2 = new CreateRejectedConnectionRequest(rejectedByUserId2, rejectedUserId,
                RejectedConnectionReasonDTO.CONNECTION_REMOVED);

        var result1 = createRejectedConnectionHandler.handle(request1);
        var result2 = createRejectedConnectionHandler.handle(request2);

        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result1);
        assertInstanceOf(CreateRejectedConnectionResults.Success.class, result2);

        var rejectionsToUser = repository.findByRejectedUser(rejectedUserId.value());
        assertEquals(2, rejectionsToUser.size());
    }
}
