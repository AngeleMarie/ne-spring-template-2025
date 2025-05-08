package rw.rra.management.vehicles.vehicles;



import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rw.rra.management.vehicles.email.EmailService;
import rw.rra.management.vehicles.owners.OwnerRepository;
import rw.rra.management.vehicles.plates.PlateRepository;
import rw.rra.management.vehicles.plates.PlateStatus;
import rw.rra.management.vehicles.vehicles.dtos.VehicleRequestDto;
import rw.rra.management.vehicles.vehicles.dtos.VehicleResponseDto;
import rw.rra.management.vehicles.vehicles.mappers.VehicleMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final EmailService emailService;
    private final PlateRepository plateRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleResponseDto registerVehicle(VehicleRequestDto dto) {
        var vehicle = vehicleMapper.toEntity(dto);

        return vehicleMapper.toDto(vehicleRepository.save(vehicle));
    }


    public Page<VehicleResponseDto> getAllVehicles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return vehicleRepository.findAll(pageable)
                .map(vehicleMapper::toDto);
    }


    public VehicleResponseDto getVehicleByPlateNumber(String plateNumber) {
        return vehicleRepository.findByPlateNumber(plateNumber)
                .map(vehicleMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public VehicleResponseDto getVehicleByChassisNumber(String chassisNumber) {
        return vehicleRepository.findByChassisNumber(chassisNumber)
                .map(vehicleMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public Page<VehicleResponseDto> getVehiclesByOwnerNationalId(String nationalId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return vehicleRepository.findByOwnerNationalId(nationalId, pageable)
                .map(vehicleMapper::toDto);
    }


    public VehicleResponseDto assignPlateAndOwner(UUID vehicleId, String plateNumberStr) {
        var vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        var plate = plateRepository.findByPlateNumber(plateNumberStr)
                .orElseThrow(() -> new RuntimeException("Plate number not found"));

        if (plate.getStatus() != PlateStatus.AVAILABLE) {
            throw new RuntimeException("Plate number is not available for assignment");
        }

        var owner = plate.getOwner();
        if (owner == null) {
            throw new RuntimeException("This plate number is not linked to any owner");
        }

        vehicle.setOwner(owner);
        vehicle.setPlateNumber(plate);
        plate.setStatus(PlateStatus.IN_USE);

        plateRepository.save(plate);
        var updatedVehicle = vehicleRepository.save(vehicle);

        String vehicleDetails = String.format(
                "Brand: %s, ModelName: %s, Chassis No: %s, Color: %s",
                vehicle.getBrand(),
                vehicle.getModelName(),
                vehicle.getChassisNumber(),
                vehicle.getColor()
        );

        emailService.sendPlateAssignedEmail(owner.getEmail(), owner.getFirstName(), plateNumberStr, vehicleDetails);

        return vehicleMapper.toDto(updatedVehicle);
    }




}

