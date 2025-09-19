package tech.kood.match_me.connections.features.rejectedConnection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RejectedConnectionRepositoryTests extends ConnectionsTestBase {
    @Autowired
    private RejectedConnectionRepository repository;

    @Autowired
    private RejectedConnectionEntityMother rejectedConnectionEntityMother;

    @Test
    void testSaveAndFindById() {
        RejectedConnectionEntity entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);
        Optional<RejectedConnectionEntity> found = repository.findById(entity.getId());
        assertTrue(found.isPresent());
        assertEquals(entity.getId(), found.get().getId());
    }

    @Test
    void testFindByRejectedByUser() {
        RejectedConnectionEntity entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);
        List<RejectedConnectionEntity> found = repository.findByRejectedByUser(entity.getRejectedByUserId());
        assertFalse(found.isEmpty());
        assertEquals(entity.getRejectedByUserId(), found.getFirst().getRejectedByUserId());
    }

    @Test
    void testFindByRejectedUser() {
        RejectedConnectionEntity entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);
        List<RejectedConnectionEntity> found = repository.findByRejectedUser(entity.getRejectedUserId());
        assertFalse(found.isEmpty());
        assertEquals(entity.getRejectedUserId(), found.getFirst().getRejectedUserId());
    }

    @Test
    void testFindBetweenUsers() {
        RejectedConnectionEntity entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);
        Optional<RejectedConnectionEntity> found = repository.findBetweenUsers(entity.getRejectedByUserId(), entity.getRejectedUserId());
        assertTrue(found.isPresent());
        assertEquals(entity.getRejectedByUserId(), found.get().getRejectedByUserId());
        assertEquals(entity.getRejectedUserId(), found.get().getRejectedUserId());
    }

    @Test
    void testFindBetweenUsersReversed() {
        RejectedConnectionEntity entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);
        // Test the bidirectional nature of findBetweenUsers
        Optional<RejectedConnectionEntity> found = repository.findBetweenUsers(entity.getRejectedUserId(), entity.getRejectedByUserId());
        assertTrue(found.isPresent());
        assertEquals(entity.getRejectedByUserId(), found.get().getRejectedByUserId());
        assertEquals(entity.getRejectedUserId(), found.get().getRejectedUserId());
    }

    @Test
    void testDeleteById() {
        RejectedConnectionEntity entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);
        boolean deleted = repository.deleteById(entity.getId());
        assertTrue(deleted);
        assertTrue(repository.findById(entity.getId()).isEmpty());
    }

    @Test
    void testDeleteByIdNonExistent() {
        UUID nonExistentId = UUID.randomUUID();
        boolean deleted = repository.deleteById(nonExistentId);
        assertFalse(deleted);
    }

    @Test
    void testDeleteAll() {
        RejectedConnectionEntity entity = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity);
        repository.deleteAll();
        List<RejectedConnectionEntity> all = repository.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void testFindAll() {
        RejectedConnectionEntity entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        RejectedConnectionEntity entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        repository.save(entity1);
        repository.save(entity2);
        List<RejectedConnectionEntity> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testSaveWithDifferentReasons() {
        RejectedConnectionEntity entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity1 = rejectedConnectionEntityMother.withReason(entity1, RejectedConnectionReason.CONNECTION_DECLINED);

        RejectedConnectionEntity entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        entity2 = rejectedConnectionEntityMother.withReason(entity2, RejectedConnectionReason.CONNECTION_REMOVED);

        repository.save(entity1);
        repository.save(entity2);

        Optional<RejectedConnectionEntity> found1 = repository.findById(entity1.getId());
        Optional<RejectedConnectionEntity> found2 = repository.findById(entity2.getId());

        assertTrue(found1.isPresent());
        assertTrue(found2.isPresent());
        assertEquals(RejectedConnectionReason.CONNECTION_DECLINED, found1.get().getReason());
        assertEquals(RejectedConnectionReason.CONNECTION_REMOVED, found2.get().getReason());
    }

    @Test
    void testFindByIdNonExistent() {
        UUID nonExistentId = UUID.randomUUID();
        Optional<RejectedConnectionEntity> found = repository.findById(nonExistentId);
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindByRejectedByUserNoResults() {
        UUID nonExistentUserId = UUID.randomUUID();
        List<RejectedConnectionEntity> found = repository.findByRejectedByUser(nonExistentUserId);
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindByRejectedUserNoResults() {
        UUID nonExistentUserId = UUID.randomUUID();
        List<RejectedConnectionEntity> found = repository.findByRejectedUser(nonExistentUserId);
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindBetweenUsersNoResults() {
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();
        Optional<RejectedConnectionEntity> found = repository.findBetweenUsers(user1, user2);
        assertTrue(found.isEmpty());
    }

    @Test
    void testMultipleRejectionsByUser() {
        UUID rejectedByUserId = UUID.randomUUID();
        RejectedConnectionEntity entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        RejectedConnectionEntity entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();

        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, rejectedByUserId, UUID.randomUUID());
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, rejectedByUserId, UUID.randomUUID());

        repository.save(entity1);
        repository.save(entity2);

        List<RejectedConnectionEntity> found = repository.findByRejectedByUser(rejectedByUserId);
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(e -> e.getRejectedByUserId().equals(rejectedByUserId)));
    }

    @Test
    void testMultipleRejectionsOfUser() {
        UUID rejectedUserId = UUID.randomUUID();
        RejectedConnectionEntity entity1 = rejectedConnectionEntityMother.createRejectedConnectionEntity();
        RejectedConnectionEntity entity2 = rejectedConnectionEntityMother.createRejectedConnectionEntity();

        entity1 = rejectedConnectionEntityMother.withSpecificIds(entity1, UUID.randomUUID(), rejectedUserId);
        entity2 = rejectedConnectionEntityMother.withSpecificIds(entity2, UUID.randomUUID(), rejectedUserId);

        repository.save(entity1);
        repository.save(entity2);

        List<RejectedConnectionEntity> found = repository.findByRejectedUser(rejectedUserId);
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(e -> e.getRejectedUserId().equals(rejectedUserId)));
    }
}
