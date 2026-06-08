package sunrisesunsetupdated;

/**
 * LocationResult.java
 *
 * Stores location information returned by the geocoding service,
 * including latitude, longitude, and a formatted display name.
 *
 * This class acts as a simple data container that transfers
 * geographic location information between application components.
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
public class LocationResult {

    /**
     * Latitude returned by the geocoding service.
     */
    private final double latitude;

    /**
     * Longitude returned by the geocoding service.
     */
    private final double longitude;

    /**
     * Formatted location name returned by the geocoding service.
     */
    private final String displayName;

    /**
     * Constructs a LocationResult object containing location data.
     *
     * @param latitude Geographic latitude of the location.
     * @param longitude Geographic longitude of the location.
     * @param displayName Formatted location name.
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

    /**
     * Returns the latitude associated with the location.
     *
     * @return Geographic latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude associated with the location.
     *
     * @return Geographic longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the formatted display name of the location.
     *
     * @return Formatted location name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns a formatted string representation of the location.
     *
     * @return Location information including display name,
     *         latitude, and longitude.
     */
    @Override
    public String toString() {

        return displayName +
                "\nLatitude: " + latitude +
                "\nLongitude: " + longitude;
    }
}
