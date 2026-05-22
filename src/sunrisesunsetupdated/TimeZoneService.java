package sunrisesunsetupdated;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeZoneService {

    private static final String API_URL =
            "https://timeapi.io/api/TimeZone/coordinate";

    private final HttpClient client;

    public TimeZoneService() {
        this.client = HttpClient.newHttpClient();
    }

    public ZoneId getTimeZone(double latitude, double longitude)
            throws IOException, InterruptedException {

        String url = API_URL
                + "?latitude=" + latitude
                + "&longitude=" + longitude;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Timezone API request failed with status code: "
                            + response.statusCode()
            );
        }

        String json = response.body();

        String timeZone = extractValue(json, "timeZone");

        return ZoneId.of(timeZone);
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
}