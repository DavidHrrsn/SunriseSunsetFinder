package sunrisesunsetupdated;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SunriseSunsetResult.java
 *
 * Stores the results of a sunrise and sunset calculation, including
 * the location coordinates, requested date, sunrise time, sunset time,
 * and total day length. The class also provides methods to format
 * the calculated values for display within the application.
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
public class SunriseSunsetResult {

    /**
     * Latitude used for the sunrise/sunset calculation.
     */
    private final double latitude;

    /**
     * Longitude used for the sunrise/sunset calculation.
     */
    private final double longitude;

    /**
     * Date for which sunrise and sunset information was calculated.
     */
    private final LocalDate date;

    /**
     * Calculated local sunrise time.
     */
    private final ZonedDateTime sunrise;

    /**
     * Calculated local sunset time.
     */
    private final ZonedDateTime sunset;

    /**
     * Length of daylight in seconds.
     */
    private final String dayLength;

    /**
     * Constructs a SunriseSunsetResult object containing calculation results.
     *
     * @param latitude Latitude used in the calculation.
     * @param longitude Longitude used in the calculation.
     * @param date Date for which the calculation was performed.
     * @param sunrise Calculated local sunrise time.
     * @param sunset Calculated local sunset time.
     * @param dayLength Length of daylight in seconds.
     */
    public SunriseSunsetResult(
            double latitude,
            double longitude,
            LocalDate date,
            ZonedDateTime sunrise,
            ZonedDateTime sunset,
            String dayLength
    ) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.dayLength = dayLength;
    }

    /**
     * Returns the sunrise time formatted for user display.
     *
     * @return Sunrise time in hh:mm a z format.
     */
    public String getFormattedSunrise() {

        return sunrise.format(
                DateTimeFormatter.ofPattern(
                        "hh:mm a z"
                )
        );
    }

    /**
     * Returns the sunset time formatted for user display.
     *
     * @return Sunset time in hh:mm a z format.
     */
    public String getFormattedSunset() {

        return sunset.format(
                DateTimeFormatter.ofPattern(
                        "hh:mm a z"
                )
        );
    }

    /**
     * Converts the stored daylight duration from seconds into
     * a human-readable HH:MM:SS format.
     *
     * @return Formatted day length string.
     */
    public String getFormattedDayLength() {

        int totalSeconds =
                Integer.parseInt(dayLength);

        int hours =
                totalSeconds / 3600;

        int minutes =
                (totalSeconds % 3600) / 60;

        int seconds =
                totalSeconds % 60;

        return String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
        );
    }

    /**
     * Returns a formatted summary of the sunrise and sunset results.
     *
     * @return Multi-line string containing calculation details.
     */
    @Override
    public String toString() {

        return "Date: " + date +
                "\nLatitude: " + latitude +
                "\nLongitude: " + longitude +
                "\nSunrise: " +
                getFormattedSunrise() +
                "\nSunset: " +
                getFormattedSunset() +
                "\nDay Length: " +
                getFormattedDayLength();
    }
}
