package tech.kood.match_me.connections.features.acceptedConnection.actions.internal;

import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.kood.match_me.common.api.InvalidInputErrorDTO;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.connections.features.acceptedConnection.actions.GetAcceptedConnections;
import tech.kood.match_me.connections.features.acceptedConnection.domain.api.AcceptedConnectionDTO;
import tech.kood.match_me.connections.features.acceptedConnection.internal.mapper.AcceptedConnectionMapper;
import tech.kood.match_me.connections.features.acceptedConnection.internal.persistance.AcceptedConnectionRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationLayer
@Service
public class GetAcceptedConnectionsQueryHandlerImpl implements GetAcceptedConnections.Handler {

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
    public GetAcceptedConnections.Result handle(GetAcceptedConnections.Request request) {
        var validationResults = validator.validate(request);

        if (!validationResults.isEmpty()) {
            return new GetAcceptedConnections.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromValidation(validationResults));
        }

        try {
            List<AcceptedConnectionDTO> connections = new ArrayList<>();
            var entities = repository.findByUser(request.userId().value());
            for (var entity : entities) {
                connections.add(mapper.toDTO(entity));
            }
            return new GetAcceptedConnections.Result.Success(connections);

        } catch (CheckedConstraintViolationException e) {
            return new GetAcceptedConnections.Result.InvalidRequest(
                    InvalidInputErrorDTO.fromException(e));
        } catch (Exception e) {
            return new GetAcceptedConnections.Result.SystemError(
                    "An unexpected error occurred while processing the request.");
        }
    }
}
