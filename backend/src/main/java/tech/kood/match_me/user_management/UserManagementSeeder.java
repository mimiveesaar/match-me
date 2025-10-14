package tech.kood.match_me.user_management;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tech.kood.match_me.seeding.SeedUserGenerator;
import tech.kood.match_me.user_management.features.user.internal.mapper.UserMapper;
import tech.kood.match_me.user_management.features.user.internal.persistance.UserRepository;

@Component
public class UserManagementSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SeedUserGenerator seedUserGenerator;
    private final UserMapper userMapper;

    public UserManagementSeeder(UserRepository userManagement, SeedUserGenerator seedUserGenerator, UserMapper userMapper) {
        this.userRepository = userManagement;
        this.seedUserGenerator = seedUserGenerator;
        this.userMapper = userMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        var users = seedUserGenerator.generate();

        for (var user : users) {
            var userEntities = userMapper.toEntity(userMapper.toUser(user));
            this.userRepository.saveUser(userEntities);
        }
    }
}