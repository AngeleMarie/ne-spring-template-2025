package rw.rra.management.vehicles.users;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email_unq", columnList = "email", unique = true),
        @Index(name = "idx_user_phonenumber_unq", columnList = "phoneNumber", unique = true),
        @Index(name = "idx_user_nationalid_unq", columnList = "nationalId", unique = true)
})

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 16)
    private String nationalId;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



    private boolean enabled = false;

}
