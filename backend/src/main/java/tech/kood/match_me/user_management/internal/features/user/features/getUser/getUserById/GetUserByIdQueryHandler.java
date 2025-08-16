package tech.kood.match_me.user_management.internal.features.user.features.getUser.getUserById;

import jakarta.transaction.Transactional;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.internal.features.user.mapper.UserMapper;
import tech.kood.match_me.user_management.internal.features.user.persistance.UserRepository;

@Service
@ApplicationLayer
public class GetUserByIdQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserByIdQueryHandler.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUserByIdQueryHandler(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public GetUserByIdResults handle(GetUserByIdRequest request) {

        try {
            var userEntity = userRepository.findUserById(request.userId().getValue());

            if (userEntity.isEmpty()) {
                var result = new GetUserByIdResults.UserNotFound(request.requestId(), request.userId(), request.tracingId());
                return result;
            }

            var user = userMapper.toUser(userEntity.get());
            var result = new GetUserByIdResults.Success(request.requestId(), user, request.tracingId());
            return result;
        } catch (Exception e) {
            logger.error("Failed to retrieve user by ID: " + e.getMessage());
            var result = new GetUserByIdResults.SystemError(request.requestId(), e.getMessage(), request.tracingId());
            return result;
        }
    }
}
