package rw.rra.management.vehicles.owners;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import rw.rra.management.vehicles.ownership.OwnershipTransfer;
import rw.rra.management.vehicles.plates.PlateNumber;
import rw.rra.management.vehicles.users.User;
import rw.rra.management.vehicles.vehicles.Vehicle;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "owners")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Owner {

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

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlateNumber> plateNumbers;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "district", column = @Column(name = "owner_district")),
            @AttributeOverride(name = "province", column = @Column(name = "owner_province")),
            @AttributeOverride(name = "street", column = @Column(name = "owner_street"))
    })
    private Address address;

    @OneToMany(mappedBy = "fromOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnershipTransfer> transfersFrom;

    @OneToMany(mappedBy = "toOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OwnershipTransfer> transfersTo;

}
