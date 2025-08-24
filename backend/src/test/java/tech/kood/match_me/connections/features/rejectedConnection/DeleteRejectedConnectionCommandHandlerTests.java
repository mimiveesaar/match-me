package tech.kood.match_me.connections.features.rejectedConnection;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.common.api.ConnectionId;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.deleteRejectedConnection.api.DeleteRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DeleteRejectedConnectionCommandHandlerTests extends ConnectionsTestBase {

    @Autowired
    private DeleteRejectedConnectionCommandHandler deleteRejectedConnectionHandler;

    @Autowired
    private RejectedConnectionRepository repository;

    @Autowired
    private RejectedConnectionEntityMother rejectedConnectionEntityMother;

    @Test
    void testHandleValidRequest_Success() {
        // Arrange: Create a rejected connection first
        var rejectedConnectionEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(rejectedConnectionEntity);

        var connectionId = new ConnectionId(rejectedConnectionEntity.getId());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new DeleteRejectedConnectionRequest(requestId, connectionId, tracingId);

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnectionResults.Success.class, result);
        var successResult = (DeleteRejectedConnectionResults.Success) result;
        assertEquals(requestId, successResult.requestId());
        assertEquals(tracingId, successResult.tracingId());

        // Verify the rejected connection was actually deleted from the repository
        var deletedConnection = repository.findById(rejectedConnectionEntity.getId());
        assertTrue(deletedConnection.isEmpty());
    }

    @Test
    void testHandleRequest_NotFound() {
        // Arrange: Use a non-existent connection ID
        var nonExistentConnectionId = new ConnectionId(UUID.randomUUID());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new DeleteRejectedConnectionRequest(requestId, nonExistentConnectionId, tracingId);

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnectionResults.NotFound.class, result);
    }

    @Test
    void testHandleRequest_NullId() {
        // This test would require mocking the repository to throw an exception
        // For now, we'll test with a null connection ID to trigger validation error
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        // Using null for connectionId should trigger a system error due to validation
        var request = new DeleteRejectedConnectionRequest(requestId, new ConnectionId(null), tracingId);

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnectionResults.InvalidRequest.class, result);

    }

    @Test
    void testHandleMultipleDeleteRequests_FirstSucceedsSecondNotFound() {
        // Arrange: Create a rejected connection
        var rejectedConnectionEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(rejectedConnectionEntity);

        var connectionId = new ConnectionId(rejectedConnectionEntity.getId());
        var requestId1 = UUID.randomUUID();
        var requestId2 = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request1 = new DeleteRejectedConnectionRequest(requestId1, connectionId, tracingId);
        var request2 = new DeleteRejectedConnectionRequest(requestId2, connectionId, tracingId);

        // Act: First deletion
        var result1 = deleteRejectedConnectionHandler.handle(request1);

        // Assert: First deletion succeeds
        assertInstanceOf(DeleteRejectedConnectionResults.Success.class, result1);

        // Act: Second deletion attempt
        var result2 = deleteRejectedConnectionHandler.handle(request2);

        // Assert: Second deletion returns NotFound
        assertInstanceOf(DeleteRejectedConnectionResults.NotFound.class, result2);
        var notFoundResult = (DeleteRejectedConnectionResults.NotFound) result2;
        assertEquals(requestId2, notFoundResult.requestId());
        assertEquals(tracingId, notFoundResult.tracingId());
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

        var connectionId1 = new ConnectionId(entity1.getId());
        var requestId = UUID.randomUUID();

        var request = new DeleteRejectedConnectionRequest(requestId, connectionId1, "test-trace");

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnectionResults.Success.class, result);

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

        var connectionId = new ConnectionId(rejectedConnectionEntity.getId());
        var requestId = UUID.randomUUID();
        var tracingId = "test-tracing-id";

        var request = new DeleteRejectedConnectionRequest(requestId, connectionId, tracingId);

        // Count before deletion
        var initialCount = repository.findAll().size();

        // Act
        var result = deleteRejectedConnectionHandler.handle(request);

        // Assert
        assertInstanceOf(DeleteRejectedConnectionResults.Success.class, result);

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

        var connectionId1 = new ConnectionId(entity1.getId());
        var connectionId2 = new ConnectionId(entity2.getId());

        var request1 = new DeleteRejectedConnectionRequest(UUID.randomUUID(), connectionId1, "trace-1");
        var request2 = new DeleteRejectedConnectionRequest(UUID.randomUUID(), connectionId2, "trace-2");

        // Act
        var result1 = deleteRejectedConnectionHandler.handle(request1);
        var result2 = deleteRejectedConnectionHandler.handle(request2);

        // Assert
        assertInstanceOf(DeleteRejectedConnectionResults.Success.class, result1);
        assertInstanceOf(DeleteRejectedConnectionResults.Success.class, result2);

        // Verify both entities were deleted
        assertTrue(repository.findById(entity1.getId()).isEmpty());
        assertTrue(repository.findById(entity2.getId()).isEmpty());
    }

    @Test
    void testHandleRequest_ConcurrentDeletion() {
        // Arrange: Create a rejected connection
        var rejectedConnectionEntity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(rejectedConnectionEntity);

        var connectionId = new ConnectionId(rejectedConnectionEntity.getId());

        // Simulate concurrent deletion requests
        var request1 = new DeleteRejectedConnectionRequest(UUID.randomUUID(), connectionId, "trace-1");
        var request2 = new DeleteRejectedConnectionRequest(UUID.randomUUID(), connectionId, "trace-2");

        // Act: Execute requests (in real concurrent scenario, timing would matter)
        var result1 = deleteRejectedConnectionHandler.handle(request1);
        var result2 = deleteRejectedConnectionHandler.handle(request2);

        // Assert: One should succeed, one should return NotFound
        assertTrue(
            (result1 instanceof DeleteRejectedConnectionResults.Success &&
             result2 instanceof DeleteRejectedConnectionResults.NotFound) ||
            (result1 instanceof DeleteRejectedConnectionResults.NotFound &&
             result2 instanceof DeleteRejectedConnectionResults.Success)
        );

        // Verify the connection is deleted
        assertTrue(repository.findById(rejectedConnectionEntity.getId()).isEmpty());
    }
}
