package tech.kood.match_me.user_management.api.DTOs;

public record UserDTO(
        String id,
        String username,
        String email) {

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