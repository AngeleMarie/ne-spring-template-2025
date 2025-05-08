package rw.rra.management.vehicles.vehicles;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.rra.management.vehicles.vehicles.dtos.AssignPlateAndOwnerRequest;
import rw.rra.management.vehicles.vehicles.dtos.VehicleRequestDto;
import rw.rra.management.vehicles.vehicles.dtos.VehicleResponseDto;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/register")
    public ResponseEntity<VehicleResponseDto> registerVehicle(@RequestBody @Valid VehicleRequestDto dto) {
        return ResponseEntity.ok(vehicleService.registerVehicle(dto));
    }

    @GetMapping("/get-all")
    public Page<VehicleResponseDto> getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return vehicleService.getAllVehicles(page, size);
    }


    @GetMapping("/by-national-id")
    public ResponseEntity<Page<VehicleResponseDto>> getVehiclesByNationalId(
            @RequestParam String nationalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(vehicleService.getVehiclesByOwnerNationalId(nationalId, page, size));
    }


    @GetMapping("/by-chassis")
    public ResponseEntity<VehicleResponseDto> getVehicleByChassisNumber(@RequestParam String chassisNumber) {
        return ResponseEntity.ok(vehicleService.getVehicleByChassisNumber(chassisNumber));
    }


    @GetMapping("/by-plate")
    public ResponseEntity<VehicleResponseDto> getVehicleByPlate(@RequestParam String plateNumber) {
        return ResponseEntity.ok(vehicleService.getVehicleByPlateNumber(plateNumber));
    }
    @PutMapping("/assign-plate-owner/{vehicleId}")
    public VehicleResponseDto assignPlateAndOwner(
            @PathVariable UUID vehicleId,
            @RequestBody AssignPlateAndOwnerRequest request
    ) {
        return vehicleService.assignPlateAndOwner(vehicleId, request.plateNumber());
    }


}

