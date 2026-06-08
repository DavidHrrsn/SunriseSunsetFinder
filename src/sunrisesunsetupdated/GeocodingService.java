package sunrisesunsetupdated;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GeocodingService.java
 *
 * Provides geocoding functionality by converting user-entered location
 * information into geographic coordinates using the OpenStreetMap
 * Nominatim API. The service retrieves latitude, longitude, and a
 * formatted display name for the requested location.
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
public class GeocodingService {

    /**
     * Base URL for the OpenStreetMap Nominatim geocoding service.
     */
    private static final String API_URL =
            "https://nominatim.openstreetmap.org/search";

    /**
     * HTTP client used to send requests to the geocoding API.
     */
    private final HttpClient client;

    /**
     * Constructs a new GeocodingService and initializes the HTTP client.
     */
    public GeocodingService() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Converts a user-provided address into geographic coordinates.
     *
     * The method sends a request to the OpenStreetMap Nominatim API and
     * returns the latitude, longitude, and formatted location name for
     * the first matching result.
     *
     * @param address The address or location entered by the user.
     * @return A LocationResult object containing coordinates and location name.
     * @throws IOException If a network communication error occurs.
     * @throws InterruptedException If the HTTP request is interrupted.
     * @throws IllegalArgumentException If the address is null or blank.
     */
    public LocationResult getLocationFromAddress(String address)
            throws IOException, InterruptedException {

        // Validate user input before making an API request.
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be blank.");
        }

        // Build the API request URL with encoded user input.
        String url = API_URL
                + "?q=" + encode(address)
                + "&format=json"
                + "&limit=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "SunriseSunsetFinder/1.0")
                .GET()
                .build();

        // Send the request and retrieve the response.
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify that the request completed successfully.
        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Geocoding request failed with status code: "
                            + response.statusCode()
            );
        }

        String json = response.body();

        // If no matching location was found, notify the user.
        if (json.equals("[]")) {
            throw new RuntimeException(
                    "No location found for the address entered."
            );
        }

        // Extract coordinate and display information from the JSON response.
        String latText = extractValue(json, "lat");
        String lonText = extractValue(json, "lon");
        String displayName = extractValue(json, "display_name");

        double latitude = Double.parseDouble(latText);
        double longitude = Double.parseDouble(lonText);

        return new LocationResult(latitude, longitude, displayName);
    }

    /**
     * Extracts a specified value from a JSON response string using
     * a regular expression pattern.
     *
     * @param json The JSON response received from the API.
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

    /**
     * Encodes text for safe inclusion in a URL query string.
     *
     * @param value The text to encode.
     * @return URL-encoded text using UTF-8 encoding.
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
