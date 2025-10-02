package tech.kood.match_me.user_management.features.user.actions.internal;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.user.actions.GetUserById;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;

@Service
@ApplicationLayer
public class GetUserByIdHandlerImpl implements GetUserById.Handler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserByIdHandlerImpl.class);

    private final Validator validator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUserByIdHandlerImpl(Validator validator, UserRepository userRepository, UserMapper userMapper) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public GetUserById.Result handle(GetUserById.Request request) {

        var validationResults = validator.validate(request);

        // Handle input validation.
        if (!validationResults.isEmpty()) {
            return new GetUserById.Result.InvalidRequest(InvalidInputErrorDTO.from(validationResults));
        }

        try {
            var userEntity = userRepository.findUserById(request.userId().value());

            if (userEntity.isEmpty()) {
                return new GetUserById.Result.UserNotFound(request.userId());
            }

            var userDTO = userMapper.toDTO(userEntity.get());
            return new GetUserById.Result.Success(userDTO);
        } catch (Exception e) {
            logger.error("Failed to retrieve userId by ID: " + e.getMessage());
            return new GetUserById.Result.SystemError(e.getMessage());
        }
    }
}