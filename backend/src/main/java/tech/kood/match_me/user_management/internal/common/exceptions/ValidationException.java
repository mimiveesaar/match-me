package tech.kood.match_me.user_management.internal.common.exceptions;

import am.ik.yavi.core.ConstraintViolations;

public class ValidationException extends RuntimeException {
    private final ConstraintViolations violations;

    public ValidationException(ConstraintViolations violations) {
        super("Validation failed:\n" + violations.violations().stream()
                .map(v -> v.name() + ": " + v.message()).reduce("", (a, b) -> a + b + "\n"));
        this.violations = violations;
    }

    public ConstraintViolations getViolations() {
        return violations;
    }
}
