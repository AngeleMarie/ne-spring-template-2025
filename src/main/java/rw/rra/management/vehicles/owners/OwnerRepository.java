package rw.rra.management.vehicles.owners;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {

    List<Owner> findByNationalIdContainingOrEmailContainingOrPhoneNumberContaining(String nationalId, String email, String phoneNumber);
}
