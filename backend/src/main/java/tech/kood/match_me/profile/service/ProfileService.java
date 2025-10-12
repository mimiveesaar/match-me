package tech.kood.match_me.profile.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.events.ProfileChangedDTOEvent;
import tech.kood.match_me.profile.model.Interest;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.repository.*;

@Service
public class ProfileService {

    private final ProfileRepository profileRepo;
    private final HomeplanetRepository homeplanetRepo;
    private final BodyformRepository bodyformRepo;
    private final LookingForRepository lookingForRepo;
    private final InterestRepository interestRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final FileStorageService fileStorageService;

    public ProfileService(
            ProfileRepository profileRepo,
            HomeplanetRepository homeplanetRepo,
            BodyformRepository bodyformRepo,
            LookingForRepository lookingForRepo,
            InterestRepository interestRepo,
            ApplicationEventPublisher eventPublisher,
            FileStorageService fileStorageService) {
        this.profileRepo = profileRepo;
        this.homeplanetRepo = homeplanetRepo;
        this.bodyformRepo = bodyformRepo;
        this.lookingForRepo = lookingForRepo;
        this.interestRepo = interestRepo;
        this.eventPublisher = eventPublisher;
        this.fileStorageService = fileStorageService;
    }

    // -------------------- PROFILE CRUD --------------------

    @Transactional
    public ProfileViewDTO saveOrUpdateProfile(ProfileDTO dto) {
        System.out.println("=== saveOrUpdateProfile called ===");
        Profile profile = getMyProfile();

        updateProfileFromDTO(profile, dto);

        Profile saved = profileRepo.saveAndFlush(profile);
        Profile reloaded = profileRepo.findByIdWithRelations(saved.getId());
        ProfileViewDTO result = toViewDTO(reloaded);
        
        eventPublisher.publishEvent(new ProfileChangedDTOEvent(this, result));
        return result;
    }

