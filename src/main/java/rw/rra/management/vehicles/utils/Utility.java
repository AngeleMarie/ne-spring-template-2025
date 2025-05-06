package rw.rra.management.vehicles.utils;

import rw.rra.management.vehicles.plates.PlateRepository;

import java.util.Random;

public class Utility {

 private  PlateRepository plateRepository;

    public String generateUniquePlateNumber() {
        String plate;
        do {
            plate = "RA" + randomLetter() + randomDigits(3) + randomLetter();
        } while (plateRepository.findByPlateNumber(plate).isPresent());
        return plate;
    }

    private String randomLetter() {
        return String.valueOf((char) ('A' + new Random().nextInt(26)));
    }

    private String randomDigits(int length) {
        Random rand = new Random();
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < length; i++) {
            digits.append(rand.nextInt(10));
        }
        return digits.toString();
    }

}
