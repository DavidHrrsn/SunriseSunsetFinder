package sunrisesunsetupdated;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/*
 * SunriseSunsetResult
 * -------------------
 * Stores and formats sunrise/sunset information.
 */
public class SunriseSunsetResult {

    // Latitude used in request
    private final double latitude;

    // Longitude used in request
    private final double longitude;

    // Requested date
    private final LocalDate date;

    // Local sunrise time
    private final ZonedDateTime sunrise;

    // Local sunset time
    private final ZonedDateTime sunset;

    // Day length in seconds
    private final String dayLength;

    /*
     * Constructor
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
     * Returns formatted sunrise time.
     */
    public String getFormattedSunrise() {

        return sunrise.format(
                DateTimeFormatter.ofPattern(
                        "hh:mm a z"
                )
        );
    }

    /*
     * Returns formatted sunset time.
     */
    public String getFormattedSunset() {

        return sunset.format(
                DateTimeFormatter.ofPattern(
                        "hh:mm a z"
                )
        );
    }

    /*
     * Converts seconds into HH:MM:SS format.
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

    /*
     * Returns formatted result information.
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