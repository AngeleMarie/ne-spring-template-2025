package rw.rra.management.vehicles.plates;

import jakarta.persistence.*;
import lombok.*;
import rw.rra.management.vehicles.owners.Owner;
import rw.rra.management.vehicles.ownership.OwnershipTransfer;
import rw.rra.management.vehicles.vehicles.Vehicle;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "plate_numbers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "plate_number")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlateNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "plate_number", nullable = false, unique = true, length = 20)
    private String plateNumber;

    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlateStatus status=PlateStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @OneToOne(mappedBy = "plateNumber", fetch = FetchType.LAZY)
    private Vehicle vehicle;


    @OneToMany(mappedBy = "oldPlate")
    private List<OwnershipTransfer> oldPlateTransfers;


    @OneToOne(mappedBy = "newPlate")
    private OwnershipTransfer newPlateTransfer;


}


