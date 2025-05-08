package rw.rra.management.vehicles.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rw.rra.management.vehicles.email.EmailService;
import rw.rra.management.vehicles.vehicles.Vehicle;
import rw.rra.management.vehicles.vehicles.VehicleRepository;

import java.time.LocalDateTime;
import java.util.List; // Import List

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleInspectionJob {

    private final VehicleRepository vehicleRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 */5 * * * *")
    public void checkVehicleInspectionStatus() {
        log.info("VehicleInspectionJob: Cron job started at {}", LocalDateTime.now());
        try {
            List<Vehicle> vehicles = vehicleRepository.findAll(); // Use List<Vehicle>
            log.info("VehicleInspectionJob: Found {} vehicles to check.", vehicles.size());

            if (vehicles.isEmpty()) {
                log.info("VehicleInspectionJob: No vehicles found to process.");
                return;
            }

            for (Vehicle vehicle : vehicles) {
                try {
                    log.debug("VehicleInspectionJob: Processing vehicle with ID: {} and Plate: {}",
                            vehicle.getId(), // Assuming Vehicle has an getId() method for logging
                            (vehicle.getPlateNumber() != null ? vehicle.getPlateNumber().getPlateNumber() : "N/A"));

                    if (vehicle.getPlateNumber() != null && vehicle.getOwner() != null && vehicle.getOwner().getEmail() != null) {
                        LocalDateTime lastInspection = vehicle.getLastInspectionTime();
                        log.debug("VehicleInspectionJob: Vehicle ID: {}, Plate: {}. Last inspection time: {}",
                                vehicle.getId(), vehicle.getPlateNumber().getPlateNumber(), lastInspection);

                        boolean needsInspectionNotification = false;
                        if (lastInspection == null) {
                            needsInspectionNotification = true;
                            log.debug("VehicleInspectionJob: Vehicle ID: {}, Plate: {}. Last inspection is null. Needs notification.", vehicle.getId(), vehicle.getPlateNumber().getPlateNumber());
                        } else {
                            LocalDateTime fiveMinutesAfterLastInspection = lastInspection.plusMinutes(5);
                            LocalDateTime currentTime = LocalDateTime.now();
                            if (fiveMinutesAfterLastInspection.isBefore(currentTime)) {
                                needsInspectionNotification = true;
                                log.debug("VehicleInspectionJob: Vehicle ID: {}, Plate: {}. Condition met: {} (5 mins after last inspection) is before {} (current time). Needs notification.",
                                        vehicle.getId(), vehicle.getPlateNumber().getPlateNumber(), fiveMinutesAfterLastInspection, currentTime);
                            } else {
                                log.debug("VehicleInspectionJob: Vehicle ID: {}, Plate: {}. Condition NOT met: {} (5 mins after last inspection) is NOT before {} (current time). No notification.",
                                        vehicle.getId(), vehicle.getPlateNumber().getPlateNumber(), fiveMinutesAfterLastInspection, currentTime);
                            }
                        }

                        if (needsInspectionNotification) {
                            String ownerEmail = vehicle.getOwner().getEmail();
                            // Basic email validation (optional, but good practice)
                            if (ownerEmail == null || ownerEmail.trim().isEmpty()) {
                                log.warn("VehicleInspectionJob: Vehicle ID: {}, Plate: {}. Owner email is null or empty. Skipping email.", vehicle.getId(), vehicle.getPlateNumber().getPlateNumber());
                                continue; // Skip to next vehicle
                            }

                            String ownerName = (vehicle.getOwner().getFirstName() != null ? vehicle.getOwner().getFirstName() : "") +
                                    " " +
                                    (vehicle.getOwner().getLastName() != null ? vehicle.getOwner().getLastName() : "");
                            ownerName = ownerName.trim();
                            String plateNumber = vehicle.getPlateNumber().getPlateNumber();

                            log.info("VehicleInspectionJob: Attempting to send inspection notification for Vehicle ID: {}, Plate: {} to owner: {} ({})",
                                    vehicle.getId(), plateNumber, ownerName, ownerEmail);

                            emailService.sendPostInspectionNotification(ownerEmail, ownerName, plateNumber);
                            log.info("VehicleInspectionJob: Email notification call initiated for Vehicle ID: {}, Plate: {}.", vehicle.getId(), plateNumber);

                            vehicle.setLastInspectionTime(LocalDateTime.now());
                            vehicleRepository.save(vehicle);
                            log.info("VehicleInspectionJob: Updated last inspection time for Vehicle ID: {}, Plate: {}.", vehicle.getId(), plateNumber);
                        }
                    } else {
                        log.warn("VehicleInspectionJob: Skipping vehicle with ID: {} due to missing PlateNumber, Owner, or Owner Email. PlateNumber present: {}, Owner present: {}, Owner Email present: {}",
                                vehicle.getId(),
                                vehicle.getPlateNumber() != null,
                                vehicle.getOwner() != null,
                                (vehicle.getOwner() != null && vehicle.getOwner().getEmail() != null));
                    }
                } catch (Exception e) {
                    // Catching general Exception to ensure the loop continues for other vehicles
                    log.error("VehicleInspectionJob: Error processing vehicle with ID: {} (Plate: {}): {}",
                            (vehicle != null ? vehicle.getId() : "N/A"),
                            (vehicle != null && vehicle.getPlateNumber() != null ? vehicle.getPlateNumber().getPlateNumber() : "N/A"),
                            e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            // Catching exceptions from findAll or other broader issues
            log.error("VehicleInspectionJob: A critical error occurred during the job execution: {}", e.getMessage(), e);
        }
        log.info("VehicleInspectionJob: Cron job finished at {}", LocalDateTime.now());
    }
}