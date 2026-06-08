package sunrisesunsetupdated;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Scanner;

/**
 * BackendTest.java
 *
 * Console-based test application used to verify the functionality of
 * the backend components for the Sunrise/Sunset Time Finder project.
 * This class allows developers to enter location information, retrieve
 * coordinates through geocoding, determine the appropriate timezone,
 * and calculate sunrise and sunset times.
 *
 * This class was primarily used during development and testing to
 * validate backend services before integration with the JavaFX GUI.
 *
 * CMSC 495 Capstone Project
 * Project: Sunrise/Sunset Time Finder
 *
 * @author David Harrison
 * @author Samuel Garmoe
 * @author Apurva Dave
 * @author Jeremy Ross
 * @author Jeremy Briggs
 * @version 1.0
 */
public class BackendTest {

    /**
     * Main method used to test backend functionality through a
     * command-line interface.
     *
     * The user enters location information, which is converted into
     * coordinates using the geocoding service. The timezone is then
     * determined from those coordinates, and sunrise/sunset times are
     * calculated and displayed.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        try {
            // Initialize backend service objects.
            GeocodingService geocodingService = new GeocodingService();
            TimeZoneService timeZoneService = new TimeZoneService();
            SolarCalculator solarCalculator = new SolarCalculator();

            System.out.println("Choose location input type:");
            System.out.println("1. Street address, city, state, and country");
            System.out.println("2. City, state, and country only");
            System.out.print("Choice: ");

            int locationChoice = Integer.parseInt(input.nextLine());

            String address;

            // Build the location string based on the user's selection.
            if (locationChoice == 1) {

                System.out.print("Street Address: ");
                String street = input.nextLine();

                System.out.print("City: ");
                String city = input.nextLine();

                System.out.print("State: ");
                String state = input.nextLine();

                System.out.print("Country: ");
                String country = input.nextLine();

                address = street + ", " + city + ", " + state + ", " + country;

            } else if (locationChoice == 2) {

                System.out.print("City: ");
                String city = input.nextLine();

                System.out.print("State: ");
                String state = input.nextLine();

                System.out.print("Country: ");
                String country = input.nextLine();

                address = city + ", " + state + ", " + country;

            } else {
                throw new IllegalArgumentException("Invalid location input type.");
            }

            // Convert the address into geographic coordinates.
            LocationResult location =
                    geocodingService.getLocationFromAddress(address);

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Determine the timezone associated with the coordinates.
            ZoneId zoneId =
                    timeZoneService.getTimeZone(latitude, longitude);

            // Calculate sunrise and sunset times for the current date.
            SunriseSunsetResult result =
                    solarCalculator.calculateSunriseSunset(
                            latitude,
                            longitude,
                            LocalDate.now(),
                            zoneId
                    );

            System.out.println("\nLocation Found:");
            System.out.println(location);

            // Inform the user when city-level coordinates are used.
            if (locationChoice == 2) {
                System.out.println(
                        "\nNote: City-only results are based on the general city coordinates."
                );
            }

            System.out.println("\nTimezone Found From Coordinates:");
            System.out.println(zoneId);

            System.out.println("\nSunrise/Sunset Information:");
            System.out.println(result);

        } catch (Exception e) {

            // Display any processing errors to assist with debugging.
            System.out.println("Error: " + e.getMessage());
        }

        // Release scanner resources.
        input.close();
    }
}
