package tech.kood.match_me.profile.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

    @Value("${app.upload.dir:${user.home}/profile-images}")
    private String uploadDir;

    public ProfileService(ProfileRepository profileRepo, HomeplanetRepository homeplanetRepo,
            BodyformRepository bodyformRepo, LookingForRepository lookingForRepo,
            InterestRepository interestRepo, ApplicationEventPublisher eventPublisher) {
        this.profileRepo = profileRepo;
        this.homeplanetRepo = homeplanetRepo;
        this.bodyformRepo = bodyformRepo;
        this.lookingForRepo = lookingForRepo;
        this.interestRepo = interestRepo;
        this.eventPublisher = eventPublisher;
    }

    // -------------------- PROFILE CRUD --------------------

    @Transactional
    public ProfileViewDTO saveOrUpdateProfile(ProfileDTO dto) {
        System.out.println("=== saveOrUpdateProfile called ===");
        Profile profile = getMyProfile();

        if (dto.getUsername() != null) {
            profile.setUsername(dto.getUsername());
        } else if (dto.getName() != null) {
            profile.setUsername(dto.getName());
        }

        if (dto.getAge() != null)
            profile.setAge(dto.getAge());

        if (dto.getHomeplanetId() != null) {
            var homeplanet = homeplanetRepo.findById(dto.getHomeplanetId()).orElseThrow(
                    () -> new RuntimeException("Homeplanet not found: " + dto.getHomeplanetId()));
            profile.setHomeplanet(homeplanet);
        }

        if (dto.getBodyformId() != null) {
            var bodyform = bodyformRepo.findById(dto.getBodyformId()).orElseThrow(
                    () -> new RuntimeException("Bodyform not found: " + dto.getBodyformId()));
            profile.setBodyform(bodyform);
        }

        if (dto.getLookingForId() != null) {
            var lookingFor = lookingForRepo.findById(dto.getLookingForId()).orElseThrow(
                    () -> new RuntimeException("LookingFor not found: " + dto.getLookingForId()));
            profile.setLookingFor(lookingFor);
        }

        if (dto.getBio() != null)
            profile.setBio(dto.getBio());

        if (dto.getInterestIds() != null) {
            var interests = dto.getInterestIds().stream().filter(id -> id != null)
                    .map(id -> interestRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Interest not found: " + id)))
                    .collect(Collectors.toSet());
            profile.setInterests(interests);
        }

        if (dto.getProfilePic() != null)
            profile.setProfilePic(dto.getProfilePic());

        Profile saved = profileRepo.saveAndFlush(profile);
        
        // Reload with all relations to avoid lazy loading issues
        Profile reloaded = profileRepo.findByIdWithRelations(saved.getId());
        ProfileViewDTO result = toViewDTO(reloaded);
        
        eventPublisher.publishEvent(new ProfileChangedDTOEvent(this, result));
        return result;
    }

    @Transactional(readOnly = true)
    public ProfileViewDTO getMyProfileDTO() {
        return toViewDTO(getMyProfile());
    }

    @Transactional
    public Profile getOrCreateProfile(UUID userId, String username) {
        return profileRepo.findByUserId(userId).orElseGet(() -> {
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
        });
    }

    @Transactional
    public Profile getMyProfile() {
        List<Profile> profiles = profileRepo.findAll();
        if (!profiles.isEmpty()) {
            return profileRepo.findByIdWithRelations(profiles.get(0).getId());
        }

        var homeplanet = homeplanetRepo.findAll().stream().findFirst().orElse(null);
        var bodyform = bodyformRepo.findAll().stream().findFirst().orElse(null);
        var lookingFor = lookingForRepo.findAll().stream().findFirst().orElse(null);
        var interests = interestRepo.findAll().stream().limit(2).collect(Collectors.toSet());

        Profile dummy = new Profile("TestUser", 25, homeplanet, bodyform, lookingFor,
                "This is a dummy profile for testing.", interests, "default-profile.png");

        return profileRepo.saveAndFlush(dummy);
    }

    // -------------------- IMAGE HANDLING --------------------

    public String saveProfileImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    @Transactional
    public ProfileViewDTO updateProfileImage(String imagePath) {
        Profile profile = getMyProfile();

        String oldImage = profile.getProfilePic();
        if (oldImage != null && !oldImage.isEmpty() && !oldImage.startsWith("http")) {
            try {
                deleteImageFile(oldImage);
            } catch (IOException e) {
                System.out.println("Warning: Could not delete old image: " + e.getMessage());
            }
        }

        profile.setProfilePic(imagePath);
        Profile saved = profileRepo.saveAndFlush(profile);
        
        // Reload with all relations to avoid lazy loading issues
        Profile reloaded = profileRepo.findByIdWithRelations(saved.getId());
        return toViewDTO(reloaded);
    }

    public Resource getProfileImage() throws IOException {
        Profile profile = getMyProfile();
        String filename = profile.getProfilePic();

        if (filename == null || filename.isEmpty() || filename.startsWith("http")) {
            return getDefaultProfileImage();
        }

        Path imagePath = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(imagePath.toUri());
        return (resource.exists() && resource.isReadable()) ? resource : getDefaultProfileImage();
    }

    private Resource getDefaultProfileImage() throws IOException {
        ClassPathResource defaultImage = new ClassPathResource("static/images/default-profile.png");
        if (defaultImage.exists())
            return defaultImage;
        throw new FileNotFoundException(
                "Default profile image not found at static/images/default-profile.png");
    }

    private void deleteImageFile(String filename) throws IOException {
        Path imagePath = Paths.get(uploadDir).resolve(filename);
        if (Files.exists(imagePath))
            Files.delete(imagePath);
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

        dto.setInterests(
                sortedInterests.stream().map(Interest::getName).collect(Collectors.toSet()));
        dto.setInterestIds(
                sortedInterests.stream().map(Interest::getId).collect(Collectors.toList()));

        dto.setProfilePic(profile.getProfilePic());

        return dto;
    }
}