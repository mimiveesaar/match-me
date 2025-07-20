package tech.kood.match_me.user_management.models;

public record HashedPassword(String passwordHash, String passwordSalt) {
    
    public HashedPassword {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or blank");
        }
        if (passwordSalt == null || passwordSalt.isBlank()) {
            throw new IllegalArgumentException("Password salt cannot be null or blank");
        }
    }

    public HashedPassword passwordHash(String newPasswordHash) {
        return new HashedPassword(newPasswordHash, this.passwordSalt);
    }
    public HashedPassword passwordSalt(String newPasswordSalt) {
        return new HashedPassword(this.passwordHash, newPasswordSalt);
    }
}