    private void updateProfileFromDTO(Profile profile, ProfileDTO dto) {
        if (dto.getUsername() != null) {
            profile.setUsername(dto.getUsername());
        } else if (dto.getName() != null) {
            profile.setUsername(dto.getName());
        }

        if (dto.getAge() != null) {
            profile.setAge(dto.getAge());
        }

        if (dto.getHomeplanetId() != null) {
            var homeplanet = homeplanetRepo.findById(dto.getHomeplanetId())
                .orElseThrow(() -> new RuntimeException("Homeplanet not found: " + dto.getHomeplanetId()));
            profile.setHomeplanet(homeplanet);
        }

        if (dto.getBodyformId() != null) {
            var bodyform = bodyformRepo.findById(dto.getBodyformId())
                .orElseThrow(() -> new RuntimeException("Bodyform not found: " + dto.getBodyformId()));
            profile.setBodyform(bodyform);
        }

        if (dto.getLookingForId() != null) {
            var lookingFor = lookingForRepo.findById(dto.getLookingForId())
                .orElseThrow(() -> new RuntimeException("LookingFor not found: " + dto.getLookingForId()));
            profile.setLookingFor(lookingFor);
        }

        if (dto.getBio() != null) {
            profile.setBio(dto.getBio());
        }

        if (dto.getInterestIds() != null) {
            var interests = dto.getInterestIds().stream()
                .filter(id -> id != null)
                .map(id -> interestRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Interest not found: " + id)))
                .collect(Collectors.toSet());
            profile.setInterests(interests);
        }

        if (dto.getProfilePic() != null) {
            profile.setProfilePic(dto.getProfilePic());
        }
    }

    @Transactional(readOnly = true)
    public ProfileViewDTO getMyProfileDTO() {
        return toViewDTO(getMyProfile());
    }

    @Transactional
    public Profile getMyProfile() {
        List<Profile> profiles = profileRepo.findAll();
        if (!profiles.isEmpty()) {
            return profileRepo.findByIdWithRelations(profiles.get(0).getId());
        }
        return createDummyProfile();
    }

    @Transactional
    public Profile getOrCreateProfile(UUID userId, String username) {
        return profileRepo.findByUserId(userId)
            .orElseGet(() -> createProfileForUser(userId, username));
    }

    private Profile createDummyProfile() {
        var homeplanet = homeplanetRepo.findAll().stream().findFirst().orElse(null);
        var bodyform = bodyformRepo.findAll().stream().findFirst().orElse(null);
        var lookingFor = lookingForRepo.findAll().stream().findFirst().orElse(null);
        var interests = interestRepo.findAll().stream().limit(2).collect(Collectors.toSet());

        Profile dummy = new Profile("TestUser", 25, homeplanet, bodyform, lookingFor,
                "This is a dummy profile for testing.", interests, "default-profile.png");

        return profileRepo.saveAndFlush(dummy);
    }

    private Profile createProfileForUser(UUID userId, String username) {
        var homeplanet = homeplanetRepo.findAll().stream().findFirst().orElse(null);
        var bodyform = bodyformRepo.findAll().stream().findFirst().orElse(null);
        var lookingFor = lookingForRepo.findAll().stream().findFirst().orElse(null);
        var interests = interestRepo.findAll().stream().limit(2).collect(Collectors.toSet());

        Profile newProfile = new Profile();
        newProfile.setUserId(userId);
        newProfile.setUsername(username);
        newProfile.setAge(20);
        newProfile.setHomeplanet(homeplanet);
        newProfile.setBodyform(bodyform);
        newProfile.setLookingFor(lookingFor);
        newProfile.setBio("Auto-created profile");
        newProfile.setInterests(interests);
        newProfile.setProfilePic("default-profile.png");

        return profileRepo.saveAndFlush(newProfile);
    }

    // -------------------- IMAGE HANDLING --------------------

    @Transactional
    public ProfileViewDTO uploadProfileImage(MultipartFile file) throws IOException {
        Profile profile = getMyProfile();

        // Delete old image if exists
        String oldImage = profile.getProfilePic();
        if (oldImage != null && !oldImage.isEmpty() && !oldImage.startsWith("http")) {
            try {
                fileStorageService.deleteFile(oldImage);
            } catch (IOException e) {
                System.out.println("Warning: Could not delete old image: " + e.getMessage());
            }
        }

        // Save new image
        String filename = fileStorageService.saveFile(file);
        profile.setProfilePic(filename);
        
        Profile saved = profileRepo.saveAndFlush(profile);
        Profile reloaded = profileRepo.findByIdWithRelations(saved.getId());
        
        return toViewDTO(reloaded);
    }

    public Resource getProfileImage() throws IOException {
        Profile profile = getMyProfile();
        return fileStorageService.loadFile(profile.getProfilePic());
    }

    // -------------------- DTO Conversion --------------------

    public ProfileViewDTO toViewDTO(Profile profile) {
        ProfileViewDTO dto = new ProfileViewDTO();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setName(profile.getUsername());
        dto.setAge(profile.getAge());

        if (profile.getHomeplanet() != null) {
            dto.setHomeplanet(profile.getHomeplanet().getName());
            dto.setHomeplanetId(profile.getHomeplanet().getId());
        }

        if (profile.getBodyform() != null) {
            dto.setBodyform(profile.getBodyform().getName());
            dto.setBodyformId(profile.getBodyform().getId());
        }

        if (profile.getLookingFor() != null) {
            dto.setLookingFor(profile.getLookingFor().getName());
            dto.setLookingForId(profile.getLookingFor().getId());
        }

        dto.setBio(profile.getBio());

        List<Interest> sortedInterests = profile.getInterests().stream()
                .sorted((i1, i2) -> i1.getName().compareTo(i2.getName()))
                .collect(Collectors.toList());

        dto.setInterests(sortedInterests.stream()
                .map(Interest::getName)
                .collect(Collectors.toSet()));
        dto.setInterestIds(sortedInterests.stream()
                .map(Interest::getId)
                .collect(Collectors.toList()));

        dto.setProfilePic(profile.getProfilePic());

        return dto;
    }
}