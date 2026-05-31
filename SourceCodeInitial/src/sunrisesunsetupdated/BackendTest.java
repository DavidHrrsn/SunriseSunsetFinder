package sunrisesunsetupdated;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Scanner;

public class BackendTest {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        try {
            GeocodingService geocodingService = new GeocodingService();
            TimeZoneService timeZoneService = new TimeZoneService();
            SolarCalculator solarCalculator = new SolarCalculator();

            System.out.println("Choose location input type:");
            System.out.println("1. Street address, city, state, and country");
            System.out.println("2. City, state, and country only");
            System.out.print("Choice: ");

            int locationChoice = Integer.parseInt(input.nextLine());

            String address;

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

            LocationResult location =
                    geocodingService.getLocationFromAddress(address);

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            ZoneId zoneId =
                    timeZoneService.getTimeZone(latitude, longitude);

            SunriseSunsetResult result =
                    solarCalculator.calculateSunriseSunset(
                            latitude,
                            longitude,
                            LocalDate.now(),
                            zoneId
                    );

            System.out.println("\nLocation Found:");
            System.out.println(location);

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
            System.out.println("Error: " + e.getMessage());
        }

        input.close();
    }
}