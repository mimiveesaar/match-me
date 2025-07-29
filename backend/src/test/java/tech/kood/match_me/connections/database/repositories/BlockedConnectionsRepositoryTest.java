package tech.kood.match_me.connections.database.repositories;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.ConnectionsTestApplication;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.internal.database.entities.BlockedConnectionEntity;
import tech.kood.match_me.connections.internal.database.repositories.BlockedConnectionsRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ConnectionsTestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class BlockedConnectionsRepositoryTest extends ConnectionsTestBase {

    @Autowired
    private BlockedConnectionsRepository blockedConnectionsRepository;

    @Autowired
    @Qualifier("connectionsFlyway")
    private Flyway connectionsFlyway;

    @Test
    void shouldSaveAndFindBlockedConnection() {
        // Given
        UUID id = UUID.randomUUID();
        UUID blockerUserId = UUID.randomUUID();
        UUID blockedUserId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        BlockedConnectionEntity blockedConnection =
                new BlockedConnectionEntity(id, blockerUserId, blockedUserId, createdAt);

        // When
        blockedConnectionsRepository.save(blockedConnection);
        Optional<BlockedConnectionEntity> found = blockedConnectionsRepository.findById(id);

        // Then
        assertTrue(found.isPresent());
        assertEquals(id, found.get().id());
        assertEquals(blockerUserId, found.get().blockerUserId());
        assertEquals(blockedUserId, found.get().blockedUserId());
        assertEquals(createdAt.truncatedTo(ChronoUnit.SECONDS),
                found.get().createdAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void shouldUpdateExistingBlockedConnectionOnConflict() {
        // Given
        UUID id = UUID.randomUUID();
        UUID originalBlockerUserId = UUID.randomUUID();
        UUID originalBlockedUserId = UUID.randomUUID();
        UUID newBlockerUserId = UUID.randomUUID();
        UUID newBlockedUserId = UUID.randomUUID();
        Instant originalCreatedAt = Instant.now().minus(1, ChronoUnit.HOURS);
        Instant newCreatedAt = Instant.now();

        BlockedConnectionEntity originalConnection = new BlockedConnectionEntity(id,
                originalBlockerUserId, originalBlockedUserId, originalCreatedAt);
        BlockedConnectionEntity updatedConnection =
                new BlockedConnectionEntity(id, newBlockerUserId, newBlockedUserId, newCreatedAt);

        // When
        blockedConnectionsRepository.save(originalConnection);
        blockedConnectionsRepository.save(updatedConnection);
        Optional<BlockedConnectionEntity> found = blockedConnectionsRepository.findById(id);

        // Then
        assertTrue(found.isPresent());
        assertEquals(id, found.get().id());
        assertEquals(newBlockerUserId, found.get().blockerUserId());
        assertEquals(newBlockedUserId, found.get().blockedUserId());
        assertEquals(newCreatedAt.truncatedTo(ChronoUnit.SECONDS),
                found.get().createdAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void shouldReturnEmptyWhenBlockedConnectionNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        Optional<BlockedConnectionEntity> found =
                blockedConnectionsRepository.findById(nonExistentId);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void shouldCheckIfUserIsBlocked() {
        // Given
        UUID blockerUserId = UUID.randomUUID();
        UUID blockedUserId = UUID.randomUUID();
        UUID unblockedUserId = UUID.randomUUID();

        BlockedConnectionEntity blockedConnection = new BlockedConnectionEntity(UUID.randomUUID(),
                blockerUserId, blockedUserId, Instant.now());

        blockedConnectionsRepository.save(blockedConnection);

        // When & Then
        assertTrue(blockedConnectionsRepository.isBlocked(blockerUserId, blockedUserId));
        assertFalse(blockedConnectionsRepository.isBlocked(blockedUserId, blockerUserId)); // Reverse
                                                                                           // direction
        assertFalse(blockedConnectionsRepository.isBlocked(blockerUserId, unblockedUserId));
        assertFalse(blockedConnectionsRepository.isBlocked(unblockedUserId, blockedUserId));
    }

    @Test
    void shouldCheckIfBlockExistsBetweenUsers() {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        UUID userId3 = UUID.randomUUID();

        BlockedConnectionEntity blockedConnection =
                new BlockedConnectionEntity(UUID.randomUUID(), userId1, userId2, Instant.now());

        blockedConnectionsRepository.save(blockedConnection);

        // When & Then
        assertTrue(blockedConnectionsRepository.existsBetweenUsers(userId1, userId2));
        assertTrue(blockedConnectionsRepository.existsBetweenUsers(userId2, userId1)); // Should
                                                                                       // work
                                                                                       // bidirectionally
        assertFalse(blockedConnectionsRepository.existsBetweenUsers(userId1, userId3));
        assertFalse(blockedConnectionsRepository.existsBetweenUsers(userId2, userId3));
    }

    @Test
    void shouldCheckIfBlockExistsBetweenUsersWithBothDirections() {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        // Create blocks in both directions
        BlockedConnectionEntity block1 =
                new BlockedConnectionEntity(UUID.randomUUID(), userId1, userId2, Instant.now());

        blockedConnectionsRepository.save(block1);

        // When & Then
        assertTrue(blockedConnectionsRepository.existsBetweenUsers(userId1, userId2));
        assertTrue(blockedConnectionsRepository.existsBetweenUsers(userId2, userId1));
    }

    @Test
    void shouldFindConnectionsByBlockerUserId() {
        // Given
        UUID blockerUserId = UUID.randomUUID();
        UUID blockedUserId1 = UUID.randomUUID();
        UUID blockedUserId2 = UUID.randomUUID();
        UUID otherBlockerUserId = UUID.randomUUID();

        Instant now = Instant.now();
        Instant earlier = now.minus(1, ChronoUnit.HOURS);

        BlockedConnectionEntity connection1 =
                new BlockedConnectionEntity(UUID.randomUUID(), blockerUserId, blockedUserId1, now);
        BlockedConnectionEntity connection2 = new BlockedConnectionEntity(UUID.randomUUID(),
                blockerUserId, blockedUserId2, earlier);
        BlockedConnectionEntity otherConnection = new BlockedConnectionEntity(UUID.randomUUID(),
                otherBlockerUserId, blockedUserId1, now);

        blockedConnectionsRepository.save(connection1);
        blockedConnectionsRepository.save(connection2);
        blockedConnectionsRepository.save(otherConnection);

        // When
        List<BlockedConnectionEntity> connections =
                blockedConnectionsRepository.findByBlockerUserId(blockerUserId);

        // Then
        assertEquals(2, connections.size());
        assertTrue(connections.stream().allMatch(c -> c.blockerUserId().equals(blockerUserId)));

        // Should be ordered by created_at DESC (newest first)
        assertEquals(now.truncatedTo(ChronoUnit.SECONDS),
                connections.get(0).createdAt().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(earlier.truncatedTo(ChronoUnit.SECONDS),
                connections.get(1).createdAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void shouldFindConnectionsByBlockedUserId() {
        // Given
        UUID blockedUserId = UUID.randomUUID();
        UUID blockerUserId1 = UUID.randomUUID();
        UUID blockerUserId2 = UUID.randomUUID();
        UUID otherBlockedUserId = UUID.randomUUID();

        Instant now = Instant.now();
        Instant earlier = now.minus(1, ChronoUnit.HOURS);

        BlockedConnectionEntity connection1 =
                new BlockedConnectionEntity(UUID.randomUUID(), blockerUserId1, blockedUserId, now);
        BlockedConnectionEntity connection2 = new BlockedConnectionEntity(UUID.randomUUID(),
                blockerUserId2, blockedUserId, earlier);
        BlockedConnectionEntity otherConnection = new BlockedConnectionEntity(UUID.randomUUID(),
                blockerUserId1, otherBlockedUserId, now);

        blockedConnectionsRepository.save(connection1);
        blockedConnectionsRepository.save(connection2);
        blockedConnectionsRepository.save(otherConnection);

        // When
        List<BlockedConnectionEntity> connections =
                blockedConnectionsRepository.findByBlockedUserId(blockedUserId);

        // Then
        assertEquals(2, connections.size());
        assertTrue(connections.stream().allMatch(c -> c.blockedUserId().equals(blockedUserId)));

        // Should be ordered by created_at DESC (newest first)
        assertEquals(now.truncatedTo(ChronoUnit.SECONDS),
                connections.get(0).createdAt().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(earlier.truncatedTo(ChronoUnit.SECONDS),
                connections.get(1).createdAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void shouldRemoveBlockSuccessfully() {
        // Given
        UUID blockerUserId = UUID.randomUUID();
        UUID blockedUserId = UUID.randomUUID();

        BlockedConnectionEntity blockedConnection = new BlockedConnectionEntity(UUID.randomUUID(),
                blockerUserId, blockedUserId, Instant.now());

        blockedConnectionsRepository.save(blockedConnection);

        // When
        boolean removed = blockedConnectionsRepository.removeBlock(blockerUserId, blockedUserId);

        // Then
        assertTrue(removed);
        assertFalse(blockedConnectionsRepository.isBlocked(blockerUserId, blockedUserId));
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistentBlock() {
        // Given
        UUID blockerUserId = UUID.randomUUID();
        UUID blockedUserId = UUID.randomUUID();

        // When
        boolean removed = blockedConnectionsRepository.removeBlock(blockerUserId, blockedUserId);

        // Then
        assertFalse(removed);
    }

    @Test
    void shouldDeleteConnectionById() {
        // Given
        UUID id = UUID.randomUUID();
        UUID blockerUserId = UUID.randomUUID();
        UUID blockedUserId = UUID.randomUUID();

        BlockedConnectionEntity blockedConnection =
                new BlockedConnectionEntity(id, blockerUserId, blockedUserId, Instant.now());

        blockedConnectionsRepository.save(blockedConnection);

        // When
        boolean deleted = blockedConnectionsRepository.deleteById(id);

        // Then
        assertTrue(deleted);
        assertFalse(blockedConnectionsRepository.findById(id).isPresent());
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistentConnectionById() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        boolean deleted = blockedConnectionsRepository.deleteById(nonExistentId);

        // Then
        assertFalse(deleted);
    }

    @Test
    void shouldDeleteConnectionsByUserId() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID otherUserId1 = UUID.randomUUID();
        UUID otherUserId2 = UUID.randomUUID();
        UUID unrelatedUserId = UUID.randomUUID();

        // Create connections where userId is blocker
        BlockedConnectionEntity connection1 =
                new BlockedConnectionEntity(UUID.randomUUID(), userId, otherUserId1, Instant.now());
        // Create connections where userId is blocked
        BlockedConnectionEntity connection2 =
                new BlockedConnectionEntity(UUID.randomUUID(), otherUserId2, userId, Instant.now());
        // Create unrelated connection
        BlockedConnectionEntity unrelatedConnection = new BlockedConnectionEntity(UUID.randomUUID(),
                unrelatedUserId, otherUserId1, Instant.now());

        blockedConnectionsRepository.save(connection1);
        blockedConnectionsRepository.save(connection2);
        blockedConnectionsRepository.save(unrelatedConnection);

        // When
        int deletedCount = blockedConnectionsRepository.deleteByUserId(userId);

        // Then
        assertEquals(2, deletedCount);
        assertFalse(blockedConnectionsRepository.isBlocked(userId, otherUserId1));
        assertFalse(blockedConnectionsRepository.isBlocked(otherUserId2, userId));
        assertTrue(blockedConnectionsRepository.isBlocked(unrelatedUserId, otherUserId1)); // Should
                                                                                           // remain
    }


    @Test
    void shouldCountConnectionsByUserId() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID otherUserId1 = UUID.randomUUID();
        UUID otherUserId2 = UUID.randomUUID();
        UUID unrelatedUserId = UUID.randomUUID();

        // Create connections where userId is blocker
        BlockedConnectionEntity connection1 =
                new BlockedConnectionEntity(UUID.randomUUID(), userId, otherUserId1, Instant.now());
        // Create connections where userId is blocked
        BlockedConnectionEntity connection2 =
                new BlockedConnectionEntity(UUID.randomUUID(), otherUserId2, userId, Instant.now());
        // Create unrelated connection
        BlockedConnectionEntity unrelatedConnection = new BlockedConnectionEntity(UUID.randomUUID(),
                unrelatedUserId, otherUserId1, Instant.now());

        blockedConnectionsRepository.save(connection1);
        blockedConnectionsRepository.save(connection2);
        blockedConnectionsRepository.save(unrelatedConnection);

        // When
        int count = blockedConnectionsRepository.countByUserId(userId);

        // Then
        assertEquals(2, count);
    }

    @Test
    void shouldReturnZeroCountForUserWithNoConnections() {
        // Given
        UUID userIdWithNoConnections = UUID.randomUUID();

        // When
        int count = blockedConnectionsRepository.countByUserId(userIdWithNoConnections);

        // Then
        assertEquals(0, count);
    }

    @Test
    void shouldDeleteAllConnections() {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        UUID userId3 = UUID.randomUUID();

        BlockedConnectionEntity connection1 =
                new BlockedConnectionEntity(UUID.randomUUID(), userId1, userId2, Instant.now());
        BlockedConnectionEntity connection2 =
                new BlockedConnectionEntity(UUID.randomUUID(), userId2, userId3, Instant.now());
        BlockedConnectionEntity connection3 =
                new BlockedConnectionEntity(UUID.randomUUID(), userId3, userId1, Instant.now());

        blockedConnectionsRepository.save(connection1);
        blockedConnectionsRepository.save(connection2);
        blockedConnectionsRepository.save(connection3);

        // When
        int deletedCount = blockedConnectionsRepository.deleteAll();

        // Then
        assertEquals(3, deletedCount);
        assertFalse(blockedConnectionsRepository.isBlocked(userId1, userId2));
        assertFalse(blockedConnectionsRepository.isBlocked(userId2, userId3));
        assertFalse(blockedConnectionsRepository.isBlocked(userId3, userId1));
    }

    @Test
    void shouldHandleEmptyResultsGracefully() {
        // Given
        UUID nonExistentUserId = UUID.randomUUID();

        // When & Then
        List<BlockedConnectionEntity> blockerConnections =
                blockedConnectionsRepository.findByBlockerUserId(nonExistentUserId);
        List<BlockedConnectionEntity> blockedConnections =
                blockedConnectionsRepository.findByBlockedUserId(nonExistentUserId);

        assertTrue(blockerConnections.isEmpty());
        assertTrue(blockedConnections.isEmpty());
    }
}
