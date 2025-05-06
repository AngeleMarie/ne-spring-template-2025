package rw.rra.management.vehicles.plates;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rw.rra.management.vehicles.commons.exceptions.BadRequestException;
import rw.rra.management.vehicles.email.EmailService;
import rw.rra.management.vehicles.plates.dtos.PlateNumberResponseDto;
import  rw.rra.management.vehicles.plates.PlateRepository;
import  rw.rra.management.vehicles.owners.OwnerRepository;
import rw.rra.management.vehicles.plates.dtos.RegisterPlateNumberDto;
import rw.rra.management.vehicles.plates.mappers.PlateNumberMapper;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PlateService {
    private final PlateRepository plateRepository;
    private final OwnerRepository ownerRepository;
    private final PlateNumberMapper plateNumberMapper;
    private final EmailService emailService;

    public PlateNumberResponseDto registerPlate(UUID ownerId, RegisterPlateNumberDto dto) {
        var owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new BadRequestException("Owner not found"));

        var existingPlate = plateRepository.findByPlateNumber(dto.plateNumber());
        if (existingPlate.isPresent()) {
            throw new BadRequestException("Plate number already exists");
        }

        var plateNumber = plateNumberMapper.toEntity(dto, owner);
        plateRepository.save(plateNumber);

        emailService.sendPlateCreatedEmail(owner.getEmail(), owner.getFirstName(), dto.plateNumber());

        return plateNumberMapper.toDto(plateNumber);
    }

    public Page<PlateNumberResponseDto> getPlatesByOwner(UUID ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PlateNumber> plates = plateRepository.findByOwnerId(ownerId, pageable);
        return plates.map(plateNumberMapper::toDto);
    }

}
