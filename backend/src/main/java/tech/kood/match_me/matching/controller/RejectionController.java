package tech.kood.match_me.matching.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.match_me.matching.model.UserRejection;
import tech.kood.match_me.matching.repository.UserRejectionRepository;

@RestController
@RequestMapping("/api/rejections")
public class RejectionController {

    private final UserRejectionRepository rejectionRepository;

    public RejectionController(UserRejectionRepository rejectionRepository) {
        this.rejectionRepository = rejectionRepository;
    }

    @PostMapping("/{rejecterId}")
    public ResponseEntity<?> rejectUser(
            @PathVariable UUID rejecterId,
            @RequestBody Map<String, UUID> body) {

        UUID rejectedId = body.get("rejected_id");
        if (rejectedId == null) {
            return ResponseEntity.badRequest().body("Missing rejected_id");
        }

        try {
            UserRejection rejection = new UserRejection(rejecterId, rejectedId);
            rejectionRepository.save(rejection);

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}