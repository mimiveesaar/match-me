package tech.kood.match_me.user_management.internal.common.cqrs;

import java.io.Serializable;

/**
 * Marker interface for query objects in the user management module.
 * <p>
 * Implementations of this interface represent read-only operations that retrieve data without
 * modifying application state.
 * </p>
 * <p>
 * This interface extends {@link java.io.Serializable}, allowing query objects to be serialized for
 * transmission or persistence.
 * </p>
 */
public interface Query extends Serializable {
}
