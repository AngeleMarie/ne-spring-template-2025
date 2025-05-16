package rw.rra.management.vehicles.owners;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {

    @Query("SELECT o FROM Owner o WHERE " +
            "LOWER(REPLACE(o.firstName, ' ', '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(REPLACE(o.lastName, ' ', '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(REPLACE(o.email, ' ', '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "REPLACE(o.phoneNumber, ' ', '') LIKE CONCAT('%', :keyword, '%') OR " +
            "REPLACE(o.nationalId, ' ', '') LIKE CONCAT('%', :keyword, '%')")
    List<Owner> searchByKeyword(String keyword);
    @Query("SELECT o FROM Owner o LEFT JOIN FETCH o.vehicles LEFT JOIN FETCH o.plateNumbers WHERE o.email = :email")
    Optional<Owner> findByEmailWithVehiclesAndPlates(String email);

}
