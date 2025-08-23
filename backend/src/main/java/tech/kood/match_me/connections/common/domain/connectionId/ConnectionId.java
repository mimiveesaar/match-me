package tech.kood.match_me.connections.common.domain.connectionId;


import jakarta.validation.constraints.NotNull;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

@DomainLayer
public class ConnectionId implements Identifier {

    @NotNull
    private final UUID value;

    public ConnectionId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ConnectionId))
            return false;
        ConnectionId that = (ConnectionId) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}