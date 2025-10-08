package tech.kood.match_me.user_management.features.user.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.api.UserIdDTO;
import tech.kood.match_me.user_management.features.user.actions.RegisterUser;
import tech.kood.match_me.user_management.features.user.domain.internal.user.UserFactory;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;

@Service
@ApplicationLayer
public class RegisterUserHandlerImpl implements RegisterUser.Handler {

    private static final Logger logger = LoggerFactory.getLogger(RegisterUserHandlerImpl.class);
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final ApplicationEventPublisher events;
    private final UserMapper userMapper;
    private final Validator validator;

    public RegisterUserHandlerImpl(UserRepository userRepository, UserFactory userFactory, ApplicationEventPublisher events,
                                   UserMapper userMapper, Validator validator) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.events = events;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    @Override
    @Transactional
    public RegisterUser.Result handle(RegisterUser.Request request) {
        logger.info("Handling user registration for email: {}", request.email());
        var validationResults = validator.validate(request);
        if (!validationResults.isEmpty()) {
            logger.warn("Validation failed for registration request: {}", validationResults);
            return new RegisterUser.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {

            // Check for email uniqueness.
            if (userRepository.emailExists(request.email().toString())) {
                logger.info("Registration failed: email already exists: {}", request.email());
                return new RegisterUser.Result.EmailExists(request.email());
            }

            var user = userFactory.newUser(request.email().value(), request.password().value());
            var userEntity = userMapper.toEntity(user);

            // Save the userId entity to the database.
            userRepository.saveUser(userEntity);
            logger.info("User saved successfully: {}", user.getId());

            var result = new  RegisterUser.Result.Success(UserIdDTO.of(user.getId().toString()));

            events.publishEvent(new RegisterUser.UserRegistered(result.userId()));
            logger.info("Published UserRegisteredEvent for userId: {}", result.userId());
            return result;

        } catch (Exception e) {
            logger.error("System error during user registration: {}", e.getMessage(), e);
            return new RegisterUser.Result.SystemError(e.getMessage());
        }
    }
}