package tech.kood.match_me.connections.features.rejectedConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.CreateRejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionFactory;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.mapper.RejectedConnectionMapper;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;

import java.util.Optional;

@Component
@ApplicationLayer
public class CreateRejectedConnectionHandlerImpl
        implements CreateRejectedConnection.Handler {

    private final RejectedConnectionFactory rejectedConnectionFactory;
    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final RejectedConnectionMapper rejectedConnectionMapper;
    private final UserIdFactory userIdFactory;
    private final Validator validator;

    public CreateRejectedConnectionHandlerImpl(
            RejectedConnectionFactory rejectedConnectionFactory,
            RejectedConnectionRepository rejectedConnectionRepository,
            RejectedConnectionMapper rejectedConnectionMapper, UserIdFactory userIdFactory, Validator validator) {
        this.rejectedConnectionFactory = rejectedConnectionFactory;
        this.rejectedConnectionRepository = rejectedConnectionRepository;
        this.rejectedConnectionMapper = rejectedConnectionMapper;
        this.userIdFactory = userIdFactory;
        this.validator = validator;
    }

    @Override
    @Transactional
    public CreateRejectedConnection.Result handle(CreateRejectedConnection.Request request) {


        var validationErrors = validator.validate(request);
        if (!validationErrors.isEmpty()) {
            return new CreateRejectedConnection.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationErrors));
        }

        try {
            // Check if a rejected connection already exists between these users
            var rejectedByUserId = userIdFactory.create(request.rejectedByUser().value());
            var rejectedUserId = userIdFactory.create(request.rejectedUser().value());

            Optional<RejectedConnectionEntity> existingRejection = rejectedConnectionRepository
                    .findBetweenUsers(rejectedByUserId.getValue(), rejectedUserId.getValue());

            if (existingRejection.isPresent()) {
                return new CreateRejectedConnection.Result.AlreadyExists();
            }

            RejectedConnection rejectedConnection = rejectedConnectionFactory.createNew(
                    rejectedByUserId, rejectedUserId, RejectedConnectionReason
                            .valueOf(request.reason().name()));

            RejectedConnectionEntity entity = rejectedConnectionMapper.toEntity(rejectedConnection);
            rejectedConnectionRepository.save(entity);

            return new CreateRejectedConnection.Result.Success(
                    new ConnectionIdDTO(
                            rejectedConnection.getId().getValue()));

        } catch (Exception e) {
            return new CreateRejectedConnection.Result.SystemError(
                    "Failed to create rejected connection: " + e.getMessage());
        }
    }
}
