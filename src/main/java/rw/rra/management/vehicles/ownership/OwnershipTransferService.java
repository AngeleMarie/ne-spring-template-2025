// OwnershipTransferService.java
package rw.rra.management.vehicles.ownership;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rw.rra.management.vehicles.email.EmailService;
import rw.rra.management.vehicles.ownership.dtos.OwnershipTransferRequestDto;
import rw.rra.management.vehicles.ownership.dtos.OwnershipTransferResponseDto;
import rw.rra.management.vehicles.ownership.mappers.OwnershipTransferMapper;
import rw.rra.management.vehicles.owners.OwnerRepository;
import rw.rra.management.vehicles.plates.PlateRepository;
import rw.rra.management.vehicles.plates.PlateStatus;
import rw.rra.management.vehicles.vehicles.VehicleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnershipTransferService {

    private final OwnershipTransferRepository transferRepository;
    private final VehicleRepository vehicleRepository;
    private final OwnerRepository ownerRepository;
    private final PlateRepository plateRepository;
    private final EmailService emailService;
    private final OwnershipTransferMapper transferMapper;

    public OwnershipTransferResponseDto transferOwnership(OwnershipTransferRequestDto dto) {
        var oldPlate = plateRepository.findByPlateNumber(dto.oldPlateNumber())
                .orElseThrow(() -> new RuntimeException("Old plate not found"));

        var newPlate = plateRepository.findByPlateNumber(dto.newPlateNumber())
                .orElseThrow(() -> new RuntimeException("New plate not found"));

        if (newPlate.getStatus() == PlateStatus.IN_USE) {
            throw new RuntimeException("New plate number is already in use");
        }

        var vehicle = vehicleRepository.findByPlateNumber(dto.oldPlateNumber())
                .orElseThrow(() -> new RuntimeException("Vehicle with old plate not found"));

        var fromOwner = vehicle.getOwner();

        if (newPlate.getOwner() == null) {
            throw new RuntimeException("New plate must be associated with a registered owner");
        }

        var toOwner = newPlate.getOwner();

        // Update plate statuses
        oldPlate.setStatus(PlateStatus.AVAILABLE);
        newPlate.setStatus(PlateStatus.IN_USE);
        plateRepository.saveAll(List.of(oldPlate, newPlate));

        // Update vehicle info
        vehicle.setOwner(toOwner);
        vehicle.setPlateNumber(newPlate);
        vehicleRepository.save(vehicle);

        // Save ownership transfer record
        var transfer = OwnershipTransfer.builder()
                .vehicle(vehicle)
                .fromOwner(fromOwner)
                .toOwner(toOwner)
                .oldPlate(oldPlate)
                .newPlate(newPlate)
                .transferAmount(dto.transferAmount())
                .transferDate(dto.transferDate())
                .build();
        emailService.sendOwnershipTransferEmailToSender(
                fromOwner.getEmail(), fromOwner.getFirstName(), dto.oldPlateNumber(), dto.transferAmount(), toOwner.getFirstName(),toOwner.getLastName());

        emailService.sendOwnershipTransferEmailToReceiver(
                toOwner.getEmail(), toOwner.getFirstName(), dto.newPlateNumber(), dto.transferAmount(), fromOwner.getFirstName(),fromOwner.getLastName());


        return transferMapper.toDto(transferRepository.save(transfer));
    }

    public Page<OwnershipTransferResponseDto> getOwnershipHistoryByPlate(String plateNumber, Pageable pageable) {
        return transferRepository.findByVehicle_PlateNumber_PlateNumber(plateNumber, pageable)
                .map(transferMapper::toDto);
    }

    public Page<OwnershipTransferResponseDto> getOwnershipHistoryByChassis(String chassisNumber, Pageable pageable) {
        return transferRepository.findByVehicle_ChassisNumber(chassisNumber, pageable)
                .map(transferMapper::toDto);
    }
}
