package sunrisesunsetupdated;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TimeZoneService.java
 *
 * Retrieves timezone information for a geographic location using
 * latitude and longitude coordinates. The service communicates with
 * the TimeAPI.io timezone API and returns a Java ZoneId object that
 * can be used to convert calculated sunrise and sunset times into
 * the correct local timezone.
 *
 * Accurate timezone identification is essential to ensure that
 * sunrise and sunset calculations are displayed correctly for
 * locations around the world.
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
public class TimeZoneService {

    /**
     * Base URL for the TimeAPI.io timezone lookup service.
     */
    private static final String API_URL =
            "https://timeapi.io/api/TimeZone/coordinate";

    /**
     * HTTP client used to communicate with the timezone API.
     */
    private final HttpClient client;

    /**
     * Constructs a TimeZoneService object and initializes
     * the HTTP client.
     */
    public TimeZoneService() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Determines the timezone associated with the specified
     * geographic coordinates.
     *
     * The method sends latitude and longitude values to the
     * timezone API and returns the corresponding Java ZoneId.
     *
     * @param latitude Geographic latitude of the location.
     * @param longitude Geographic longitude of the location.
     * @return ZoneId representing the location's timezone.
     * @throws IOException If a network communication error occurs.
     * @throws InterruptedException If the request is interrupted.
     */
    public ZoneId getTimeZone(double latitude, double longitude)
            throws IOException, InterruptedException {

        // Build the API request URL using the supplied coordinates.
        String url = API_URL
                + "?latitude=" + latitude
                + "&longitude=" + longitude;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Send the request and retrieve the API response.
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify that the request completed successfully.
        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Timezone API request failed with status code: "
                            + response.statusCode()
            );
        }

        String json = response.body();

        // Extract the timezone identifier from the JSON response.
        String timeZone = extractValue(json, "timeZone");

        return ZoneId.of(timeZone);
    }

    /**
     * Extracts a specified value from a JSON response string
     * using a regular expression pattern.
     *
     * @param json The JSON response returned by the API.
     * @param key The key whose value should be extracted.
     * @return The value associated with the specified key.
     * @throws RuntimeException If the key cannot be found.
     */
    private String extractValue(String json, String key) {

        Pattern pattern = Pattern.compile(
                "\"" + key + "\"\\s*:\\s*\"([^\"]*)\""
        );

        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new RuntimeException(
                "Could not find value for key: " + key
        );
    }
}
