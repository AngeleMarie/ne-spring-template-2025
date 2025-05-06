package rw.rra.management.vehicles.plates;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.rra.management.vehicles.plates.dtos.RegisterPlateNumberDto;
import rw.rra.management.vehicles.plates.dtos.PlateNumberResponseDto;
import rw.rra.management.vehicles.plates.PlateService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plates")
@RequiredArgsConstructor
public class PlateController {

    private final PlateService plateNumberService;

    @PostMapping("/add/{ownerId}")
    public ResponseEntity<PlateNumberResponseDto> registerPlate(
            @PathVariable UUID ownerId,
            @RequestBody @Valid RegisterPlateNumberDto dto) {
        var response = plateNumberService.registerPlate(ownerId, dto);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Page<PlateNumberResponseDto>> getPlatesByOwner(
            @PathVariable UUID ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var response = plateNumberService.getPlatesByOwner(ownerId, page, size);
        return ResponseEntity.ok(response);
    }

}
