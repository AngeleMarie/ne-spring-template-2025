package rw.rra.management.vehicles.utils;

import org.springframework.stereotype.Component;
import rw.rra.management.vehicles.plates.PlateRepository;

@Component
public class Utility {

    private final PlateRepository plateRepository;

    public Utility(PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }

    public String generateUniquePlateNumber() {
        String plate;
        do {
            plate = "RA" + randomLetter() + randomDigits(3) + randomLetter();
        } while (plateRepository.findByPlateNumber(plate).isPresent());
        System.out.println("Generated unique plate number: " + plate); // Add this line
        return plate;
    }


    private String randomLetter() {
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F'};
        int index = (int) (Math.random() * letters.length);
        return String.valueOf(letters[index]);
    }


    private String randomDigits(int count) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < count; i++) {
            digits.append((int) (Math.random() * 10));
        }
        return digits.toString();
    }
}
