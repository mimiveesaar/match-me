package tech.kood.match_me.profile.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import tech.kood.match_me.profile.dto.ProfileDTO;
import tech.kood.match_me.profile.dto.ProfileViewDTO;
import tech.kood.match_me.profile.model.Interest;
import tech.kood.match_me.profile.model.Profile;
import tech.kood.match_me.profile.repository.BodyformRepository;
import tech.kood.match_me.profile.repository.HomeplanetRepository;
import tech.kood.match_me.profile.repository.InterestRepository;
import tech.kood.match_me.profile.repository.LookingForRepository;
import tech.kood.match_me.profile.repository.ProfileRepository;

@Service
public class ProfileService {

    private final ProfileRepository profileRepo;
    private final HomeplanetRepository homeplanetRepo;
    private final BodyformRepository bodyformRepo;
    private final LookingForRepository lookingForRepo;
    private final InterestRepository interestRepo;

    public ProfileService(ProfileRepository profileRepo, HomeplanetRepository homeplanetRepo,
            BodyformRepository bodyformRepo, LookingForRepository lookingForRepo,
            InterestRepository interestRepo) {
        this.profileRepo = profileRepo;
        this.homeplanetRepo = homeplanetRepo;
        this.bodyformRepo = bodyformRepo;
        this.lookingForRepo = lookingForRepo;
        this.interestRepo = interestRepo;
    }

    @Transactional("profileManagementTransactionManager")
    public ProfileViewDTO saveOrUpdateProfile(ProfileDTO dto) {
        Profile profile = getMyProfile(); // fetch existing profile

        // OR if you want to handle both:
        if (dto.getUsername() != null) {
            System.out.println("Updating username to: " + dto.getUsername());
            profile.setUsername(dto.getUsername());
        } else if (dto.getName() != null) {
            System.out.println("Updating username from name field to: " + dto.getName());
            profile.setUsername(dto.getName());
        }

        if (dto.getAge() != null) {
            profile.setAge(dto.getAge());
        }
        if (dto.getHomeplanetId() != null) {
            profile.setHomeplanet(homeplanetRepo.findById(dto.getHomeplanetId())
                    .orElseThrow(() -> new RuntimeException("Homeplanet not found")));
        }
        if (dto.getBodyformId() != null) {
            profile.setBodyform(bodyformRepo.findById(dto.getBodyformId())
                    .orElseThrow(() -> new RuntimeException("Bodyform not found")));
        }
        if (dto.getLookingForId() != null) {
            profile.setLookingFor(lookingForRepo.findById(dto.getLookingForId())
                    .orElseThrow(() -> new RuntimeException("LookingFor not found")));
        }
        if (dto.getBio() != null) {
            profile.setBio(dto.getBio());
        }
        if (dto.getInterestIds() != null && !dto.getInterestIds().isEmpty()) {
            // Clear existing interests first
            profile.getInterests().clear();

            // Add new interests
            profile.setInterests(dto.getInterestIds().stream()
                    .map(id -> interestRepo.findById(id)
                            .orElseThrow(() -> new RuntimeException("Interest not found")))
                    .collect(Collectors.toSet()));
        }
        if (dto.getProfilePic() != null) {
            profile.setProfilePic(dto.getProfilePic());
        }

        // Force a flush to ensure changes are persisted
        Profile savedProfile = profileRepo.saveAndFlush(profile);

        // Convert to DTO while still in transaction
        return toViewDTO(savedProfile);
    }

    @Transactional(value = "profileManagementTransactionManager", readOnly = true)
    public ProfileViewDTO getMyProfileDTO() {
        Profile profile = getMyProfile();
        return toViewDTO(profile);
    }

    @Transactional(value = "profileManagementTransactionManager", readOnly = true)
    public Profile getMyProfile() {

        return profileRepo.findAllWithRelations().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No profile found"));
    }

    @Value("${app.upload.dir:${user.home}/profile-images}")
    private String uploadDir;

    public String saveProfileImage(MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        // Save file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename; // Store just filename, not full path
    }

    @Transactional("profileManagementTransactionManager")
    public ProfileViewDTO updateProfileImage(String imagePath) {
        Profile profile = getMyProfile();

        // Delete old image if it exists
        String oldImage = profile.getProfilePic();
        if (oldImage != null && !oldImage.isEmpty() && !oldImage.startsWith("http")) {
            try {
                deleteImageFile(oldImage);
            } catch (IOException e) {
                System.out.println("Warning: Could not delete old image: " + e.getMessage());
            }
        }

        profile.setProfilePic(imagePath);
        Profile savedProfile = profileRepo.saveAndFlush(profile);
        return toViewDTO(savedProfile);
    }

    public Resource getProfileImage() throws IOException {
        Profile profile = getMyProfile();
        String filename = profile.getProfilePic();

        // If no profile pic is set, use default
        if (filename == null || filename.isEmpty()) {
            return getDefaultProfileImage();
        }

        // If it's a URL, we can't serve it directly - use default
        if (filename.startsWith("http")) {
            return getDefaultProfileImage();
        }

        Path imagePath = Paths.get(uploadDir).resolve(filename);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            // If file doesn't exist, return default instead of error
            System.out.println("Profile image not found: " + filename + ", using default");
            return getDefaultProfileImage();
        }
    }

    private Resource getDefaultProfileImage() throws IOException {
        // Load default image from resources
        ClassPathResource defaultImage = new ClassPathResource("static/images/default-profile.png");

        if (defaultImage.exists()) {
            return defaultImage;
        } else {
            throw new FileNotFoundException(
                    "Default profile image not found at: static/images/default-profile.png");
        }
    }

    public String getDefaultProfileImageUrl() {
        return "/images/default-profile.png";
    }

    private void deleteImageFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty() || filename.startsWith("http")) {
            return;
        }

        Path imagePath = Paths.get(uploadDir).resolve(filename);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            System.out.println("Deleted old image: " + imagePath);
        }
    }

    private ProfileViewDTO toViewDTO(Profile profile) {
        ProfileViewDTO dto = new ProfileViewDTO();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setName(profile.getUsername());
        dto.setAge(profile.getAge());

        // Include both names and IDs
        dto.setHomeplanet(profile.getHomeplanet().getName());
        dto.setHomeplanetId(profile.getHomeplanet().getId().intValue()); // Add this

        dto.setBodyform(profile.getBodyform().getName());
        dto.setBodyformId(profile.getBodyform().getId().intValue()); // Add this

        dto.setLookingFor(profile.getLookingFor().getName());
        dto.setLookingForId(profile.getLookingFor().getId().intValue()); // Add this

        dto.setBio(profile.getBio());

        // Sort interests alphabetically by name
        List<Interest> sortedInterests = profile.getInterests().stream()
                .sorted((i1, i2) -> i1.getName().compareTo(i2.getName()))
                .collect(Collectors.toList());

        dto.setInterests(
                sortedInterests.stream().map(i -> i.getName()).collect(Collectors.toSet()));

        dto.setInterestIds(sortedInterests.stream().map(i -> i.getId().intValue())
                .collect(Collectors.toList()));

        dto.setProfilePic(profile.getProfilePic());

        return dto;
    }
}
