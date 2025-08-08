package tech.kood.match_me.user_management.internal.common.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class DomainObjectInputValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    public static final Validator instance = factory.getValidator();
}
