package tech.kood.match_me.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.kood.match_me.profile.repository.BodyformRepository;
import tech.kood.match_me.profile.repository.HomeplanetRepository;
import tech.kood.match_me.profile.repository.LookingForRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3002", "http://localhost:3000"})
public class ReferenceDataController {
    
    @Autowired
    private BodyformRepository bodyformRepository;
    
    @Autowired
    private HomeplanetRepository homeplanetRepository;
    
    @Autowired
    private LookingForRepository lookingForRepository;
    
    @GetMapping("/bodyforms")
    public ResponseEntity<List<Map<String, Object>>> getAllBodyforms() {
        return ResponseEntity.ok(
            bodyformRepository.findAll().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", item.getId());
                    map.put("name", item.getName());
                    return map;
                })
                .collect(Collectors.toList())
        );
    }
    
    @GetMapping("/homeplanets")
    public ResponseEntity<List<Map<String, Object>>> getAllHomeplanets() {
        return ResponseEntity.ok(
            homeplanetRepository.findAll().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", item.getId());
                    map.put("name", item.getName());
                    return map;
                })
                .collect(Collectors.toList())
        );
    }
    
    @GetMapping("/looking-for")
    public ResponseEntity<List<Map<String, Object>>> getAllLookingFor() {
        return ResponseEntity.ok(
            lookingForRepository.findAll().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", item.getId());
                    map.put("name", item.getName());
                    return map;
                })
                .collect(Collectors.toList())
        );
    }
}