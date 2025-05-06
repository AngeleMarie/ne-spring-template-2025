package rw.rra.management.vehicles.ownership;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OwnershipTransferRepository extends JpaRepository<OwnershipTransfer, UUID> {

    Page<OwnershipTransfer> findByVehicle_ChassisNumber(String chassisNumber, Pageable pageable);

    Page<OwnershipTransfer> findByVehicle_PlateNumber_PlateNumber(String plateNumber, Pageable pageable);
}
