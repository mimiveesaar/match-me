package tech.kood.match_me.connections.features.rejectedConnection;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api.GetRejectionsByUserRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api.GetRejectionsByUserQueryHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionsByUser.api.GetRejectionsByUserResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;
import tech.kood.match_me.common.domain.api.UserIdDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetRejectionsByUserQueryHandlerTests extends ConnectionsTestBase {

    @Autowired
    private GetRejectionsByUserQueryHandler getRejectionsByUserHandler;

    @Autowired
    private RejectedConnectionRepository repository;

    @Autowired
    private RejectedConnectionEntityMother rejectedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success_WithRejections() {
        // Arrange
        var rejectedUserId = UUID.randomUUID();
        var rejectedByUserId1 = UUID.randomUUID();
        var rejectedByUserId2 = UUID.randomUUID();

        // Create two rejections WHERE rejectedUserId is the one being rejected
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, rejectedByUserId1, rejectedUserId);
        entity1 = rejectedConnectionEntityMother.withReason(entity1, RejectedConnectionReason.CONNECTION_DECLINED);
        repository.save(entity1);

        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, rejectedByUserId2, rejectedUserId);
        entity2 = rejectedConnectionEntityMother.withReason(entity2, RejectedConnectionReason.CONNECTION_REMOVED);
        repository.save(entity2);

        var request = new GetRejectionsByUserRequest(new UserIdDTO(rejectedUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertEquals(2, successResult.rejections().size());

        var rejections = successResult.rejections();
        var rejectedByUserIds = rejections.stream()
                .map(r -> r.rejectedByUser().value())
                .toList();
        assertTrue(rejectedByUserIds.contains(rejectedByUserId1));
        assertTrue(rejectedByUserIds.contains(rejectedByUserId2));

        // All rejections should be for the same rejected user
        rejections.forEach(rejection ->
            assertEquals(rejectedUserId, rejection.rejectedUser().value())
        );
    }

    @Test
    void testHandleValidRequest_Success_NoRejections() {
        // Arrange
        var rejectedUserId = UUID.randomUUID();

        var request = new GetRejectionsByUserRequest(new UserIdDTO(rejectedUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertTrue(successResult.rejections().isEmpty());
    }

    @Test
    void testHandleRequest_InvalidRequest_NullRejectedUser() {
        // Arrange
        var request = new GetRejectionsByUserRequest(null);

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.InvalidRequest.class, result);
        var invalidResult = (GetRejectionsByUserResults.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_InvalidRequest_InvalidUserIdDTO() {
        // Arrange
        var rejectedUserId = new UserIdDTO(null); // Invalid UserIdDTO

        var request = new GetRejectionsByUserRequest(rejectedUserId);

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.InvalidRequest.class, result);
        var invalidResult = (GetRejectionsByUserResults.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_Success_OnlyReturnsRejectionsForSpecificUser() {
        // Arrange
        var targetRejectedUserId = UUID.randomUUID();
        var otherRejectedUserId = UUID.randomUUID();
        var rejectedByUserId1 = UUID.randomUUID();
        var rejectedByUserId2 = UUID.randomUUID();

        // Create rejection for target user
        var targetEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        targetEntity = rejectedConnectionEntityMother.withSpecificIds(targetEntity, rejectedByUserId1, targetRejectedUserId);
        repository.save(targetEntity);

        // Create rejection for other user (should not be returned)
        var otherEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        otherEntity = rejectedConnectionEntityMother.withSpecificIds(otherEntity, rejectedByUserId2, otherRejectedUserId);
        repository.save(otherEntity);

        var request = new GetRejectionsByUserRequest(new UserIdDTO(targetRejectedUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertEquals(1, successResult.rejections().size());
        assertEquals(rejectedByUserId1, successResult.rejections().getFirst().rejectedByUser().value());
        assertEquals(targetRejectedUserId, successResult.rejections().getFirst().rejectedUser().value());
    }

    @Test
    void testHandleRequest_Success_WithDifferentReasons() {
        // Arrange
        var rejectedUserId = UUID.randomUUID();
        var rejectedByUserId1 = UUID.randomUUID();
        var rejectedByUserId2 = UUID.randomUUID();

        // Create rejection with CONNECTION_DECLINED reason
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, rejectedByUserId1, rejectedUserId);
        entity1 = rejectedConnectionEntityMother.withReason(entity1, RejectedConnectionReason.CONNECTION_DECLINED);
        repository.save(entity1);

        // Create rejection with CONNECTION_REMOVED reason
        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, rejectedByUserId2, rejectedUserId);
        entity2 = rejectedConnectionEntityMother.withReason(entity2, RejectedConnectionReason.CONNECTION_REMOVED);
        repository.save(entity2);

        var request = new GetRejectionsByUserRequest(new UserIdDTO(rejectedUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertEquals(2, successResult.rejections().size());

        var reasons = successResult.rejections().stream()
                .map(r -> r.reason())
                .toList();
        assertEquals(2, reasons.size());
        // Both reasons should be present in the results
        assertTrue(reasons.stream().anyMatch(r -> r.name().equals("CONNECTION_DECLINED")));
        assertTrue(reasons.stream().anyMatch(r -> r.name().equals("CONNECTION_REMOVED")));
    }

    @Test
    void testHandleRequest_Success_MultipleUsersRejectedSameUser() {
        // Arrange
        var rejectedUserId = UUID.randomUUID();
        var rejectedByUserId1 = UUID.randomUUID();
        var rejectedByUserId2 = UUID.randomUUID();
        var rejectedByUserId3 = UUID.randomUUID();

        // Create multiple rejections where different users rejected the same user
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, rejectedByUserId1, rejectedUserId);
        repository.save(entity1);

        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, rejectedByUserId2, rejectedUserId);
        repository.save(entity2);

        var entity3 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity3 = rejectedConnectionEntityMother.withSpecificIds(entity3, rejectedByUserId3, rejectedUserId);
        repository.save(entity3);

        var request = new GetRejectionsByUserRequest(new UserIdDTO(rejectedUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertEquals(3, successResult.rejections().size());

        // All rejections should be for the correct rejected user
        successResult.rejections().forEach(rejection ->
            assertEquals(rejectedUserId, rejection.rejectedUser().value())
        );

        // Should have rejections from all three different users
        var rejectedByUserIds = successResult.rejections().stream()
                .map(r -> r.rejectedByUser().value())
                .toList();
        assertTrue(rejectedByUserIds.contains(rejectedByUserId1));
        assertTrue(rejectedByUserIds.contains(rejectedByUserId2));
        assertTrue(rejectedByUserIds.contains(rejectedByUserId3));
    }

    @Test
    void testTransactionalBehavior() {
        // Arrange
        var rejectedUserId = UUID.randomUUID();
        var rejectedByUserId = UUID.randomUUID();

        // Create a rejection
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, rejectedByUserId, rejectedUserId);
        repository.save(entity);

        var request = new GetRejectionsByUserRequest(new UserIdDTO(rejectedUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert - should work correctly within transaction
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertEquals(1, successResult.rejections().size());
    }

    @Test
    void testHandleRequest_Success_EmptyResultForNonExistentUser() {
        // Arrange
        var nonExistentUserId = UUID.randomUUID();

        // Create some rejections for other users to ensure the query is selective
        var otherEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(otherEntity);

        var request = new GetRejectionsByUserRequest(new UserIdDTO(nonExistentUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertTrue(successResult.rejections().isEmpty());
    }

    @Test
    void testHandleRequest_Success_ValidatesReturnedData() {
        // Arrange
        var rejectedUserId = UUID.randomUUID();
        var rejectedByUserId = UUID.randomUUID();

        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, rejectedByUserId, rejectedUserId);
        repository.save(entity);

        var request = new GetRejectionsByUserRequest(new UserIdDTO(rejectedUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertEquals(1, successResult.rejections().size());

        var rejection = successResult.rejections().get(0);
        assertNotNull(rejection.connectionIdDTO());
        assertNotNull(rejection.rejectedByUser());
        assertNotNull(rejection.rejectedUser());
        assertNotNull(rejection.reason());
        assertNotNull(rejection.createdAt());

        assertEquals(rejectedByUserId, rejection.rejectedByUser().value());
        assertEquals(rejectedUserId, rejection.rejectedUser().value());
    }

    @Test
    void testHandleRequest_Success_WithMixedRejectionScenarios() {
        // Arrange
        var targetUserId = UUID.randomUUID();
        var user1 = UUID.randomUUID();
        var user2 = UUID.randomUUID();
        var user3 = UUID.randomUUID();

        // Create a rejection where targetUserId rejected user1 (should NOT be returned)
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, targetUserId, user1);
        repository.save(entity1);

        // Create rejection where user2 rejected targetUserId (should be returned)
        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, user2, targetUserId);
        repository.save(entity2);

        // Create rejection where user3 rejected targetUserId (should be returned)
        var entity3 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity3 = rejectedConnectionEntityMother.withSpecificIds(entity3, user3, targetUserId);
        repository.save(entity3);

        var request = new GetRejectionsByUserRequest(new UserIdDTO(targetUserId));

        // Act
        var result = getRejectionsByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectionsByUserResults.Success.class, result);
        var successResult = (GetRejectionsByUserResults.Success) result;
        assertEquals(2, successResult.rejections().size());

        // All returned rejections should have targetUserId as the rejected user
        successResult.rejections().forEach(rejection ->
            assertEquals(targetUserId, rejection.rejectedUser().value())
        );

        var rejectedByUserIds = successResult.rejections().stream()
                .map(r -> r.rejectedByUser().value())
                .toList();
        assertTrue(rejectedByUserIds.contains(user2));
        assertTrue(rejectedByUserIds.contains(user3));
        assertFalse(rejectedByUserIds.contains(targetUserId)); // Should not contain self
    }
}
