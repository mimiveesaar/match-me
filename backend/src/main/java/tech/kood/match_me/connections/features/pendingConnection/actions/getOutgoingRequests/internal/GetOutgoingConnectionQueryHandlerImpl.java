package tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.internal;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api.GetOutgoingConnectionQueryHandler;
import tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api.GetOutgoingConnectionsRequest;
import tech.kood.match_me.connections.features.pendingConnection.actions.getOutgoingRequests.api.GetOutgoingConnectionsResults;
import tech.kood.match_me.connections.features.pendingConnection.domain.api.PendingConnectionDTO;
import tech.kood.match_me.connections.features.pendingConnection.internal.mapper.PendingConnectionMapper;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.PendingConnectionRepository;
import tech.kood.match_me.connections.features.pendingConnection.internal.persistance.pendingConnectionEntity.PendingConnectionEntity;

import java.util.ArrayList;

@Service
public class GetOutgoingConnectionQueryHandlerImpl implements GetOutgoingConnectionQueryHandler {

    private final Validator validator;
    private final PendingConnectionRepository pendingConnectionRepository;
    private final PendingConnectionMapper pendingConnectionMapper;

    public GetOutgoingConnectionQueryHandlerImpl(Validator validator, PendingConnectionRepository pendingConnectionRepository, PendingConnectionMapper pendingConnectionMapper) {
        this.validator = validator;
        this.pendingConnectionRepository = pendingConnectionRepository;
        this.pendingConnectionMapper = pendingConnectionMapper;
    }

    @Override
    public GetOutgoingConnectionsResults handle(GetOutgoingConnectionsRequest request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetOutgoingConnectionsResults.InvalidRequest(request.requestId(), InvalidInputErrorDTO.fromValidation(validationResults), request.tracingId());
        }

        try {
            var outgoingRequests = pendingConnectionRepository.findBySender(request.userId().value());
            var outgoingRequestsMapped = new ArrayList<PendingConnectionDTO>();
            for (PendingConnectionEntity outgoingRequest : outgoingRequests) {
                PendingConnectionDTO dto = pendingConnectionMapper.toDTO(outgoingRequest);
                outgoingRequestsMapped.add(dto);
            }

            return new GetOutgoingConnectionsResults.Success(request.requestId(), outgoingRequestsMapped, request.tracingId());
        } catch (Exception e) {
            return new GetOutgoingConnectionsResults.SystemError(request.requestId(), "An unexpected error occurred while processing the request.", request.tracingId());
        }
    }
}
