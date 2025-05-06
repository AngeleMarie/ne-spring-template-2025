
package rw.rra.management.vehicles.ownership;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import rw.rra.management.vehicles.ownership.dtos.OwnershipTransferRequestDto;
import rw.rra.management.vehicles.ownership.dtos.OwnershipTransferResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class OwnershipTransferController {

    private final OwnershipTransferService transferService;

    @PostMapping("/initiate-transfer")
    public OwnershipTransferResponseDto transferVehicle(@RequestBody OwnershipTransferRequestDto dto) {
        return transferService.transferOwnership(dto);
    }

    @GetMapping("/history/plate/{plateNumber}")
    public Page<OwnershipTransferResponseDto> getHistoryByPlate(
            @PathVariable String plateNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transferService.getOwnershipHistoryByPlate(plateNumber, PageRequest.of(page, size));
    }

    @GetMapping("/history/chassis/{chassisNumber}")
    public Page<OwnershipTransferResponseDto> getHistoryByChassis(
            @PathVariable String chassisNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transferService.getOwnershipHistoryByChassis(chassisNumber, PageRequest.of(page, size));
    }
}
