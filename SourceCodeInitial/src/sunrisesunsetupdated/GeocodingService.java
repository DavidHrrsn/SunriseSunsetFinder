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

public class GeocodingService {

    private static final String API_URL =
            "https://nominatim.openstreetmap.org/search";

    private final HttpClient client;

    public GeocodingService() {
        this.client = HttpClient.newHttpClient();
    }

    public LocationResult getLocationFromAddress(String address)
            throws IOException, InterruptedException {

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be blank.");
        }

        String url = API_URL
                + "?q=" + encode(address)
                + "&format=json"
                + "&limit=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "SunriseSunsetFinder/1.0")
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Geocoding request failed with status code: "
                            + response.statusCode()
            );
        }

        String json = response.body();

        if (json.equals("[]")) {
            throw new RuntimeException(
                    "No location found for the address entered."
            );
        }

        String latText = extractValue(json, "lat");
        String lonText = extractValue(json, "lon");
        String displayName = extractValue(json, "display_name");

        double latitude = Double.parseDouble(latText);
        double longitude = Double.parseDouble(lonText);

        return new LocationResult(latitude, longitude, displayName);
    }

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

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}