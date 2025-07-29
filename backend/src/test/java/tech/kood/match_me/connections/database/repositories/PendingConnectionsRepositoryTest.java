package tech.kood.match_me.connections.database.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.ConnectionsTestApplication;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.internal.database.entities.PendingConnectionEntity;
import tech.kood.match_me.connections.internal.database.repositories.PendingConnectionsRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ConnectionsTestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class PendingConnectionsRepositoryTest extends ConnectionsTestBase {

    @Autowired
    PendingConnectionsRepository pendingConnectionsRepository;

    @Test
    void shouldSaveAndFindPendingConnection() {
        // Given
        UUID id = UUID.randomUUID();
        UUID senderUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        PendingConnectionEntity pendingConnection =
                new PendingConnectionEntity(id, senderUserId, targetUserId, createdAt);

        // When
        pendingConnectionsRepository.save(pendingConnection);

        // Then
        var found = pendingConnectionsRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(senderUserId, found.get().senderUserId());
        assertEquals(targetUserId, found.get().targetUserId());
    }

    @Test
    void shouldFindBySenderUserId() {
        // Given
        UUID senderUserId = UUID.randomUUID();
        UUID targetUserId1 = UUID.randomUUID();
        UUID targetUserId2 = UUID.randomUUID();
        Instant createdAt = Instant.now();

        PendingConnectionEntity connection1 = new PendingConnectionEntity(UUID.randomUUID(),
                senderUserId, targetUserId1, createdAt);
        PendingConnectionEntity connection2 = new PendingConnectionEntity(UUID.randomUUID(),
                senderUserId, targetUserId2, createdAt);

        pendingConnectionsRepository.save(connection1);
        pendingConnectionsRepository.save(connection2);

        // When
        List<PendingConnectionEntity> connections =
                pendingConnectionsRepository.findBySenderUserId(senderUserId);

        // Then
        assertEquals(2, connections.size());
        assertTrue(connections.stream().allMatch(conn -> conn.senderUserId().equals(senderUserId)));
    }

    @Test
    void shouldFindByTargetUserId() {
        // Given
        UUID senderUserId1 = UUID.randomUUID();
        UUID senderUserId2 = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        PendingConnectionEntity connection1 = new PendingConnectionEntity(UUID.randomUUID(),
                senderUserId1, targetUserId, createdAt);
        PendingConnectionEntity connection2 = new PendingConnectionEntity(UUID.randomUUID(),
                senderUserId2, targetUserId, createdAt);

        pendingConnectionsRepository.save(connection1);
        pendingConnectionsRepository.save(connection2);

        // When
        List<PendingConnectionEntity> connections =
                pendingConnectionsRepository.findByTargetUserId(targetUserId);

        // Then
        assertEquals(2, connections.size());
        assertTrue(connections.stream().allMatch(conn -> conn.targetUserId().equals(targetUserId)));
    }

    @Test
    void shouldCheckIfExistsBetweenUsers() {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        Instant createdAt = Instant.now();

        PendingConnectionEntity connection =
                new PendingConnectionEntity(UUID.randomUUID(), userId1, userId2, createdAt);

        pendingConnectionsRepository.save(connection);

        // When & Then
        assertTrue(pendingConnectionsRepository.existsBetweenUsers(userId1, userId2));
        assertTrue(pendingConnectionsRepository.existsBetweenUsers(userId2, userId1));
        assertFalse(pendingConnectionsRepository.existsBetweenUsers(UUID.randomUUID(), userId1));
    }

    @Test
    void shouldDeleteById() {
        // Given
        UUID id = UUID.randomUUID();
        UUID senderUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        PendingConnectionEntity connection =
                new PendingConnectionEntity(id, senderUserId, targetUserId, createdAt);

        pendingConnectionsRepository.save(connection);

        // When
        boolean deleted = pendingConnectionsRepository.deleteById(id);

        // Then
        assertTrue(deleted);
        assertFalse(pendingConnectionsRepository.findById(id).isPresent());
    }

    @Test
    void shouldCountBySenderUserId() {
        // Given
        UUID senderUserId = UUID.randomUUID();
        UUID targetUserId1 = UUID.randomUUID();
        UUID targetUserId2 = UUID.randomUUID();
        Instant createdAt = Instant.now();

        PendingConnectionEntity connection1 = new PendingConnectionEntity(UUID.randomUUID(),
                senderUserId, targetUserId1, createdAt);
        PendingConnectionEntity connection2 = new PendingConnectionEntity(UUID.randomUUID(),
                senderUserId, targetUserId2, createdAt);

        pendingConnectionsRepository.save(connection1);
        pendingConnectionsRepository.save(connection2);

        // When
        long count = pendingConnectionsRepository.countBySenderUserId(senderUserId);

        // Then
        assertEquals(2L, count);
    }
}
