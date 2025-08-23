package tech.kood.match_me.user_management.features.user.actions.getUser.internal;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdQueryHandler;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdRequest;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByIdResults;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;

@Service
@ApplicationLayer
public class GetUserByIdQueryHandlerImpl implements GetUserByIdQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserByIdQueryHandlerImpl.class);

    private final Validator validator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUserByIdQueryHandlerImpl(Validator validator, UserRepository userRepository, UserMapper userMapper) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public GetUserByIdResults handle(GetUserByIdRequest request) {

        var validationResults = validator.validate(request);

        // Handle input validation.
        if (!validationResults.isEmpty()) {
            return new GetUserByIdResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.from(validationResults), request.tracingId());
        }

        try {
            var userEntity = userRepository.findUserById(request.userId().value());

            if (userEntity.isEmpty()) {
                return new GetUserByIdResults.UserNotFound(request.requestId(), request.userId(), request.tracingId());
            }

            var userDTO = userMapper.toDTO(userEntity.get());
            return new GetUserByIdResults.Success(request.requestId(), userDTO, request.tracingId());
        } catch (Exception e) {
            logger.error("Failed to retrieve userId by ID: " + e.getMessage());
            return new GetUserByIdResults.SystemError(request.requestId(), e.getMessage(), request.tracingId());
        }
    }
}