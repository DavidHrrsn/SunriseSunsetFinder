package sunrisesunsetupdated;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SolarCalculator {

    private static final double ZENITH = 90.833;

    public SunriseSunsetResult calculateSunriseSunset(
            double latitude,
            double longitude,
            LocalDate date,
            ZoneId zoneId
    ) {

        ZonedDateTime sunrise =
                calculateTime(latitude, longitude, date, zoneId, true);

        ZonedDateTime sunset =
                calculateTime(latitude, longitude, date, zoneId, false);

        long dayLengthSeconds =
                Duration.between(sunrise, sunset).getSeconds();

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

    private ZonedDateTime calculateTime(
            double latitude,
            double longitude,
            LocalDate date,
            ZoneId zoneId,
            boolean isSunrise
    ) {

        int dayOfYear = date.getDayOfYear();

        double lngHour = longitude / 15.0;

        double t;

        if (isSunrise) {
            t = dayOfYear + ((6.0 - lngHour) / 24.0);
        } else {
            t = dayOfYear + ((18.0 - lngHour) / 24.0);
        }

        double meanAnomaly =
                (0.9856 * t) - 3.289;

        double trueLongitude =
                meanAnomaly
                        + (1.916 * Math.sin(Math.toRadians(meanAnomaly)))
                        + (0.020 * Math.sin(Math.toRadians(2 * meanAnomaly)))
                        + 282.634;

        trueLongitude =
                normalizeDegrees(trueLongitude);

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

        double longitudeQuadrant =
                Math.floor(trueLongitude / 90.0) * 90.0;

        double rightAscensionQuadrant =
                Math.floor(rightAscension / 90.0) * 90.0;

        rightAscension =
                rightAscension
                        + (longitudeQuadrant - rightAscensionQuadrant);

        rightAscension =
                rightAscension / 15.0;

        double sinDeclination =
                0.39782 *
                        Math.sin(Math.toRadians(trueLongitude));

        double cosDeclination =
                Math.cos(Math.asin(sinDeclination));

        double cosHourAngle =
                (Math.cos(Math.toRadians(ZENITH))
                        - (sinDeclination *
                        Math.sin(Math.toRadians(latitude))))
                        / (cosDeclination *
                        Math.cos(Math.toRadians(latitude)));

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

        hourAngle =
                hourAngle / 15.0;

        double localMeanTime =
                hourAngle
                        + rightAscension
                        - (0.06571 * t)
                        - 6.622;

        double utcTime =
                localMeanTime - lngHour;

        utcTime =
                normalizeHours(utcTime);

        int hour =
                (int) utcTime;

        int minute =
                (int) ((utcTime - hour) * 60);

        int second =
                (int) Math.round(
                        (((utcTime - hour) * 60) - minute) * 60
                );

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

        ZonedDateTime utcDateTime =
                ZonedDateTime.of(
                        date,
                        utcLocalTime,
                        ZoneId.of("UTC")
                );

        return utcDateTime.withZoneSameInstant(zoneId);
    }

    private double normalizeDegrees(double degrees) {

        degrees = degrees % 360.0;

        if (degrees < 0) {
            degrees += 360.0;
        }

        return degrees;
    }

    private double normalizeHours(double hours) {

        hours = hours % 24.0;

        if (hours < 0) {
            hours += 24.0;
        }

        return hours;
    }
}