package tech.kood.match_me.user_management.internal.features.user.features.getUser.getUserByEmail;

import jakarta.transaction.Transactional;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.kood.match_me.user_management.internal.features.user.mapper.UserMapper;
import tech.kood.match_me.user_management.internal.features.user.persistance.UserRepository;

@Service
@ApplicationLayer
public class GetUserByEmailQueryHandler {

     private static final Logger logger = LoggerFactory.getLogger(GetUserByEmailQueryHandler.class);

     private final UserRepository userRepository;
     private final UserMapper userMapper;

     public GetUserByEmailQueryHandler(UserRepository userRepository, UserMapper userMapper) {
          this.userRepository = userRepository;
          this.userMapper = userMapper;
     }

     @Transactional
     public GetUserByEmailResults handle(GetUserByEmailRequest request) {

         try {
             var userEntity = userRepository.findUserByEmail(request.email().toString());

             if (userEntity.isEmpty()) {
                 var result = new GetUserByEmailResults.UserNotFound(request.requestId(), request.email(), request.tracingId());
                 return result;
             }

             var user = userMapper.toUser(userEntity.get());
             var result = new GetUserByEmailResults.Success(request.requestId(), user, request.tracingId());

             return result;
         } catch (Exception e) {
             logger.error("Failed to retrieve user by email: " + e.getMessage());

             var result = new GetUserByEmailResults.SystemError(request.requestId(), e.getMessage(), request.tracingId());
             return result;
         }
     }
}
