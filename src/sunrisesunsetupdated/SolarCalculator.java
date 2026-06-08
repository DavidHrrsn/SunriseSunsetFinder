package sunrisesunsetupdated;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * SolarCalculator.java
 *
 * Performs local sunrise and sunset calculations using geographic
 * coordinates, a selected date, and the location's timezone. This class
 * uses a standard solar position calculation to estimate sunrise and
 * sunset times without relying on a sunrise/sunset API.
 *
 * The calculated UTC times are converted into the correct local timezone
 * before being returned to the user.
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
public class SolarCalculator {

    /**
     * Official zenith value commonly used for sunrise and sunset calculations.
     * The value includes atmospheric refraction and the apparent radius of the sun.
     */
    private static final double ZENITH = 90.833;

    /**
     * Calculates sunrise time, sunset time, and total daylight duration
     * for a specific location and date.
     *
     * @param latitude Geographic latitude of the location.
     * @param longitude Geographic longitude of the location.
     * @param date Date for which sunrise and sunset should be calculated.
     * @param zoneId Timezone used to display the final local times.
     * @return A SunriseSunsetResult object containing calculated solar data.
     */
    public SunriseSunsetResult calculateSunriseSunset(
            double latitude,
            double longitude,
            LocalDate date,
            ZoneId zoneId
    ) {

        // Calculate sunrise and sunset separately because the hour angle differs.
        ZonedDateTime sunrise =
                calculateTime(latitude, longitude, date, zoneId, true);

        ZonedDateTime sunset =
                calculateTime(latitude, longitude, date, zoneId, false);

        // Determine daylight duration in seconds.
        long dayLengthSeconds =
                Duration.between(sunrise, sunset).getSeconds();

        // Correct for cases where sunset is calculated after midnight boundary.
        if (dayLengthSeconds < 0) {
            dayLengthSeconds += 24 * 60 * 60;
        }

        return new SunriseSunsetResult(
                latitude,
                longitude,
                date,
                sunrise,
                sunset,
                String.valueOf(dayLengthSeconds)
        );
    }

    /**
     * Calculates either sunrise or sunset time for the provided location.
     *
     * The method follows a solar calculation process that estimates the
     * sun's position for the date, determines the hour angle, converts the
     * result to UTC, and then converts UTC into the requested local timezone.
     *
     * @param latitude Geographic latitude of the location.
     * @param longitude Geographic longitude of the location.
     * @param date Date for the calculation.
     * @param zoneId Timezone used for the final displayed result.
     * @param isSunrise True to calculate sunrise; false to calculate sunset.
     * @return ZonedDateTime containing the calculated local sunrise or sunset.
     */
    private ZonedDateTime calculateTime(
            double latitude,
            double longitude,
            LocalDate date,
            ZoneId zoneId,
            boolean isSunrise
    ) {

        // Determine the numeric day of the year.
        int dayOfYear = date.getDayOfYear();

        // Convert longitude into an approximate hour value.
        double lngHour = longitude / 15.0;

        double t;

        // Estimate the approximate time of sunrise or sunset.
        if (isSunrise) {
            t = dayOfYear + ((6.0 - lngHour) / 24.0);
        } else {
            t = dayOfYear + ((18.0 - lngHour) / 24.0);
        }

        // Calculate the sun's mean anomaly.
        double meanAnomaly =
                (0.9856 * t) - 3.289;

        // Calculate the sun's true longitude.
        double trueLongitude =
                meanAnomaly
                        + (1.916 * Math.sin(Math.toRadians(meanAnomaly)))
                        + (0.020 * Math.sin(Math.toRadians(2 * meanAnomaly)))
                        + 282.634;

        trueLongitude =
                normalizeDegrees(trueLongitude);

        // Calculate the sun's right ascension.
        double rightAscension =
                Math.toDegrees(
                        Math.atan(
                                0.91764 *
                                        Math.tan(
                                                Math.toRadians(trueLongitude)
                                        )
                        )
                );

        rightAscension =
                normalizeDegrees(rightAscension);

        // Adjust right ascension so it falls in the same quadrant as true longitude.
        double longitudeQuadrant =
                Math.floor(trueLongitude / 90.0) * 90.0;

        double rightAscensionQuadrant =
                Math.floor(rightAscension / 90.0) * 90.0;

        rightAscension =
                rightAscension
                        + (longitudeQuadrant - rightAscensionQuadrant);

        // Convert right ascension from degrees to hours.
        rightAscension =
                rightAscension / 15.0;

        // Calculate the sun's declination.
        double sinDeclination =
                0.39782 *
                        Math.sin(Math.toRadians(trueLongitude));

        double cosDeclination =
                Math.cos(Math.asin(sinDeclination));

        // Calculate the local hour angle.
        double cosHourAngle =
                (Math.cos(Math.toRadians(ZENITH))
                        - (sinDeclination *
                        Math.sin(Math.toRadians(latitude))))
                        / (cosDeclination *
                        Math.cos(Math.toRadians(latitude)));

        // Handle polar or extreme latitude cases where the sun may not rise or set.
        if (cosHourAngle > 1) {
            throw new RuntimeException(
                    "The sun never rises on this date at this location."
            );
        }

        if (cosHourAngle < -1) {
            throw new RuntimeException(
                    "The sun never sets on this date at this location."
            );
        }

        double hourAngle;

        // Sunrise and sunset use opposite sides of the hour angle.
        if (isSunrise) {
            hourAngle =
                    360.0 -
                            Math.toDegrees(
                                    Math.acos(cosHourAngle)
                            );
        } else {
            hourAngle =
                    Math.toDegrees(
                            Math.acos(cosHourAngle)
                    );
        }

        // Convert hour angle from degrees to hours.
        hourAngle =
                hourAngle / 15.0;

        // Calculate local mean time.
        double localMeanTime =
                hourAngle
                        + rightAscension
                        - (0.06571 * t)
                        - 6.622;

        // Convert local mean time to UTC.
        double utcTime =
                localMeanTime - lngHour;

        utcTime =
                normalizeHours(utcTime);

        // Separate decimal UTC time into hour, minute, and second values.
        int hour =
                (int) utcTime;

        int minute =
                (int) ((utcTime - hour) * 60);

        int second =
                (int) Math.round(
                        (((utcTime - hour) * 60) - minute) * 60
                );

        // Correct rounding overflow for seconds, minutes, and hours.
        if (second == 60) {
            second = 0;
            minute++;
        }

        if (minute == 60) {
            minute = 0;
            hour++;
        }

        if (hour == 24) {
            hour = 0;
        }

        LocalTime utcLocalTime =
                LocalTime.of(hour, minute, second);

        // Create a UTC date/time first, then convert it into the target timezone.
        ZonedDateTime utcDateTime =
                ZonedDateTime.of(
                        date,
                        utcLocalTime,
                        ZoneId.of("UTC")
                );

        return utcDateTime.withZoneSameInstant(zoneId);
    }

    /**
     * Normalizes an angle so that it falls within the range 0 to 360 degrees.
     *
     * @param degrees Angle value to normalize.
     * @return Normalized degree value.
     */
    private double normalizeDegrees(double degrees) {

        degrees = degrees % 360.0;

        if (degrees < 0) {
            degrees += 360.0;
        }

        return degrees;
    }

    /**
     * Normalizes a time value so that it falls within the range 0 to 24 hours.
     *
     * @param hours Hour value to normalize.
     * @return Normalized hour value.
     */
    private double normalizeHours(double hours) {

        hours = hours % 24.0;

        if (hours < 0) {
            hours += 24.0;
        }

        return hours;
    }
}
