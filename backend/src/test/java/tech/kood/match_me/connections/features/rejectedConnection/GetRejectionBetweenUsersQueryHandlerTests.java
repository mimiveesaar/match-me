package tech.kood.match_me.connections.features.rejectedConnection;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.GetRejectionBetweenUsersRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.GetRejectionBetweenUsersQueryHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.GetRejectionBetweenUsersResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetRejectionBetweenUsersQueryHandlerTests extends ConnectionsTestBase {

    @Autowired
    private GetRejectionBetweenUsersQueryHandler getRejectionBetweenUsersHandler;

    @Autowired
    private RejectedConnectionRepository repository;

    @Autowired
    private RejectedConnectionEntityMother rejectedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success_RejectionExists() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();

        // Create rejection from user1 to user2
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, user1Id, user2Id);
        entity = rejectedConnectionEntityMother.withReason(entity, RejectedConnectionReason.CONNECTION_DECLINED);
        repository.save(entity);

        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(user1Id), new UserIdDTO(user2Id));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.Success.class, result);
        var successResult = (GetRejectionBetweenUsersResults.Success) result;
        assertNotNull(successResult.rejection());
        assertEquals(user1Id, successResult.rejection().rejectedByUser().value());
        assertEquals(user2Id, successResult.rejection().rejectedUser().value());
    }

    @Test
    void testHandleValidRequest_Success_BidirectionalSearch() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();

        // Create rejection from user1 to user2
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, user1Id, user2Id);
        repository.save(entity);

        // Search with users in reverse order (user2, user1) - should still find the rejection
        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(user2Id), new UserIdDTO(user1Id));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.Success.class, result);
        var successResult = (GetRejectionBetweenUsersResults.Success) result;
        assertNotNull(successResult.rejection());
        // The rejection should still show the original direction (user1 rejected user2)
        assertEquals(user1Id, successResult.rejection().rejectedByUser().value());
        assertEquals(user2Id, successResult.rejection().rejectedUser().value());
    }

    @Test
    void testHandleValidRequest_NotFound_NoRejectionExists() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();

        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(user1Id), new UserIdDTO(user2Id));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.NotFound.class, result);
        var notFoundResult = (GetRejectionBetweenUsersResults.NotFound) result;
    }

    @Test
    void testHandleValidRequest_NotFound_RejectionExistsButNotBetweenTheseUsers() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();
        var user3Id = UUID.randomUUID();

        // Create rejection between user1 and user3 (not user2)
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, user1Id, user3Id);
        repository.save(entity);

        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(user1Id), new UserIdDTO(user2Id));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.NotFound.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_NullUser1() {
        // Arrange
        var user2Id = new UserIdDTO(UUID.randomUUID());
        var request = new GetRejectionBetweenUsersRequest(null, user2Id);

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.InvalidRequest.class, result);
        var invalidResult = (GetRejectionBetweenUsersResults.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_InvalidRequest_NullUser2() {
        // Arrange
        var user1Id = new UserIdDTO(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new GetRejectionBetweenUsersRequest(user1Id, null);

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.InvalidRequest.class, result);
        var invalidResult = (GetRejectionBetweenUsersResults.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_InvalidRequest_InvalidUser1DTO() {
        // Arrange
        var user1Id = new UserIdDTO(null); // Invalid UserIdDTO
        var user2Id = new UserIdDTO(UUID.randomUUID());

        var request = new GetRejectionBetweenUsersRequest(user1Id, user2Id);

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.InvalidRequest.class, result);
        var invalidResult = (GetRejectionBetweenUsersResults.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_InvalidRequest_InvalidUser2DTO() {
        // Arrange
        var user1Id = new UserIdDTO(UUID.randomUUID());
        var user2Id = new UserIdDTO(null); // Invalid UserIdDTO

        var request = new GetRejectionBetweenUsersRequest(user1Id, user2Id);

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.InvalidRequest.class, result);
        var invalidResult = (GetRejectionBetweenUsersResults.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_InvalidRequest_SameUser() {
        // Arrange
        var userId = new UserIdDTO(UUID.randomUUID());
        var request = new GetRejectionBetweenUsersRequest(userId, userId);

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.InvalidRequest.class, result);
        var invalidResult = (GetRejectionBetweenUsersResults.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_Success_WithDifferentRejectionReasons() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();

        // Create rejection with CONNECTION_REMOVED reason
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, user1Id, user2Id);
        entity = rejectedConnectionEntityMother.withReason(entity, RejectedConnectionReason.CONNECTION_REMOVED);
        repository.save(entity);

        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(user1Id), new UserIdDTO(user2Id));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.Success.class, result);
        var successResult = (GetRejectionBetweenUsersResults.Success) result;
        assertEquals("CONNECTION_REMOVED", successResult.rejection().reason().name());
    }

    @Test
    void testHandleRequest_Success_ValidatesReturnedData() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();

        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, user1Id, user2Id);
        repository.save(entity);

        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(user1Id), new UserIdDTO(user2Id));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.Success.class, result);
        var successResult = (GetRejectionBetweenUsersResults.Success) result;

        var rejection = successResult.rejection();
        assertNotNull(rejection.connectionIdDTO());
        assertNotNull(rejection.rejectedByUser());
        assertNotNull(rejection.rejectedUser());
        assertNotNull(rejection.reason());
        assertNotNull(rejection.createdAt());

        assertEquals(user1Id, rejection.rejectedByUser().value());
        assertEquals(user2Id, rejection.rejectedUser().value());
    }

    @Test
    void testHandleRequest_NotFound_WithMultipleOtherRejections() {
        // Arrange
        var targetUser1 = UUID.randomUUID();
        var targetUser2 = UUID.randomUUID();
        var otherUser1 = UUID.randomUUID();
        var otherUser2 = UUID.randomUUID();

        // Create multiple rejections that don't involve our target users
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, otherUser1, otherUser2);
        repository.save(entity1);

        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, targetUser1, otherUser1);
        repository.save(entity2);

        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(targetUser1), new UserIdDTO(targetUser2));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.NotFound.class, result);
    }

    @Test
    void testTransactionalBehavior() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();

        // Create a rejection
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, user1Id, user2Id);
        repository.save(entity);

        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(user1Id), new UserIdDTO(user2Id));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert - should work correctly within transaction
        assertInstanceOf(GetRejectionBetweenUsersResults.Success.class, result);
        var successResult = (GetRejectionBetweenUsersResults.Success) result;
        assertNotNull(successResult.rejection());
    }

    @Test
    void testHandleRequest_Success_FindsOnlyRelevantRejection() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();
        var user3Id = UUID.randomUUID();

        // Create multiple rejections
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, user1Id, user3Id);
        repository.save(entity1);

        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, user1Id, user2Id);
        entity2 = rejectedConnectionEntityMother.withReason(entity2, RejectedConnectionReason.CONNECTION_DECLINED);
        repository.save(entity2);

        var entity3 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity3 = rejectedConnectionEntityMother.withSpecificIds(entity3, user2Id, user3Id);
        repository.save(entity3);

        var request = new GetRejectionBetweenUsersRequest(new UserIdDTO(user1Id), new UserIdDTO(user2Id));

        // Act
        var result = getRejectionBetweenUsersHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.Success.class, result);
        var successResult = (GetRejectionBetweenUsersResults.Success) result;

        var rejection = successResult.rejection();
        assertEquals(user1Id, rejection.rejectedByUser().value());
        assertEquals(user2Id, rejection.rejectedUser().value());
        assertEquals("CONNECTION_DECLINED", rejection.reason().name());
    }

    @Test
    void testHandleRequest_Success_BidirectionalSearchConsistency() {
        // Arrange
        var user1Id = UUID.randomUUID();
        var user2Id = UUID.randomUUID();

        // Create rejection from user1 to user2
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, user1Id, user2Id);
        repository.save(entity);

        var request1 = new GetRejectionBetweenUsersRequest(new UserIdDTO(user1Id), new UserIdDTO(user2Id));
        var request2 = new GetRejectionBetweenUsersRequest(new UserIdDTO(user2Id), new UserIdDTO(user1Id));

        // Act
        var result1 = getRejectionBetweenUsersHandler.handle(request1);
        var result2 = getRejectionBetweenUsersHandler.handle(request2);

        // Assert
        assertInstanceOf(GetRejectionBetweenUsersResults.Success.class, result1);
        assertInstanceOf(GetRejectionBetweenUsersResults.Success.class, result2);

        var successResult1 = (GetRejectionBetweenUsersResults.Success) result1;
        var successResult2 = (GetRejectionBetweenUsersResults.Success) result2;

        // Both should return the same rejection data regardless of search order
        assertEquals(successResult1.rejection().connectionIdDTO(), successResult2.rejection().connectionIdDTO());
        assertEquals(successResult1.rejection().rejectedByUser().value(), successResult2.rejection().rejectedByUser().value());
        assertEquals(successResult1.rejection().rejectedUser().value(), successResult2.rejection().rejectedUser().value());
        assertEquals(successResult1.rejection().reason().name(), successResult2.rejection().reason().name());
    }
}