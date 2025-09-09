package tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.connections.common.api.ConnectionIdDTO;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionRequest;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionCommandHandler;
import tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.api.CreateRejectedConnectionResults;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnection;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionFactory;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnectionReason;
import tech.kood.match_me.connections.features.rejectedConnection.internal.mapper.RejectedConnectionMapper;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.RejectedConnectionRepository;
import tech.kood.match_me.connections.features.rejectedConnection.internal.persistance.rejectedConnectionEntity.RejectedConnectionEntity;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.domain.internal.userId.UserIdFactory;

import java.util.Optional;

@Component
@ApplicationLayer
public class CreateRejectedConnectionCommandHandlerImpl
        implements CreateRejectedConnectionCommandHandler {

    private final RejectedConnectionFactory rejectedConnectionFactory;
    private final RejectedConnectionRepository rejectedConnectionRepository;
    private final RejectedConnectionMapper rejectedConnectionMapper;
    private final UserIdFactory userIdFactory;
    private final Validator validator;

    public CreateRejectedConnectionCommandHandlerImpl(
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
    public CreateRejectedConnectionResults handle(CreateRejectedConnectionRequest request) {


        var validationErrors = validator.validate(request);
        if (!validationErrors.isEmpty()) {
            return new CreateRejectedConnectionResults.InvalidRequest(request.requestId(),
                    InvalidInputErrorDTO.fromValidation(validationErrors), request.tracingId());
        }

        try {
            // Check if a rejected connection already exists between these users
            var rejectedByUserId = userIdFactory.create(request.rejectedByUser().value());
            var rejectedUserId = userIdFactory.create(request.rejectedUser().value());

            Optional<RejectedConnectionEntity> existingRejection = rejectedConnectionRepository
                    .findBetweenUsers(rejectedByUserId.getValue(), rejectedUserId.getValue());

            if (existingRejection.isPresent()) {
                return new CreateRejectedConnectionResults.AlreadyExists(request.requestId(),
                        request.tracingId());
            }

            RejectedConnection rejectedConnection = rejectedConnectionFactory.createNew(
                    rejectedByUserId, rejectedUserId, RejectedConnectionReason
                            .valueOf(request.reason().name()));

            RejectedConnectionEntity entity = rejectedConnectionMapper.toEntity(rejectedConnection);
            rejectedConnectionRepository.save(entity);

            return new CreateRejectedConnectionResults.Success(request.requestId(),
                    new ConnectionIdDTO(
                            rejectedConnection.getId().getValue()),
                    request.tracingId());

        } catch (Exception e) {
            return new CreateRejectedConnectionResults.SystemError(request.requestId(),
                    "Failed to create rejected connection: " + e.getMessage(), request.tracingId());
        }
    }
}
