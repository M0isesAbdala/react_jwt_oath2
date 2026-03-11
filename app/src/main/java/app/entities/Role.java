package app.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;

    @JsonValue
    public String jsonValue() {
        return name;
    }

    @Getter
    public enum Roles {

        ADMIN(1L),
        USER(2L);

        final long roleId;

        Roles(long roleId) {
            this.roleId = roleId;
        }
    }
}
