package tech.kood.match_me.matching.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.matching.model.ConnectionRequest;
import tech.kood.match_me.matching.repository.ConnectionRequestRepository;


@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    private final ConnectionRequestRepository connectionRequestRepository;

    public ConnectionController(ConnectionRequestRepository connectionRequestRepository) {
        this.connectionRequestRepository = connectionRequestRepository;
    }
    
    @PostMapping("/{requesterId}")
    public ResponseEntity<?> sendConnectionRequest(
        @PathVariable UUID requesterId,
        @RequestBody Map<String, UUID> body) {

        UUID requestedId = body.get("requested_id");
        if (requestedId == null) {
            return ResponseEntity.badRequest().body("Missing requested_id");
        }

        try {
            ConnectionRequest connectionRequest = new ConnectionRequest(requesterId, requestedId);
            connectionRequestRepository.save(connectionRequest);

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending connection request: " + e.getMessage());
        }
    }
    
}
