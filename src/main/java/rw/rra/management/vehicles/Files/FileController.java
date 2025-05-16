package rw.rra.management.vehicles.Files;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.rra.management.vehicles.vehicles.VehicleService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final ExcelService excelService;
    private final VehicleService vehicleService;

    @GetMapping("/export/vehicles-report")
    public ResponseEntity<byte[]> exportVehiclesReport() throws IOException {
        // Define Excel column headers
        List<String> headers = List.of("Plate Number", "Model", "Owner First Name", "Owner Last Name", "Chassis Number");

        List<List<String>> data = vehicleService.getAllVehiclesForReport()
                .stream()
                .map(vehicle -> List.of(
                        String.valueOf(vehicle.getPlateNumber()),
                        String.valueOf(vehicle.getModelName()),
                        String.valueOf(vehicle.getOwner().getFirstName()),
                        String.valueOf(vehicle.getOwner().getLastName()),
                        String.valueOf(vehicle.getChassisNumber())
                ))
                .toList();


        byte[] excelContent = excelService.generateExcelTransactions(headers, data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vehicles-report.xlsx")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(excelContent);
    }
}
