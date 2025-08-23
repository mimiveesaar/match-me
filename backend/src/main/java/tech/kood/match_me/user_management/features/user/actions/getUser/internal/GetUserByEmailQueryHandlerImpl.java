package tech.kood.match_me.user_management.features.user.actions.getUser.internal;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByEmailQueryHandler;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByEmailRequest;
import tech.kood.match_me.user_management.features.user.actions.getUser.api.GetUserByEmailResults;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;

@Service
@ApplicationLayer
public class GetUserByEmailQueryHandlerImpl implements GetUserByEmailQueryHandler {

     private static final Logger logger = LoggerFactory.getLogger(GetUserByEmailQueryHandlerImpl.class);

     private final Validator validator;
     private final UserRepository userRepository;
     private final UserMapper userMapper;

     public GetUserByEmailQueryHandlerImpl(UserRepository userRepository, UserMapper userMapper, Validator validator) {
          this.userRepository = userRepository;
          this.userMapper = userMapper;
          this.validator = validator;
     }

     @Transactional
     public GetUserByEmailResults handle(GetUserByEmailRequest request) {

         var validationResults = validator.validate(request);

         // Handle input validation.
         if (!validationResults.isEmpty()) {
             return new GetUserByEmailResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.from(validationResults), request.tracingId());
         }

         try {
             var userEntity = userRepository.findUserByEmail(request.email().toString());

             if (userEntity.isEmpty()) {
                 return new GetUserByEmailResults.UserNotFound(request.requestId(), request.email(), request.tracingId());
             }

             var userDTO = userMapper.toDTO(userEntity.get());
             return new GetUserByEmailResults.Success(request.requestId(), userDTO, request.tracingId());
         } catch (Exception e) {
             logger.error("Failed to retrieve userId by email: " + e.getMessage());

             return new GetUserByEmailResults.SystemError(request.requestId(), e.getMessage(), request.tracingId());
         }
     }
}
