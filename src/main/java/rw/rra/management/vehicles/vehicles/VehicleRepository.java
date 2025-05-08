package rw.rra.management.vehicles.vehicles;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rw.rra.management.vehicles.vehicles.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByChassisNumber(String chassisNumber);

    @Query("SELECT v FROM Vehicle v WHERE v.plateNumber.plateNumber = :plateNumber")
    Optional<Vehicle> findByPlateNumber(String plateNumber);


    @Query("SELECT v FROM Vehicle v WHERE v.owner.nationalId = :nationalId")
    Page<Vehicle> findByOwnerNationalId(String nationalId, Pageable pageable);

}
