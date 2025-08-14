package tech.kood.match_me.user_management.api.DTOs;


import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UUID;

public record UserDTO(@NotEmpty @UUID String id, @NotEmpty String username, @Email String email) {

    public UserDTO withId(String id) {
        return new UserDTO(id, this.username, this.email);
    }

    public UserDTO withUsername(String username) {
        return new UserDTO(this.id, username, this.email);
    }

    public UserDTO withEmail(String email) {
        return new UserDTO(this.id, this.username, email);
    }
}
