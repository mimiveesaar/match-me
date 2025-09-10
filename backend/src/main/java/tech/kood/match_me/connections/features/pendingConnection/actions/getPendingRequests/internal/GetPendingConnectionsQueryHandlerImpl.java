package tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.internal;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsQueryHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.getPendingRequests.api.GetPendingConnectionsResults;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;

import java.util.List;

@Service
public class GetPendingConnectionsQueryHandlerImpl implements GetPendingConnectionsQueryHandler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionMapper pendingConnectionMapper;

    public GetPendingConnectionsQueryHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionMapper pendingConnectionMapper) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionMapper = pendingConnectionMapper;
    }

    @Override
    public GetPendingConnectionsResults handle(GetPendingConnectionsRequest request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetPendingConnectionsResults.InvalidRequest(InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            // Get incoming requests
            var incomingRequests = pendingConnectionRepository.findByTarget(request.userId().value());
            List<PendingConnectionDTO> incomingRequestsMapped = mapEntityListToDTOList(incomingRequests);

            // Get outgoing requests
            var outgoingRequests = pendingConnectionRepository.findBySender(request.userId().value());
            List<PendingConnectionDTO> outgoingRequestsMapped = mapEntityListToDTOList(outgoingRequests);

            return new GetPendingConnectionsResults.Success(incomingRequestsMapped, outgoingRequestsMapped);
        } catch (Exception e) {
            return new GetPendingConnectionsResults.SystemError("An unexpected error occurred while processing the request.");
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
