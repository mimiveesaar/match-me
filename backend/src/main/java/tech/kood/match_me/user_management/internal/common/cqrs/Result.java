package tech.kood.match_me.user_management.internal.common.cqrs;

import java.io.Serializable;

/**
 * Marker interface for result objects in the user management module.
 * <p>
 * Implementations of this interface represent the outcome of operations in the user management
 * domain, such as commands or queries.
 * </p>
 * <p>
 * This interface extends {@link java.io.Serializable}, allowing result objects to be serialized for
 * transmission or persistence.
 * </p>
 */
public interface Result extends Serializable {
}
