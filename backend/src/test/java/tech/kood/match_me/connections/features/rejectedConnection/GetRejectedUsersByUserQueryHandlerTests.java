package tech.kood.match_me.connections.features.rejectedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api.GetRejectedUsersByUserQueryHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api.GetRejectedUsersByUserRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectedUsersByUser.api.GetRejectedUsersByUserResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetRejectedUsersByUserQueryHandlerTests extends ConnectionsTestBase {

    @Autowired
    private GetRejectedUsersByUserQueryHandler getRejectedUsersByUserHandler;

    @Autowired
    private RejectedConnectionRepository repository;

    @Autowired
    private RejectedConnectionEntityMother rejectedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success_WithRejections() {
        // Arrange
        var rejectedByUserId = UUID.randomUUID();
        var rejectedUserId1 = UUID.randomUUID();
        var rejectedUserId2 = UUID.randomUUID();

        // Create two rejected connections for the user
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, rejectedByUserId, rejectedUserId1);
        entity1 = rejectedConnectionEntityMother.withReason(entity1, RejectedConnectionReason.CONNECTION_DECLINED);
        repository.save(entity1);

        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, rejectedByUserId, rejectedUserId2);
        entity2 = rejectedConnectionEntityMother.withReason(entity2, RejectedConnectionReason.CONNECTION_REMOVED);
        repository.save(entity2);

        var request = new GetRejectedUsersByUserRequest(new UserIdDTO(rejectedByUserId));

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectedUsersByUserResults.Success.class, result);
        var successResult = (GetRejectedUsersByUserResults.Success) result;
        assertEquals(2, successResult.rejections().size());

        var rejections = successResult.rejections();
        var rejectedUserIds = rejections.stream()
                .map(r -> r.rejectedUser().value())
                .toList();
        assertTrue(rejectedUserIds.contains(rejectedUserId1));
        assertTrue(rejectedUserIds.contains(rejectedUserId2));
    }

    @Test
    void testHandleValidRequest_Success_NoRejections() {
        // Arrange
        var rejectedByUserId = UUID.randomUUID();

        var request = new GetRejectedUsersByUserRequest(new UserIdDTO(rejectedByUserId));

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectedUsersByUserResults.Success.class, result);
        var successResult = (GetRejectedUsersByUserResults.Success) result;
        assertTrue(successResult.rejections().isEmpty());
    }


    @Test
    void testHandleRequest_InvalidRequest_NullRejectedByUser() {
        var request = new GetRejectedUsersByUserRequest(null );

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectedUsersByUserResults.InvalidRequest.class, result);
    }

    @Test
    void testHandleRequest_InvalidRequest_InvalidUserIdDTO() {
        // Arrange
        var requestId = UUID.randomUUID();
        var rejectedByUserId = new UserIdDTO(null); // Invalid UserIdDTO

        var request = new GetRejectedUsersByUserRequest(rejectedByUserId);

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectedUsersByUserResults.InvalidRequest.class, result);
        var invalidResult = (GetRejectedUsersByUserResults.InvalidRequest) result;
        assertNotNull(invalidResult.error());
    }

    @Test
    void testHandleRequest_Success_OnlyReturnsRejectionsForSpecificUser() {
        // Arrange
        var targetUserId = UUID.randomUUID();
        var otherUserId = UUID.randomUUID();
        var rejectedUserId1 = UUID.randomUUID();
        var rejectedUserId2 = UUID.randomUUID();

        // Create rejection by target user
        var targetEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        targetEntity = rejectedConnectionEntityMother.withSpecificIds(targetEntity, targetUserId, rejectedUserId1);
        repository.save(targetEntity);

        // Create rejection by other user (should not be returned)
        var otherEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        otherEntity = rejectedConnectionEntityMother.withSpecificIds(otherEntity, otherUserId, rejectedUserId2);
        repository.save(otherEntity);

        var request = new GetRejectedUsersByUserRequest(new UserIdDTO(targetUserId));

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectedUsersByUserResults.Success.class, result);
        var successResult = (GetRejectedUsersByUserResults.Success) result;
        assertEquals(1, successResult.rejections().size());
        assertEquals(rejectedUserId1, successResult.rejections().get(0).rejectedUser().value());
        assertEquals(targetUserId, successResult.rejections().get(0).rejectedByUser().value());
    }

    @Test
    void testHandleRequest_Success_WithDifferentReasons() {
        // Arrange
        var rejectedByUserId = UUID.randomUUID();
        var rejectedUserId1 = UUID.randomUUID();
        var rejectedUserId2 = UUID.randomUUID();
        var requestId = UUID.randomUUID();

        // Create rejection with CONNECTION_DECLINED reason
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, rejectedByUserId, rejectedUserId1);
        entity1 = rejectedConnectionEntityMother.withReason(entity1, RejectedConnectionReason.CONNECTION_DECLINED);
        repository.save(entity1);

        // Create rejection with CONNECTION_REMOVED reason
        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, rejectedByUserId, rejectedUserId2);
        entity2 = rejectedConnectionEntityMother.withReason(entity2, RejectedConnectionReason.CONNECTION_REMOVED);
        repository.save(entity2);

        var request = new GetRejectedUsersByUserRequest(new UserIdDTO(rejectedByUserId));

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectedUsersByUserResults.Success.class, result);
        var successResult = (GetRejectedUsersByUserResults.Success) result;
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
    void testHandleRequest_Success_MultipleRejectionsOrderedByCreationTime() {
        // Arrange
        var rejectedByUserId = UUID.randomUUID();
        var rejectedUserId1 = UUID.randomUUID();
        var rejectedUserId2 = UUID.randomUUID();
        var rejectedUserId3 = UUID.randomUUID();
        var requestId = UUID.randomUUID();

        // Create multiple rejections
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, rejectedByUserId, rejectedUserId1);
        repository.save(entity1);

        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, rejectedByUserId, rejectedUserId2);
        repository.save(entity2);

        var entity3 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity3 = rejectedConnectionEntityMother.withSpecificIds(entity3, rejectedByUserId, rejectedUserId3);
        repository.save(entity3);

        var request = new GetRejectedUsersByUserRequest(new UserIdDTO(rejectedByUserId));

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectedUsersByUserResults.Success.class, result);
        var successResult = (GetRejectedUsersByUserResults.Success) result;
        assertEquals(3, successResult.rejections().size());

        // All rejections should be for the correct rejecting user
        successResult.rejections().forEach(rejection ->
            assertEquals(rejectedByUserId, rejection.rejectedByUser().value())
        );
    }

    @Test
    void testTransactionalBehavior() {
        // Arrange
        var rejectedByUserId = UUID.randomUUID();
        var rejectedUserId = UUID.randomUUID();
        var requestId = UUID.randomUUID();

        // Create a rejection
        var entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity = rejectedConnectionEntityMother.withSpecificIds(entity, rejectedByUserId, rejectedUserId);
        repository.save(entity);

        var request = new GetRejectedUsersByUserRequest(new UserIdDTO(rejectedByUserId));

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert - should work correctly within transaction
        assertInstanceOf(GetRejectedUsersByUserResults.Success.class, result);
        var successResult = (GetRejectedUsersByUserResults.Success) result;
        assertEquals(1, successResult.rejections().size());
    }

    @Test
    void testHandleRequest_Success_EmptyResultForNonExistentUser() {
        // Arrange
        var nonExistentUserId = UUID.randomUUID();
        var requestId = UUID.randomUUID();

        // Create some rejections for other users to ensure the query is selective
        var otherEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(otherEntity);

        var request = new GetRejectedUsersByUserRequest(new UserIdDTO(nonExistentUserId));

        // Act
        var result = getRejectedUsersByUserHandler.handle(request);

        // Assert
        assertInstanceOf(GetRejectedUsersByUserResults.Success.class, result);
        var successResult = (GetRejectedUsersByUserResults.Success) result;
        assertTrue(successResult.rejections().isEmpty());
    }
}
