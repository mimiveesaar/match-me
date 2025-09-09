package tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.internal;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionQueryHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionsRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.getIncomingRequests.api.GetIncomingConnectionsResults;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;

import java.util.ArrayList;

@Service
public class GetIncomingConnectionQueryHandlerImpl implements GetIncomingConnectionQueryHandler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionMapper pendingConnectionMapper;

    public GetIncomingConnectionQueryHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionMapper pendingConnectionMapper) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionMapper = pendingConnectionMapper;
    }

    @Override
    public GetIncomingConnectionsResults handle(GetIncomingConnectionsRequest request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetIncomingConnectionsResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.fromValidation(validationResults), request.tracingId());
        }

        try {
            var incomingRequests = pendingConnectionRepository.findByTarget(request.userId().value());
            var incomingRequestsMapped = new ArrayList<PendingConnectionDTO>();
            for (PendingConnectionEntity incomingRequest : incomingRequests) {
                PendingConnectionDTO dto = pendingConnectionMapper.toDTO(incomingRequest);
                incomingRequestsMapped.add(dto);
            }

            return new GetIncomingConnectionsResults.Success(request.requestId(), incomingRequestsMapped , request.tracingId());
        } catch (Exception e) {
            return new GetIncomingConnectionsResults.SystemError(request.requestId(), "An unexpected error occurred while processing the request.", request.tracingId());
        }
    }
}