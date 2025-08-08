package tech.kood.match_me.user_management.internal.common.cqrs;

/**
 * Generic interface for handling commands in the CQRS pattern within the user management module.
 * <p>
 * Implementations encapsulate the execution logic for a specific {@link Command} type and
 * synchronously return a {@link Result} representing the outcome of the operation.
 * </p>
 *
 * @param <C> The type of command this handler can process.
 * @param <R> The type of result produced by handling the command.
 */
@FunctionalInterface
public interface CommandHandler<C extends Command, R extends Result> {

    /**
     * Handles the given command and returns the operation result.
     *
     * @param command the command to handle
     * @return the result of handling the command
     */
    R handle(C command);
}
