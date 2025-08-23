package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRowMapper;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PendingConnectionRepositoryTests {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private PendingConnectionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PendingConnectionRepository(jdbcTemplate, new PendingConnectionRowMapper());
        repository.deleteAll();
    }

    private PendingConnectionEntity createEntity(UUID id, UUID senderId, UUID targetId, Instant createdAt) {
        return new PendingConnectionEntity(id, senderId, targetId, createdAt);
    }

    @Test
    void testSaveAndFindById() {
        UUID id = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();
        Instant now = Instant.now();
        PendingConnectionEntity entity = createEntity(id, senderId, targetId, now);
        repository.save(entity);
        Optional<PendingConnectionEntity> found = repository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(id, found.get().getId());
    }

    @Test
    void testFindBySender() {
        UUID senderId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();
        PendingConnectionEntity entity = createEntity(UUID.randomUUID(), senderId, targetId, Instant.now());
        repository.save(entity);
        List<PendingConnectionEntity> found = repository.findBySender(senderId);
        assertFalse(found.isEmpty());
        assertEquals(senderId, found.get(0).getSenderId());
    }

    @Test
    void testFindByTarget() {
        UUID senderId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();
        PendingConnectionEntity entity = createEntity(UUID.randomUUID(), senderId, targetId, Instant.now());
        repository.save(entity);
        List<PendingConnectionEntity> found = repository.findByTarget(targetId);
        assertFalse(found.isEmpty());
        assertEquals(targetId, found.get(0).getTargetId());
    }

    @Test
    void testFindBySenderAndTarget() {
        UUID senderId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();
        PendingConnectionEntity entity = createEntity(UUID.randomUUID(), senderId, targetId, Instant.now());
        repository.save(entity);
        Optional<PendingConnectionEntity> found = repository.findBySenderAndTarget(senderId, targetId);
        assertTrue(found.isPresent());
        assertEquals(senderId, found.get().getSenderId());
        assertEquals(targetId, found.get().getTargetId());
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        PendingConnectionEntity entity = createEntity(id, UUID.randomUUID(), UUID.randomUUID(), Instant.now());
        repository.save(entity);
        boolean deleted = repository.deleteById(id);
        assertTrue(deleted);
        assertTrue(repository.findById(id).isEmpty());
    }

    @Test
    void testDeleteOlderThan() {
        Instant now = Instant.now();
        PendingConnectionEntity entity1 = createEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), now.minusSeconds(3600));
        PendingConnectionEntity entity2 = createEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), now);
        repository.save(entity1);
        repository.save(entity2);
        boolean deleted = repository.deleteOlderThan(now.minusSeconds(1800));
        assertTrue(deleted);
        List<PendingConnectionEntity> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals(entity2.getId(), all.get(0).getId());
    }

    @Test
    void testDeleteAll() {
        PendingConnectionEntity entity = createEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now());
        repository.save(entity);
        repository.deleteAll();
        List<PendingConnectionEntity> all = repository.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void testFindAll() {
        PendingConnectionEntity entity1 = createEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now());
        PendingConnectionEntity entity2 = createEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now());
        repository.save(entity1);
        repository.save(entity2);
        List<PendingConnectionEntity> all = repository.findAll();
        assertEquals(2, all.size());
    }
}

