package tech.kood.match_me.connections.features.pendingConnection.actions.internal;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.pendingConnection.actions.GetPendingConnections;
import tech.kood.match_me.connections.features.pendingConnection.domain.PendingConnectionDTO;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;

import java.util.List;

@Service
public class GetPendingConnectionsQueryHandlerImpl implements GetPendingConnections.Handler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionMapper pendingConnectionMapper;

    public GetPendingConnectionsQueryHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionMapper pendingConnectionMapper) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionMapper = pendingConnectionMapper;
    }

    @Override
    public GetPendingConnections.Result handle(GetPendingConnections.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetPendingConnections.Result.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            // Get incoming requests
            var incomingRequests = pendingConnectionRepository.findByTarget(request.userId().value());
            List<PendingConnectionDTO> incomingRequestsMapped = mapEntityListToDTOList(incomingRequests);

            // Get outgoing requests
            var outgoingRequests = pendingConnectionRepository.findBySender(request.userId().value());
            List<PendingConnectionDTO> outgoingRequestsMapped = mapEntityListToDTOList(outgoingRequests);

            return new GetPendingConnections.Result.Success(incomingRequestsMapped, outgoingRequestsMapped);
        } catch (Exception e) {
            return new GetPendingConnections.Result.SystemError("An unexpected error occurred while processing the request.");
        }
    }
    
    private List<PendingConnectionDTO> mapEntityListToDTOList(List<PendingConnectionEntity> entities) {
        return entities.stream()
                .map(entity -> {
                    try {
                        return pendingConnectionMapper.toDTO(entity);
                    } catch (CheckedConstraintViolationException e) {
                        throw new RuntimeException("Failed to map pending connection entity to DTO", e);
                    }
                })
                .toList();
    }
}
