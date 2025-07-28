package tech.kood.match_me.connections.database.repositories;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.ConnectionsTestApplication;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.database.entities.AcceptedConnectionEntity;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ConnectionsTestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class AcceptedConnectionsRepositoryTest extends ConnectionsTestBase {

    @Autowired
    private AcceptedConnectionsRepository acceptedConnectionsRepository;

    @Autowired
    @Qualifier("connectionsFlyway")
    private Flyway connectionsFlyway;

    @Test
    void shouldSaveAndFindAcceptedConnection() {
        // Given
        UUID id = UUID.randomUUID();
        UUID senderUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        AcceptedConnectionEntity connection =
                new AcceptedConnectionEntity(id, senderUserId, targetUserId, createdAt);

        // When
        acceptedConnectionsRepository.save(connection);
        var found = acceptedConnectionsRepository.findById(id);

        // Then
        assertTrue(found.isPresent());
        assertEquals(id, found.get().id());
        assertEquals(senderUserId, found.get().senderUserId());
        assertEquals(targetUserId, found.get().targetUserId());
        assertEquals(createdAt.truncatedTo(ChronoUnit.SECONDS),
                found.get().createdAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void shouldFindConnectionsBySenderUserId() {
        // Given
        UUID senderUserId = UUID.randomUUID();
        UUID targetUserId1 = UUID.randomUUID();
        UUID targetUserId2 = UUID.randomUUID();

        AcceptedConnectionEntity connection1 = new AcceptedConnectionEntity(UUID.randomUUID(),
                senderUserId, targetUserId1, Instant.now());
        AcceptedConnectionEntity connection2 = new AcceptedConnectionEntity(UUID.randomUUID(),
                senderUserId, targetUserId2, Instant.now());

        acceptedConnectionsRepository.save(connection1);
        acceptedConnectionsRepository.save(connection2);

        // When
        List<AcceptedConnectionEntity> connections =
                acceptedConnectionsRepository.findBySenderUserId(senderUserId);

        // Then
        assertEquals(2, connections.size());
        assertTrue(connections.stream().allMatch(c -> c.senderUserId().equals(senderUserId)));
    }

    @Test
    void shouldCheckIfConnectionExistsBetweenUsers() {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        AcceptedConnectionEntity connection =
                new AcceptedConnectionEntity(UUID.randomUUID(), userId1, userId2, Instant.now());

        acceptedConnectionsRepository.save(connection);

        // When & Then
        assertTrue(acceptedConnectionsRepository.existsBetweenUsers(userId1, userId2));
        assertTrue(acceptedConnectionsRepository.existsBetweenUsers(userId2, userId1)); // Should
                                                                                        // work
                                                                                        // bidirectionally
        assertFalse(acceptedConnectionsRepository.existsBetweenUsers(UUID.randomUUID(), userId1));
    }

    @Test
    void shouldDeleteConnectionById() {
        // Given
        UUID id = UUID.randomUUID();
        AcceptedConnectionEntity connection = new AcceptedConnectionEntity(id, UUID.randomUUID(),
                UUID.randomUUID(), Instant.now());

        acceptedConnectionsRepository.save(connection);
        assertTrue(acceptedConnectionsRepository.findById(id).isPresent());

        // When
        boolean deleted = acceptedConnectionsRepository.deleteById(id);

        // Then
        assertTrue(deleted);
        assertFalse(acceptedConnectionsRepository.findById(id).isPresent());
    }

    @Test
    void shouldCountConnectionsByUserId() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID otherUser1 = UUID.randomUUID();
        UUID otherUser2 = UUID.randomUUID();

        // User as sender
        AcceptedConnectionEntity connection1 =
                new AcceptedConnectionEntity(UUID.randomUUID(), userId, otherUser1, Instant.now());
        // User as target
        AcceptedConnectionEntity connection2 =
                new AcceptedConnectionEntity(UUID.randomUUID(), otherUser2, userId, Instant.now());

        acceptedConnectionsRepository.save(connection1);
        acceptedConnectionsRepository.save(connection2);

        // When
        int count = acceptedConnectionsRepository.countByUserId(userId);

        // Then
        assertEquals(2, count);
    }
}
