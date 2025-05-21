    package rw.rra.management.vehicles.owners;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.*;
    import rw.rra.management.vehicles.ownership.OwnershipTransfer;
    import rw.rra.management.vehicles.plates.PlateNumber;
    import rw.rra.management.vehicles.vehicles.Vehicle;

    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;
    import java.util.UUID;

    @Entity
    @Table(name = "owners")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor

    @JsonIgnoreProperties(value = {"createdAt", "updatedAt", "createdBy", "updatedBy"}, allowGetters = true)
    public class Owner  {

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

        @JsonManagedReference
        @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private Set<Vehicle> vehicles = new HashSet<>();

        @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private Set<PlateNumber> plateNumbers = new HashSet<>();


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
