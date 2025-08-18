package tech.kood.match_me.user_management.features.user.features.registerUser.api;

import org.jmolecules.architecture.layered.ApplicationLayer;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;

@ApplicationLayer
public interface RegisterUserCommandHandler {
    RegisterUserResults handle(RegisterUserRequest request) throws CheckedConstraintViolationException;
}
