package tech.kood.match_me.matching.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.kood.match_me.matching.service.UserRejectionRequestService;

@RestController
@RequestMapping("/api/rejections")
public class RejectionController {

    private final UserRejectionRequestService userRejectionRequestService;

    public RejectionController(UserRejectionRequestService userRejectionRequestService) {
        this.userRejectionRequestService = userRejectionRequestService;
    }

    @PostMapping("/{requesterId}")
    public ResponseEntity<?> sendRejectionRequest(
            @PathVariable UUID requesterId,
            @RequestBody Map<String, UUID> body) {

        UUID requestedId = body.get("requested_id"); // matches frontend key
        if (requestedId == null) {
            return ResponseEntity.badRequest().body("Missing requested_id");
        }

        try {
            userRejectionRequestService.sendRejectionRequest(requesterId, requestedId);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error sending rejection request: " + e.getMessage());
        }
    }
}
