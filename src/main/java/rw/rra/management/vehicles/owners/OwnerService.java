package rw.rra.management.vehicles.owners;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rw.rra.management.vehicles.email.EmailService;
import rw.rra.management.vehicles.owners.dtos.OwnerRequestDto;
import rw.rra.management.vehicles.owners.dtos.OwnerResponseDto;
import rw.rra.management.vehicles.owners.mappers.OwnerMapper;
import rw.rra.management.vehicles.plates.PlateNumber;
import rw.rra.management.vehicles.plates.PlateRepository;
import rw.rra.management.vehicles.plates.PlateStatus;

import java.util.List;
import rw.rra.management.vehicles.utils.Utility;

@Service
@RequiredArgsConstructor
public class OwnerService {

    @Autowired
    private Utility utility;
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final PlateRepository plateRepository;
    private final EmailService emailService;

    public OwnerResponseDto registerOwner(OwnerRequestDto dto) {


        Owner owner = ownerMapper.toEntity(dto);
        ownerRepository.save(owner);
        String plateNumber = utility.generateUniquePlateNumber();
        PlateNumber plate = new PlateNumber();
        plate.setPlateNumber(plateNumber);
        plate.setStatus(PlateStatus.IN_USE);
        plate.setOwner(owner); // Assuming bidirectional mapping
        plateRepository.save(plate);
        emailService.sendUserRegisteredEmail(owner.getEmail(), owner.getFirstName(),plate.getPlateNumber());

        return ownerMapper.toDto(owner);
    }


    public Page<OwnerResponseDto> getAllOwners(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ownerRepository.findAll(pageable)
                .map(ownerMapper::toDto);
    }


    public List<OwnerResponseDto> searchOwners(String keyword) {
        return ownerRepository
                .findByNationalIdContainingOrEmailContainingOrPhoneNumberContaining(keyword, keyword, keyword)
                .stream()
                .map(ownerMapper::toDto)
                .toList();
    }
}
