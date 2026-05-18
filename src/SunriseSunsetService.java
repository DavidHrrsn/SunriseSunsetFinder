import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * SunriseSunsetService
 * --------------------
 * This class handles communication with the Sunrise-Sunset API.
 * It sends requests using latitude, longitude, and date information,
 * then returns formatted sunrise and sunset results.
 */
public class SunriseSunsetService {

    // Base API URL used to retrieve sunrise and sunset data
    private static final String API_URL = "https://api.sunrise-sunset.org/json";

    // HTTP client used to send API requests
    private final HttpClient client;

    /*
     * Constructor
     * Creates a new HttpClient object.
     */
    public SunriseSunsetService() {
        this.client = HttpClient.newHttpClient();
    }

    /*
     * getSunriseSunset
     * -----------------
     * Sends a request to the API and retrieves sunrise/sunset data.
     *
     * Parameters:
     * latitude  - geographic latitude
     * longitude - geographic longitude
     * date      - date requested by the user
     * zoneId    - local timezone for conversion
     *
     * Returns:
     * A SunriseSunsetResult object containing formatted results.
     */
    public SunriseSunsetResult getSunriseSunset(
            double latitude,
            double longitude,
            LocalDate date,
            ZoneId zoneId
    ) throws IOException, InterruptedException {

        // Validate coordinate ranges before sending request
        validateCoordinates(latitude, longitude);

        // Build API request URL with query parameters
        String url = API_URL
                + "?lat=" + latitude
                + "&lng=" + longitude
                + "&date=" + encode(date.toString())
                + "&formatted=0";

        // Create HTTP GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Send request and store response
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify successful response code
        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "API request failed with status code: "
                            + response.statusCode()
            );
        }

        // Store JSON response body
        String json = response.body();

        // Check API status value
        String status = extractValue(json, "status");

        if (!"OK".equalsIgnoreCase(status)) {
            throw new RuntimeException(
                    "API returned an error: " + status
            );
        }

        // Extract sunrise, sunset, and day length values
        String sunriseUtc = extractValue(json, "sunrise");
        String sunsetUtc = extractValue(json, "sunset");
        String dayLength = extractValue(json, "day_length");

        // Convert UTC times into local timezone
        ZonedDateTime sunriseLocal =
                convertUtcToLocal(sunriseUtc, zoneId);

        ZonedDateTime sunsetLocal =
                convertUtcToLocal(sunsetUtc, zoneId);

        // Return completed result object
        return new SunriseSunsetResult(
                latitude,
                longitude,
                date,
                sunriseLocal,
                sunsetLocal,
                dayLength
        );
    }

    /*
     * validateCoordinates
     * -------------------
     * Ensures latitude and longitude are within valid ranges.
     */
    private void validateCoordinates(
            double latitude,
            double longitude
    ) {

        // Latitude must be between -90 and 90
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 and 90."
            );
        }

        // Longitude must be between -180 and 180
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 and 180."
            );
        }
    }

    /*
     * convertUtcToLocal
     * -----------------
     * Converts UTC API time into the user's local timezone.
     */
    private ZonedDateTime convertUtcToLocal(
            String utcTime,
            ZoneId zoneId
    ) {

        // Parse UTC timestamp into Instant object
        Instant instant = Instant.parse(utcTime);

        // Convert to local timezone
        return instant.atZone(zoneId);
    }

    /*
     * extractValue
     * ------------
     * Uses regular expressions to retrieve values from JSON.
     * Supports both string and numeric values.
     */
    private String extractValue(String json, String key) {

        // Pattern for quoted string values
        Pattern stringPattern = Pattern.compile(
                "\"" + key + "\"\\s*:\\s*\"([^\"]*)\""
        );

        Matcher stringMatcher =
                stringPattern.matcher(json);

        // Return string value if found
        if (stringMatcher.find()) {
            return stringMatcher.group(1);
        }

        // Pattern for numeric values
        Pattern numberPattern = Pattern.compile(
                "\"" + key + "\"\\s*:\\s*(\\d+)"
        );

        Matcher numberMatcher =
                numberPattern.matcher(json);

        // Return numeric value if found
        if (numberMatcher.find()) {
            return numberMatcher.group(1);
        }

        // Throw exception if key cannot be found
        throw new RuntimeException(
                "Could not find value for key: " + key
        );
    }

    /*
     * encode
     * ------
     * Encodes text for safe use inside URLs.
     */
    private String encode(String value) {

        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8
        );
    }
}