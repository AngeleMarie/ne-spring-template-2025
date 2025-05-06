package rw.rra.management.vehicles.owners;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.rra.management.vehicles.owners.dtos.OwnerRequestDto;
import rw.rra.management.vehicles.owners.dtos.OwnerResponseDto;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/add")
    public ResponseEntity<OwnerResponseDto> registerOwner(@RequestBody @Valid OwnerRequestDto dto) {
        return ResponseEntity.ok(ownerService.registerOwner(dto));
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<OwnerResponseDto>> getAllOwners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ownerService.getAllOwners(page, size));
    }


    @GetMapping("/search")
    public ResponseEntity<List<OwnerResponseDto>> searchOwners(@RequestParam String keyword) {
        return ResponseEntity.ok(ownerService.searchOwners(keyword));
    }

}
