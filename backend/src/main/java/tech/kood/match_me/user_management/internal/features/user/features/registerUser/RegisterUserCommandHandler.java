package tech.kood.match_me.user_management.internal.features.user.features.registerUser;


import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.internal.features.user.domain.model.UserFactory;
import tech.kood.match_me.user_management.internal.features.user.mapper.UserMapper;
import tech.kood.match_me.user_management.internal.features.user.persistance.UserRepository;

@Service
@ApplicationLayer
public final class RegisterUserCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserCommandHandler.class);
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final ApplicationEventPublisher events;
    private final UserMapper userMapper;
    private final Validator validator;

    public RegisterUserCommandHandler(UserRepository userRepository, UserFactory userFactory, ApplicationEventPublisher events,
                                      UserMapper userMapper, Validator validator) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.events = events;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    public RegisterUserResults handle(RegisterUserRequest request) throws CheckedConstraintViolationException {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResults);
        }

        try {
            // Check for email uniqueness.
            if (userRepository.emailExists(request.email().toString())) {
                var result = new RegisterUserResults.EmailExists(request.email(),
                        request.requestId(), request.tracingId());
                events.publishEvent(new RegisterUserEvent(request, result));
                return result;
            }

            var user = userFactory.newUser(request.email(), request.password());
            var userEntity = userMapper.toEntity(user);

            // Save the user entity to the database.
            userRepository.saveUser(userEntity);

            var result = new RegisterUserResults.Success(
                    user.getId(),
                    request.requestId(),
                    request.tracingId()
            );
            events.publishEvent(new RegisterUserEvent(request, result));
            return result;

        } catch (Exception e) {
            var result = new RegisterUserResults.SystemError(e.getMessage(), request.requestId(), request.tracingId());
            events.publishEvent(new RegisterUserEvent(request, result));
            return result;
        }
    }
}
