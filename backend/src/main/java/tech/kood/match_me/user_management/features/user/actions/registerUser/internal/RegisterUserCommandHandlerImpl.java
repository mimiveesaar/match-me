package tech.kood.match_me.user_management.features.user.actions.registerUser.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserCommandHandler;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserRequest;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.RegisterUserResults;
import tech.kood.match_me.user_management.features.user.actions.registerUser.api.UserRegisteredEvent;
import tech.kood.match_me.user_management.features.user.domain.internal.user.UserFactory;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;

@Service
@ApplicationLayer
public final class RegisterUserCommandHandlerImpl implements RegisterUserCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserCommandHandlerImpl.class);
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final ApplicationEventPublisher events;
    private final UserMapper userMapper;
    private final Validator validator;

    public RegisterUserCommandHandlerImpl(UserRepository userRepository, UserFactory userFactory, ApplicationEventPublisher events,
                                          UserMapper userMapper, Validator validator) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.events = events;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    @Override
    public RegisterUserResults handle(RegisterUserRequest request) {

        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            return new RegisterUserResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.from(validationResults), request.tracingId());
        }

        try {
            // Check for email uniqueness.
            if (userRepository.emailExists(request.email().toString())) {
                return new RegisterUserResults.EmailExists(
                        request.requestId(),
                        request.email(),
                        request.tracingId());
            }

            var user = userFactory.newUser(request.email().value(), request.password().value());
            var userEntity = userMapper.toEntity(user);

            // Save the userId entity to the database.
            userRepository.saveUser(userEntity);

            var result = new RegisterUserResults.Success(
                    request.requestId(),
                    UserIdDTO.of(user.getId().toString()),
                    request.tracingId()
            );

            events.publishEvent(new UserRegisteredEvent(result.userId()));
            return result;

        } catch (Exception e) {
            return new RegisterUserResults.SystemError(request.requestId(), e.getMessage(), request.tracingId());
        }
    }
}
