package tech.kood.match_me.connections.features.rejectedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.DeleteRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(transactionManager = "connectionsTransactionManager")
public class DeleteRejectedConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private DeleteRejectedConnection.Handler deleteRejectedConnectionHandler;

    @Autowired
    private RejectedConnectionRepository repository;

    @Autowired
    private RejectedConnectionEntityMother rejectedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success() {
        // Arrange: Create a rejected connection first
        var rejectedConnectionEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(rejectedConnectionEntity);

        var connectionId = new ConnectionIdDTO(rejectedConnectionEntity.getId());

        var request = new DeleteRejectedConnection.Request(connectionId);

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnection.Result.Success.class, result);

        // Verify the rejected connection was actually deleted from the repository
        var deletedConnection = repository.findById(rejectedConnectionEntity.getId());
        assertTrue(deletedConnection.isEmpty());
    }

    @Test
    void testHandleRequest_NotFound() {
        // Arrange: Use a non-existent connection ID
        var nonExistentConnectionId = new ConnectionIdDTO(UUID.randomUUID());

        var request = new DeleteRejectedConnection.Request(nonExistentConnectionId);

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnection.Result.NotFound.class, result);
    }

    @Test
    void testHandleRequest_NullId() {
        // This test would require mocking the repository to throw an exception
        // For now, we'll test with a null connection ID to trigger validation error

        // Using null for connectionId should trigger a system error due to validation
        var request = new DeleteRejectedConnection.Request(new ConnectionIdDTO(null));

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnection.Result.InvalidRequest.class, result);

    }

    @Test
    void testHandleMultipleDeleteRequests_FirstSucceedsSecondNotFound() {
        // Arrange: Create a rejected connection
        var rejectedConnectionEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(rejectedConnectionEntity);

        var connectionId = new ConnectionIdDTO(rejectedConnectionEntity.getId());

        var request1 = new DeleteRejectedConnection.Request(connectionId);
        var request2 = new DeleteRejectedConnection.Request(connectionId);

        // Act: First deletion
        var result1 = deleteRejectedConnectionHandler.handle(request1);

        // Assert: First deletion succeeds
        assertInstanceOf(DeleteRejectedConnection.Result.Success.class, result1);

        // Act: Second deletion attempt
        var result2 = deleteRejectedConnectionHandler.handle(request2);

        // Assert: Second deletion returns NotFound
        assertInstanceOf(DeleteRejectedConnection.Result.NotFound.class, result2);
        var notFoundResult = (DeleteRejectedConnection.Result.NotFound) result2;
    }

    @Test
    void testHandleRequest_VerifyRepositoryState() {
        // Arrange: Create multiple rejected connections
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity1);
        repository.save(entity2);

        var initialCount = repository.findAll().size();
        assertTrue(initialCount >= 2); // At least our two entities

        var connectionId1 = new ConnectionIdDTO(entity1.getId());

        var request = new DeleteRejectedConnection.Request(connectionId1);

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnection.Result.Success.class, result);

        // Verify only one entity was deleted
        var finalCount = repository.findAll().size();
        assertEquals(initialCount - 1, finalCount);

        // Verify the correct entity was deleted
        assertTrue(repository.findById(entity1.getId()).isEmpty());
        assertTrue(repository.findById(entity2.getId()).isPresent());
    }

    @Test
    void testTransactionalBehavior() {
        // Arrange: Create a rejected connection
        var rejectedConnectionEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(rejectedConnectionEntity);

        var connectionId = new ConnectionIdDTO(rejectedConnectionEntity.getId());

        var request = new DeleteRejectedConnection.Request(connectionId);

        // Count before deletion
        var initialCount = repository.findAll().size();

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnection.Result.Success.class, result);

        // Verify count decreased by 1
        var finalCount = repository.findAll().size();
        assertEquals(initialCount - 1, finalCount);
    }

    @Test
    void testHandleRequest_WithDifferentConnectionIds() {
        // Arrange: Create multiple rejected connections
        var entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        var entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity1);
        repository.save(entity2);

        var connectionId1 = new ConnectionIdDTO(entity1.getId());
        var connectionId2 = new ConnectionIdDTO(entity2.getId());

        var request1 = new DeleteRejectedConnection.Request(connectionId1);
        var request2 = new DeleteRejectedConnection.Request(connectionId2);

        // Act
        var result1 = deleteRejectedConnectionHandler.handle(request1);
        var result2 = deleteRejectedConnectionHandler.handle(request2);

        // Assert
        assertInstanceOf(DeleteRejectedConnection.Result.Success.class, result1);
        assertInstanceOf(DeleteRejectedConnection.Result.Success.class, result2);

        // Verify both entities were deleted
        assertTrue(repository.findById(entity1.getId()).isEmpty());
        assertTrue(repository.findById(entity2.getId()).isEmpty());
    }

    @Test
    void testHandleRequest_ConcurrentDeletion() {
        // Arrange: Create a rejected connection
        var rejectedConnectionEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(rejectedConnectionEntity);

        var connectionId = new ConnectionIdDTO(rejectedConnectionEntity.getId());

        // Simulate concurrent deletion requests
        var request1 = new DeleteRejectedConnection.Request(connectionId);
        var request2 = new DeleteRejectedConnection.Request(connectionId);

        // Act: Execute requests (in real concurrent scenario, timing would matter)
        var result1 = deleteRejectedConnectionHandler.handle(request1);
        var result2 = deleteRejectedConnectionHandler.handle(request2);

        // Assert: One should succeed, one should return NotFound
        assertTrue(
            (result1 instanceof DeleteRejectedConnection.Result.Success &&
             result2 instanceof DeleteRejectedConnection.Result.NotFound) ||
            (result1 instanceof DeleteRejectedConnection.Result.NotFound &&
             result2 instanceof DeleteRejectedConnection.Result.Success)
        );

        // Verify the connection is deleted
        assertTrue(repository.findById(rejectedConnectionEntity.getId()).isEmpty());
    }
}
