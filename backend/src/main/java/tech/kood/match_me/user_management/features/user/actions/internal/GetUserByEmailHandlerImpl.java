package tech.kood.match_me.user_management.features.user.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.user.actions.GetUserByEmail;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;

@Service
@ApplicationLayer
public class GetUserByEmailHandlerImpl implements GetUserByEmail.Handler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserByEmailHandlerImpl.class);

    private final Validator validator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUserByEmailHandlerImpl(UserRepository userRepository, UserMapper userMapper, Validator validator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    @Transactional
    public GetUserByEmail.Result handle(GetUserByEmail.Request request) {

        var validationResults = validator.validate(request);

        // Handle input validation.
        if (!validationResults.isEmpty()) {
            return new GetUserByEmail.Result.InvalidRequest(InvalidInputErrorDTO.from(validationResults));
        }

        try {
            var userEntity = userRepository.findUserByEmail(request.email().toString());

            if (userEntity.isEmpty()) {
                return new GetUserByEmail.Result.UserNotFound(request.email());
            }

            var userDTO = userMapper.toDTO(userEntity.get());
            return new GetUserByEmail.Result.Success(userDTO);
        } catch (Exception e) {
            logger.error("Failed to retrieve userId by email: " + e.getMessage());

            return new GetUserByEmail.Result.SystemError(e.getMessage());
        }
    }
}