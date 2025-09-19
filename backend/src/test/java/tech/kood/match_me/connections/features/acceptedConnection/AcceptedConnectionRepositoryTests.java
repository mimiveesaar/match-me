package tech.kood.match_me.connections.features.acceptedConnection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.ConnectionsTestBase;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity.AcceptedConnectionEntity;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AcceptedConnectionRepositoryTests extends ConnectionsTestBase {

    @Autowired
    private AcceptedConnectionRepository repository;

    @Autowired
    private AcceptedConnectionEntityMother acceptedConnectionEntityMother;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }


    @Test
    void testSaveAndFindById() {
        AcceptedConnectionEntity entity = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity);

        var foundEntity = repository.findById(entity.getId());

        assertTrue(foundEntity.isPresent());
        assertEquals(entity.getId(), foundEntity.get().getId());
        assertEquals(entity.getAcceptedByUserId(), foundEntity.get().getAcceptedByUserId());
        assertEquals(entity.getAcceptedUserId(), foundEntity.get().getAcceptedUserId());
        assertEquals(entity.getCreatedAt().truncatedTo(ChronoUnit.SECONDS),
                     foundEntity.get().getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void testFindByAcceptedByUser() {
        AcceptedConnectionEntity entity = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity);

        var foundEntities = repository.findByAcceptedByUser(entity.getAcceptedByUserId());

        assertFalse(foundEntities.isEmpty());
        assertEquals(1, foundEntities.size());
        assertEquals(entity.getId(), foundEntities.getFirst().getId());
    }

    @Test
    void testFindByAcceptedUser() {
        AcceptedConnectionEntity entity = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity);

        var foundEntities = repository.findByAcceptedUser(entity.getAcceptedUserId());

        assertFalse(foundEntities.isEmpty());
        assertEquals(1, foundEntities.size());
        assertEquals(entity.getId(), foundEntities.getFirst().getId());
    }

    @Test
    void findByUser() {
        AcceptedConnectionEntity entity1 = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity1);

        var foundEntities = repository.findByUser(entity1.getAcceptedByUserId());
        assertFalse(foundEntities.isEmpty());
        assertEquals(1, foundEntities.size());
        assertEquals(entity1.getId(), foundEntities.getFirst().getId());
    }

    @Test
    void testFindBetweenUsers() {
        AcceptedConnectionEntity entity = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity);

        var foundEntity = repository.findBetweenUsers(entity.getAcceptedByUserId(), entity.getAcceptedUserId());

        assertTrue(foundEntity.isPresent());
        assertEquals(entity.getId(), foundEntity.get().getId());
        
        // Test reverse order of users
        var reversedFoundEntity = repository.findBetweenUsers(entity.getAcceptedUserId(), entity.getAcceptedByUserId());
        assertTrue(reversedFoundEntity.isPresent());
        assertEquals(entity.getId(), reversedFoundEntity.get().getId());
    }

    @Test
    void testDeleteById() {
        AcceptedConnectionEntity entity = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        repository.save(entity);

        boolean deleted = repository.deleteById(entity.getId());

        assertTrue(deleted);
        assertTrue(repository.findById(entity.getId()).isEmpty());
    }

    @Test
    void testDeleteById_NotFound() {
        boolean deleted = repository.deleteById(UUID.randomUUID());

        assertFalse(deleted);
    }

    @Test
    void testFindAll() {
        AcceptedConnectionEntity entity1 = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        AcceptedConnectionEntity entity2 = acceptedConnectionEntityMother.createAcceptedConnectionEntity();
        
        repository.save(entity1);
        repository.save(entity2);

        var allEntities = repository.findAll();

        assertEquals(2, allEntities.size());
        assertTrue(allEntities.stream().anyMatch(e -> e.getId().equals(entity1.getId())));
        assertTrue(allEntities.stream().anyMatch(e -> e.getId().equals(entity2.getId())));
    }
}
