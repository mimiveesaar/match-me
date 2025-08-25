package tech.kood.match_me.common.exceptions;

import jakarta.validation.ConstraintViolation;
import org.springframework.modulith.NamedInterface;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A checked exception that reports constraint violations encountered during validation.
 * <p>
 * This exception is typically thrown when an object fails validation and the caller is required to handle
 * or propagate the error. It encapsulates a set of {@link jakarta.validation.ConstraintViolation} instances
 * that describe the specific validation failures.
 * <p>
 * Usage example:
 * <pre>
 *     Set<ConstraintViolation<?>> violations = validator.validate(someObject);
 *     if (!violations.isEmpty()) {
 *         throw new CheckedConstraintViolationException(violations);
 *     }
 * </pre>
 * <p>
 * This exception is checked, so it must be declared or handled by calling code.
 */
public class CheckedConstraintViolationException extends Exception {

    private final Set<ConstraintViolation<?>> constraintViolations;

    /**
     * Creates a constraint violation report.
     *
     * @param message error message
     * @param constraintViolations a {@code Set} of {@link ConstraintViolation}s or null
     */
    public CheckedConstraintViolationException(String message,
                                        Set<? extends ConstraintViolation<?>> constraintViolations) {
        super( message );

        if ( constraintViolations == null ) {
            this.constraintViolations = null;
        }
        else {
            this.constraintViolations = new HashSet<>( constraintViolations );
        }
    }

    /**
     * Creates a constraint violation report.
     *
     * @param constraintViolations a {@code Set} of {@link ConstraintViolation}s or null
     */
    public CheckedConstraintViolationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        this(
                constraintViolations != null ? toString( constraintViolations ) : null,
                constraintViolations
        );
    }

    /**
     * Returns the set of constraint violations reported during a validation.
     *
     * @return the {@code Set} of {@link ConstraintViolation}s or null
     */
    public Set<ConstraintViolation<?>> getConstraintViolations() {
        return constraintViolations;
    }

    private static String toString(Set<? extends ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map( cv -> cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage() )
                .collect( Collectors.joining( ", " ) );
    }
}
