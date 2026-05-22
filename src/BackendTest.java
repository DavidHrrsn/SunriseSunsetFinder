import java.time.LocalDate;
import java.time.ZoneId;

/*
 * BackendTest
 * -----------
 * This class is used to test the backend service without the GUI.
 * It creates a sample request and prints the sunrise/sunset result.
 */
public class BackendTest {

    public static void main(String[] args) {

        try {
            // Create service object to call the sunrise/sunset API
            SunriseSunsetService service = new SunriseSunsetService();

            // Request sunrise/sunset data for Baltimore, Maryland
            SunriseSunsetResult result = service.getSunriseSunset(
                    39.2904,                         // Latitude
                    -76.6122,                        // Longitude
                    LocalDate.now(),       // Requested date
                    ZoneId.of("America/New_York")   // Local timezone
            );

            // Print formatted result to the console
            System.out.println(result);

        } catch (Exception e) {

            // Print error message if something goes wrong
            System.out.println("Error: " + e.getMessage());
        }
    }
}