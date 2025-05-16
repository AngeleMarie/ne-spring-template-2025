package rw.rra.management.vehicles.users;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.rra.management.vehicles.commons.exceptions.BadRequestException;
import rw.rra.management.vehicles.owners.Owner;
import rw.rra.management.vehicles.owners.OwnerRepository;
import rw.rra.management.vehicles.plates.PlateNumber;
import rw.rra.management.vehicles.plates.dtos.PlateNumberResponseDto;
import rw.rra.management.vehicles.plates.mappers.PlateNumberMapper;
import rw.rra.management.vehicles.users.dtos.UserResponseDto;
import rw.rra.management.vehicles.vehicles.Vehicle;
import rw.rra.management.vehicles.vehicles.dtos.VehicleResponseDto;
import rw.rra.management.vehicles.vehicles.mappers.VehicleMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final VehicleMapper vehicleMapper;
    private final PlateNumberMapper plateNumberMapper;
    private final UserService userService;
    private final OwnerRepository ownerRepository;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentLoggedInUser());
    }

    @GetMapping("/me/vehicles")
    @PreAuthorize("hasRole('STANDARD')")
    public ResponseEntity<Map<String, Object>> getMyRegisteredVehiclesAndPlates() {
        User authenticatedUser = userService.getAuthenticatedUser();

        Owner owner = ownerRepository.findByEmailWithVehiclesAndPlates(authenticatedUser.getEmail())
                .orElseThrow(() -> new BadRequestException(
                        "No owner record found for your account. Please ensure your account is linked with vehicle ownership."
                ));

        List<VehicleResponseDto> vehicleDTOs = vehicleMapper.toDtoList(new ArrayList<>(owner.getVehicles()));
        List<PlateNumberResponseDto> plateDTOs = plateNumberMapper.toDtoList(new ArrayList<>(owner.getPlateNumbers()));


        Map<String, Object> response = new HashMap<>();
        response.put("owner", owner.getFirstName() + " " + owner.getLastName());
        response.put("vehicles", vehicleDTOs);
        response.put("plates", plateDTOs);

        return ResponseEntity.ok(response);
    }
}
