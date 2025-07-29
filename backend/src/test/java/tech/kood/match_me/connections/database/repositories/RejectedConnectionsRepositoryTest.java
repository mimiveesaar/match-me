package tech.kood.match_me.connections.database.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.ConnectionsTestApplication;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.internal.database.entities.RejectedConnectionEntity;
import tech.kood.match_me.connections.internal.database.repositories.RejectedConnectionsRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ConnectionsTestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class RejectedConnectionsRepositoryTest extends ConnectionsTestBase {

    @Autowired
    RejectedConnectionsRepository rejectedConnectionsRepository;

    @BeforeEach
    void setUp() {
        rejectedConnectionsRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindRejectedConnection() {
        // Given
        UUID id = UUID.randomUUID();
        UUID senderUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        UUID rejectedByUserId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        RejectedConnectionEntity rejectedConnection = new RejectedConnectionEntity(id, senderUserId,
                targetUserId, rejectedByUserId, createdAt);

        // When
        rejectedConnectionsRepository.save(rejectedConnection);

        // Then
        Optional<RejectedConnectionEntity> found = rejectedConnectionsRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(id, found.get().id());
        assertEquals(senderUserId, found.get().senderUserId());
        assertEquals(targetUserId, found.get().targetUserId());
        assertEquals(rejectedByUserId, found.get().rejectedByUserId());
    }

    @Test
    void shouldCheckIfConnectionExistsBetweenUsers() {
        // Given
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();
        UUID rejectedByUserId = UUID.randomUUID();

        RejectedConnectionEntity rejectedConnection = new RejectedConnectionEntity(
                UUID.randomUUID(), user1, user2, rejectedByUserId, Instant.now());

        // When
        rejectedConnectionsRepository.save(rejectedConnection);

        // Then - should find connection in both directions
        assertTrue(rejectedConnectionsRepository.existsBetweenUsers(user1, user2));
        assertTrue(rejectedConnectionsRepository.existsBetweenUsers(user2, user1));

        // Should return false for non-existent connections
        UUID user3 = UUID.randomUUID();
        assertFalse(rejectedConnectionsRepository.existsBetweenUsers(user1, user3));
    }

    @Test
    void shouldFindConnectionsBySenderId() {
        // Given
        UUID senderUserId = UUID.randomUUID();
        UUID targetUserId1 = UUID.randomUUID();
        UUID targetUserId2 = UUID.randomUUID();
        UUID rejectedByUserId = UUID.randomUUID();

        // Create connections from the same sender
        RejectedConnectionEntity connection1 = new RejectedConnectionEntity(UUID.randomUUID(),
                senderUserId, targetUserId1, rejectedByUserId, Instant.now().minusSeconds(10));
        RejectedConnectionEntity connection2 = new RejectedConnectionEntity(UUID.randomUUID(),
                senderUserId, targetUserId2, rejectedByUserId, Instant.now());

        rejectedConnectionsRepository.save(connection1);
        rejectedConnectionsRepository.save(connection2);

        // When
        List<RejectedConnectionEntity> connections =
                rejectedConnectionsRepository.findBySenderId(senderUserId);

        // Then
        assertEquals(2, connections.size());
        assertTrue(connections.stream().allMatch(conn -> conn.senderUserId().equals(senderUserId)));
        // Should be ordered by created_at DESC
        assertEquals(connection2.id(), connections.get(0).id());
        assertEquals(connection1.id(), connections.get(1).id());
    }

    @Test
    void shouldDeleteConnectionById() {
        // Given
        UUID id = UUID.randomUUID();
        RejectedConnectionEntity rejectedConnection = new RejectedConnectionEntity(id,
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now());
        rejectedConnectionsRepository.save(rejectedConnection);

        // When
        boolean deleted = rejectedConnectionsRepository.deleteById(id);

        // Then
        assertTrue(deleted);
        assertFalse(rejectedConnectionsRepository.findById(id).isPresent());

        // Should return false when deleting non-existent connection
        assertFalse(rejectedConnectionsRepository.deleteById(UUID.randomUUID()));
    }

    @Test
    void shouldDeleteAllConnectionsByUserId() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        // Create connections where userId is involved in different roles
        RejectedConnectionEntity asSender = new RejectedConnectionEntity(UUID.randomUUID(), userId,
                otherUserId, otherUserId, Instant.now());
        RejectedConnectionEntity asTarget = new RejectedConnectionEntity(UUID.randomUUID(),
                otherUserId, userId, otherUserId, Instant.now());
        RejectedConnectionEntity asRejector = new RejectedConnectionEntity(UUID.randomUUID(),
                otherUserId, otherUserId, userId, Instant.now());

        // Create connection not involving userId
        RejectedConnectionEntity unrelated = new RejectedConnectionEntity(UUID.randomUUID(),
                otherUserId, otherUserId, otherUserId, Instant.now());

        rejectedConnectionsRepository.save(asSender);
        rejectedConnectionsRepository.save(asTarget);
        rejectedConnectionsRepository.save(asRejector);
        rejectedConnectionsRepository.save(unrelated);

        // When
        int deletedCount = rejectedConnectionsRepository.deleteByUserId(userId);

        // Then
        assertEquals(3, deletedCount);
        assertTrue(rejectedConnectionsRepository.findById(unrelated.id()).isPresent());
        assertFalse(rejectedConnectionsRepository.findById(asSender.id()).isPresent());
        assertFalse(rejectedConnectionsRepository.findById(asTarget.id()).isPresent());
        assertFalse(rejectedConnectionsRepository.findById(asRejector.id()).isPresent());
    }

    @Test
    void shouldCountConnectionsByUserId() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        // Create connections where userId is involved in different roles
        RejectedConnectionEntity asSender = new RejectedConnectionEntity(UUID.randomUUID(), userId,
                otherUserId, otherUserId, Instant.now());
        RejectedConnectionEntity asTarget = new RejectedConnectionEntity(UUID.randomUUID(),
                otherUserId, userId, otherUserId, Instant.now());
        RejectedConnectionEntity asRejector = new RejectedConnectionEntity(UUID.randomUUID(),
                otherUserId, otherUserId, userId, Instant.now());

        rejectedConnectionsRepository.save(asSender);
        rejectedConnectionsRepository.save(asTarget);
        rejectedConnectionsRepository.save(asRejector);

        // When
        int count = rejectedConnectionsRepository.countByUserId(userId);

        // Then
        assertEquals(3, count);

        // Should return 0 for user with no connections
        assertEquals(0, rejectedConnectionsRepository.countByUserId(UUID.randomUUID()));
    }
}
