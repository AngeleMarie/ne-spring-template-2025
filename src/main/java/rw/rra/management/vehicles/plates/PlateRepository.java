package rw.rra.management.vehicles.plates;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.rra.management.vehicles.owners.Owner;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlateRepository extends JpaRepository<PlateNumber, UUID> {
    Optional<PlateNumber> findByPlateNumber(String plateNumber);

    Page<PlateNumber> findByOwnerId(UUID ownerId, Pageable pageable);


}

