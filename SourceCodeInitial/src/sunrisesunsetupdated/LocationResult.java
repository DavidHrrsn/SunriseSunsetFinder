package sunrisesunsetupdated;
/*
 * LocationResult
 * --------------
 * Stores the latitude, longitude, and display name returned
 * from the geocoding service.
 */
public class LocationResult {

    // Latitude returned from geocoding API
    private final double latitude;

    // Longitude returned from geocoding API
    private final double longitude;

    // Full formatted address/location name
    private final String displayName;

    /*
     * Constructor
     */
    public LocationResult(
            double latitude,
            double longitude,
            String displayName
    ) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.displayName = displayName;
    }

    /*
     * Returns latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /*
     * Returns longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /*
     * Returns display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /*
     * Returns formatted location information
     */
    @Override
    public String toString() {

        return displayName +
                "\nLatitude: " + latitude +
                "\nLongitude: " + longitude;
    }
}