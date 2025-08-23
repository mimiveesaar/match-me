package tech.kood.match_me.connections.features.rejectedConnection.domain.internal;

import org.jmolecules.architecture.layered.DomainLayer;

@DomainLayer
public enum RejectedConnectionReason {
    CONNECTION_DECLINED,
    CONNECTION_REMOVED
}
