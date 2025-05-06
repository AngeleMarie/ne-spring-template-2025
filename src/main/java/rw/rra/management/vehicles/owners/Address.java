package rw.rra.management.vehicles.owners;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {

    @Column(name = "owner_district", nullable = false, length = 100)
    private String district;

    @Column(name = "owner_province", nullable = false, length = 100)
    private String province;

    @Column(name = "owner_street", nullable = false, length = 200)
    private String street;
}
