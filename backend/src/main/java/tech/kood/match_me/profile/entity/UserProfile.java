
package tech.kood.match_me.profile.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user", schema = "profile")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile {

    @Id
    private String username;

    private String age;
    private String bodyform;
    private String mail;
    private String password;
    private String planet;
    
    @Column(name = "looking_for")
    private String lookingFor;

    private String interests;
    private String bio;

    @Column(name = "profile_pic")
    private String profilePic;
}
