package tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.acceptedConnectionEntity;

import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@InfrastructureLayer
@Component
public class AcceptedConnectionEntityFactory {

    public AcceptedConnectionEntity create(UUID id, UUID acceptedByUserId, UUID acceptedUserId,
            Instant createdAt) {
        return new AcceptedConnectionEntity(id, acceptedByUserId, acceptedUserId, createdAt);
    }
}
