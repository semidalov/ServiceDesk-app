package sdi.servicedesk.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty
    @Column(name = "username")
    private String username;

    @NotEmpty
    @Column(name = "pass_hash")
    private String password;

    @NotNull
    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @NotNull
    @Column(name = "locked")
    private Boolean locked;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private UserRole role;

    @ToString.Exclude
    @NotNull
    @OneToOne(mappedBy = "user")
    private Employee employee;
}
