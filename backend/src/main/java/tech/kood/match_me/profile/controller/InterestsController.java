package tech.kood.match_me.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.kood.match_me.profile.model.Interest;
import tech.kood.match_me.profile.repository.InterestRepository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000"})
public class InterestsController {
    
    @Autowired
    private InterestRepository interestRepository;
    
    @GetMapping("/interests")
    public ResponseEntity<List<Map<String, Object>>> getAllInterests() {
        List<Interest> interests = interestRepository.findAll();
        List<Map<String, Object>> response = interests.stream()
                .map(interest -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", interest.getId());
                    map.put("name", interest.getName());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}