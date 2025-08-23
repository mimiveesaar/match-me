package tech.kood.match_me.connections.features.pendingConnection;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PendingConnectionRepositoryTests extends ConnectionsTestBase {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private PendingConnectionRepository repository;

    @Autowired
    private PendingConnectionEntityMother pendingConnectionEntityMother;

    @Test
    void testSaveAndFindById() {

        PendingConnectionEntity entity = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity);
        Optional<PendingConnectionEntity> found = repository.findById(entity.getId());
        assertTrue(found.isPresent());
        assertEquals(entity.getId(), found.get().getId());
    }

    @Test
    void testFindBySender() {

        PendingConnectionEntity entity = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity);
        List<PendingConnectionEntity> found = repository.findBySender(entity.getSenderId());
        assertFalse(found.isEmpty());
        assertEquals(entity.getSenderId(), found.getFirst().getSenderId());
    }

    @Test
    void testFindByTarget() {
        PendingConnectionEntity entity = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity);
        List<PendingConnectionEntity> found = repository.findByTarget(entity.getTargetId());
        assertFalse(found.isEmpty());
        assertEquals(entity.getTargetId(), found.getFirst().getTargetId());
    }

    @Test
    void testFindBetweenUsers() {
        PendingConnectionEntity entity = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity);
        Optional<PendingConnectionEntity> found = repository.findBetweenUsers(entity.getSenderId(), entity.getTargetId());
        assertTrue(found.isPresent());
        assertEquals(entity.getSenderId(), found.get().getSenderId());
        assertEquals(entity.getTargetId(), found.get().getTargetId());
    }

    @Test
    void testDeleteById() {
        PendingConnectionEntity entity = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity);
        boolean deleted = repository.deleteById(entity.getId());
        assertTrue(deleted);
        assertTrue(repository.findById(entity.getId()).isEmpty());
    }

    @Test
    void testDeleteOlderThan() {
        Instant now = Instant.now();
        PendingConnectionEntity entity1 = pendingConnectionEntityMother.createPendingConnectionEntity();
        entity1 = pendingConnectionEntityMother.withCreatedAt(entity1, now.minusSeconds(3600));
        PendingConnectionEntity entity2 = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity1);
        repository.save(entity2);
        boolean deleted = repository.deleteOlderThan(now.minusSeconds(1800));
        assertTrue(deleted);
        List<PendingConnectionEntity> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals(entity2.getId(), all.getFirst().getId());
    }

    @Test
    void testDeleteAll() {
        PendingConnectionEntity entity = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity);
        repository.deleteAll();
        List<PendingConnectionEntity> all = repository.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void testFindAll() {
        PendingConnectionEntity entity1 = pendingConnectionEntityMother.createPendingConnectionEntity();
        PendingConnectionEntity entity2 = pendingConnectionEntityMother.createPendingConnectionEntity();
        repository.save(entity1);
        repository.save(entity2);
        List<PendingConnectionEntity> all = repository.findAll();
        assertEquals(2, all.size());
    }
}

