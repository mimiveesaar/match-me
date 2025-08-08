package tech.kood.match_me.user_management.internal.common.cqrs;

/**
 * Generic interface for handling queries in the CQRS pattern within the user management module.
 * <p>
 * Implementations encapsulate the execution logic for a specific {@link Query} type and
 * synchronously return a {@link Result} representing the outcome of the operation.
 * </p>
 *
 * @param <Q> The type of query this handler can process.
 * @param <R> The type of result produced by handling the query.
 */
@FunctionalInterface
public interface QueryHandler<Q extends Query, R extends Result> {

    /**
     * Handles the given query and returns the operation result.
     *
     * @param query the query to handle
     * @return the result of handling the query
     */
    R handle(Q query);
}
