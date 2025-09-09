package tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsQueryHandler;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsRequest;
import tech.kood.match_me.connections.features.acceptedConnection.actions.getConnections.api.GetAcceptedConnectionsResults;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;
import tech.kood.match_me.connections.features.acceptedConnection.internal.mapper.AcceptedConnectionMapper;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;

import java.util.ArrayList;
import java.util.List;

@Component
@ApplicationLayer
public class GetAcceptedConnectionsQueryHandlerImpl implements GetAcceptedConnectionsQueryHandler {

    private final AcceptedConnectionRepository repository;
    private final Validator validator;
    private final AcceptedConnectionMapper mapper;

    public GetAcceptedConnectionsQueryHandlerImpl(
            AcceptedConnectionRepository repository, Validator validator,
            AcceptedConnectionMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public GetAcceptedConnectionsResults handle(GetAcceptedConnectionsRequest request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetAcceptedConnectionsResults.InvalidRequest(request.requestId(),
                    InvalidInputErrorDTO.fromValidation(validationResults), request.tracingId());
        }

        try {
            List<AcceptedConnectionDTO> connections = new ArrayList<>();
            var entities = repository.findByUser(request.userId().value());
            for (var entity : entities) {
                connections.add(mapper.toDTO(entity));
            }
            return new GetAcceptedConnectionsResults.Success(request.requestId(), connections, request.tracingId());

        } catch (CheckedConstraintViolationException e) {
            return new GetAcceptedConnectionsResults.InvalidRequest(request.requestId(),
                    InvalidInputErrorDTO.fromException(e), request.tracingId());
        } catch (Exception e) {
            return new GetAcceptedConnectionsResults.SystemError(request.requestId(),
                    "An unexpected error occurred while processing the request.", request.tracingId());
        }
    }
}
