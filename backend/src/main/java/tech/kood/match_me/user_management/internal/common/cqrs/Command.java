package tech.kood.match_me.user_management.internal.common.cqrs;

import java.io.Serializable;

/**
 * Marker interface for command objects in the user management module.
 * <p>
 * Implementations of this interface represent actions or operations that modify application state.
 * </p>
 * <p>
 * This interface extends {@link java.io.Serializable}, allowing command objects to be serialized
 * for transmission or persistence.
 * </p>
 */
public interface Command extends Serializable {
}
