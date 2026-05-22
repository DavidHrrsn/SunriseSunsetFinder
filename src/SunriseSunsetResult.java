import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/*
 * SunriseSunsetResult
 * -------------------
 * This class stores and formats the sunrise and sunset
 * information returned from the API.
 */
public class SunriseSunsetResult {

    // Geographic latitude of the requested location
    private final double latitude;

    // Geographic longitude of the requested location
    private final double longitude;

    // Date requested by the user
    private final LocalDate date;

    // Local sunrise time
    private final ZonedDateTime sunrise;

    // Local sunset time
    private final ZonedDateTime sunset;

    // Day length returned from API in seconds
    private final String dayLength;

    /*
     * Constructor
     * Initializes all result fields.
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

    /*
     * getFormattedSunrise
     * -------------------
     * Formats sunrise time into 12-hour AM/PM format.
     */
    public String getFormattedSunrise() {

        return sunrise.format(
                DateTimeFormatter.ofPattern("hh:mm a z")
        );
    }

    /*
     * getFormattedSunset
     * ------------------
     * Formats sunset time into 12-hour AM/PM format.
     */
    public String getFormattedSunset() {

        return sunset.format(
                DateTimeFormatter.ofPattern("hh:mm a z")
        );
    }

    /*
     * getFormattedDayLength
     * ---------------------
     * Converts total seconds into HH:MM:SS format.
     */
    public String getFormattedDayLength() {

        // Convert day length string into integer seconds
        int totalSeconds = Integer.parseInt(dayLength);

        // Calculate hours
        int hours = totalSeconds / 3600;

        // Calculate remaining minutes
        int minutes = (totalSeconds % 3600) / 60;

        // Calculate remaining seconds
        int seconds = totalSeconds % 60;

        // Return formatted time string
        return String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
        );
    }

    /*
     * toString
     * --------
     * Returns a formatted summary of all sunrise/sunset data.
     */
    @Override
    public String toString() {

        return "Date: " + date +
                "\nLatitude: " + latitude +
                "\nLongitude: " + longitude +
                "\nSunrise: " + getFormattedSunrise() +
                "\nSunset: " + getFormattedSunset() +
                "\nDay Length: " + getFormattedDayLength();
    }
}