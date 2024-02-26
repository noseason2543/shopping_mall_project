package thanadon.project.shoppingMall.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import thanadon.project.shoppingMall.utils.UserUtils.userRole;

import javax.validation.constraints.Email;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "users", schema = "user_service")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
//    @JsonProperty("user_name")
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    @Email
    private String email;
    private String phoneNumber;
    private Date lastLoginTime;
    private String gene;
    @Enumerated(EnumType.STRING)
    private userRole role;
}
