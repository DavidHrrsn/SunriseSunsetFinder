package sunrisesunsetupdated;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * SunriseSunsetApp.java
 *
 * Main JavaFX application for the Sunrise/Sunset Time Finder project.
 * This class provides the graphical user interface used to collect
 * location and date information from the user, invoke backend services,
 * and display calculated sunrise and sunset results.
 *
 * The application integrates geocoding, timezone lookup, and local
 * solar calculations to provide accurate sunrise and sunset times
 * for locations around the world.
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
public class SunriseSunsetApp extends Application {

    private VBox resultsSection;
    private TextField streetField;
    private TextField cityField;
    private TextField stateField;
    private ComboBox<String> countryBox;
    private DatePicker datePicker;

    /**
     * Initializes and displays the JavaFX user interface.
     *
     * @param stage Primary application window.
     */
    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();

        Image bgImage = loadImage("earth-background.png");

        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(115, 115, true, true, true, true)
        );

        root.setBackground(new Background(backgroundImage));

        VBox pageLayout = new VBox(14);
        pageLayout.setAlignment(Pos.CENTER);

        VBox mainCard = new VBox(16);
        mainCard.getStyleClass().add("main-card");
        mainCard.setMaxWidth(660);
        mainCard.setPadding(new Insets(24, 30, 24, 30));
        mainCard.setAlignment(Pos.TOP_CENTER);

        VBox header = new VBox(4);
        header.setAlignment(Pos.CENTER);

        Label title = new Label("Sun Rise/Set Time Finder");
        title.getStyleClass().add("title");

        Label subtitle = new Label(
                "Find accurate sunrise and sunset times by location and date."
        );
        subtitle.getStyleClass().add("subtitle");

        header.getChildren().addAll(title, subtitle);

        VBox formCard = new VBox(10);
        formCard.getStyleClass().add("form-card");
        formCard.setMaxWidth(590);

        HBox locationHeader = new HBox(8);
        locationHeader.setAlignment(Pos.CENTER_LEFT);

        ImageView locationHeaderIcon = createIcon("location-icon.png", 24);

        Label locationTitle = new Label("Location Information");
        locationTitle.getStyleClass().add("section-title");

        locationHeader.getChildren().addAll(locationHeaderIcon, locationTitle);

        streetField = new TextField();
        streetField.setPromptText("Street Address (Optional)");

        cityField = new TextField();
        cityField.setPromptText("City");

        stateField = new TextField();
        stateField.setPromptText("State / Province / Region (Optional)");

        countryBox = new ComboBox<>();
        countryBox.getItems().addAll(
                "usa",
                "United States",
                "Canada",
                "Mexico",
                "Japan",
                "United Kingdom",
                "France",
                "Germany",
                "Australia"
        );
        countryBox.setValue("usa");
        countryBox.setEditable(true);
        countryBox.setMaxWidth(Double.MAX_VALUE);

        HBox streetRow = createInputRow("home-icon.png", streetField);
        HBox cityRow = createInputRow("city-icon.png", cityField);
        HBox stateRow = createInputRow("map-icon.png", stateField);
        HBox countryRow = createComboRow("globe-icon.png", countryBox);

        Separator separator = new Separator();

        VBox dateArea = new VBox(9);
        dateArea.setAlignment(Pos.CENTER);

        HBox dateLabelRow = new HBox(7);
        dateLabelRow.setAlignment(Pos.CENTER);

        ImageView calendarIcon = createIcon("calendar-icon.png", 22);

        Label dateLabel = new Label("Select Date");
        dateLabel.getStyleClass().add("date-title");

        dateLabelRow.getChildren().addAll(calendarIcon, dateLabel);

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(240);

        Button searchButton = new Button("Find Sunrise/Sunset");
        searchButton.getStyleClass().add("search-button");
        searchButton.setPrefWidth(290);

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("reset-button");
        resetButton.setPrefWidth(105);

        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("reset-button");
        exitButton.setPrefWidth(105);

        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getChildren().addAll(searchButton, resetButton, exitButton);

        dateArea.getChildren().addAll(dateLabelRow, datePicker, buttonRow);

        formCard.getChildren().addAll(
                locationHeader,
                streetRow,
                cityRow,
                stateRow,
                countryRow,
                separator,
                dateArea
        );

        resultsSection = new VBox();
        resultsSection.setVisible(false);
        resultsSection.setManaged(false);

        mainCard.getChildren().addAll(header, formCard, resultsSection);

        Label footer = new Label("Accurate times based on astronomical calculations");
        footer.getStyleClass().add("footer");

        pageLayout.getChildren().addAll(mainCard, footer);

        searchButton.setOnAction(e -> showResults());
        resetButton.setOnAction(e -> resetForm());
        exitButton.setOnAction(e -> stage.close());

        root.getChildren().add(pageLayout);

        Scene scene = new Scene(root, 1180, 850);
        scene.getStylesheets()
                .add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Sun Rise/Set Time Finder");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates the results display section after a successful calculation.
     *
     * @param location Geocoded location information.
     * @param result Calculated sunrise and sunset results.
     * @param zoneId Timezone associated with the location.
     * @return Fully populated results display container.
     */
    private VBox buildDynamicResultsSection(
            LocationResult location,
            SunriseSunsetResult result,
            ZoneId zoneId
    ) {

        VBox resultsCard = new VBox(12);
        resultsCard.getStyleClass().add("results-card");
        resultsCard.setMaxWidth(590);

        HBox resultsHeader = new HBox(8);
        resultsHeader.setAlignment(Pos.CENTER_LEFT);

        ImageView resultsIcon = createIcon("sunrise-icon.png", 24);

        Label resultsTitle = new Label("Results");
        resultsTitle.getStyleClass().add("section-title");

        resultsHeader.getChildren().addAll(resultsIcon, resultsTitle);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);

        grid.add(createResultBox(
                "location-icon.png",
                "Location",
                location.getDisplayName(),
                "green-value"
        ), 0, 0);

        grid.add(createResultBox(
                "sunrise-icon.png",
                "Sunrise",
                result.getFormattedSunrise(),
                "orange-value"
        ), 1, 0);

        grid.add(createResultBox(
                "sunset-icon.png",
                "Sunset",
                result.getFormattedSunset(),
                "pink-value"
        ), 2, 0);

        grid.add(createResultBox(
                "timezone-icon.png",
                "Timezone",
                zoneId.toString(),
                "purple-value"
        ), 0, 1);

        grid.add(createResultBox(
                "latitude-icon.png",
                "Latitude",
                String.valueOf(location.getLatitude()),
                "blue-value"
        ), 1, 1);

        grid.add(createResultBox(
                "longitude-icon.png",
                "Longitude",
                String.valueOf(location.getLongitude()),
                "teal-value"
        ), 2, 1);

        HBox infoBar = new HBox(14);
        infoBar.getStyleClass().add("info-bar");
        infoBar.setAlignment(Pos.CENTER);

        Label dateInfo = new Label("Date: " + datePicker.getValue());

        Label dayLength = new Label(
                "Day Length: " + result.getFormattedDayLength()
        );

        dateInfo.getStyleClass().add("info-text");
        dayLength.getStyleClass().add("info-text");

        Label dividerOne = new Label("|");
        dividerOne.getStyleClass().add("info-divider");

        infoBar.getChildren().addAll(
                dateInfo,
                dividerOne,
                dayLength
        );

        resultsCard.getChildren().addAll(
                resultsHeader,
                grid,
                infoBar
        );

        return resultsCard;
    }

    /**
     * Creates an individual result display box containing an icon,
     * label, and value.
     *
     * @param iconFile Image file used as the icon.
     * @param label Descriptive label for the result.
     * @param value Value to display.
     * @param valueClass CSS style class applied to the value.
     * @return Styled result display box.
     */
    private VBox createResultBox(
            String iconFile,
            String label,
            String value,
            String valueClass
    ) {

        HBox top = new HBox(7);
        top.setAlignment(Pos.CENTER_LEFT);

        ImageView icon = createIcon(iconFile, 24);

        Label titleLabel = new Label(label);
        titleLabel.getStyleClass().add("result-title");

        top.getChildren().addAll(icon, titleLabel);

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().addAll("result-value", valueClass);
        valueLabel.setWrapText(true);

        VBox box = new VBox(6);
        box.getStyleClass().add("result-box");
        box.getChildren().addAll(top, valueLabel);

        return box;
    }

    /**
     * Creates a styled text input row consisting of an icon and
     * text field.
     *
     * @param iconFile Image used as the row icon.
     * @param field Text field associated with the input.
     * @return Configured input row.
     */
    private HBox createInputRow(String iconFile, TextField field) {

        HBox row = new HBox(8);
        row.getStyleClass().add("input-row");
        row.setAlignment(Pos.CENTER_LEFT);

        ImageView icon = createIcon(iconFile, 22);

        HBox.setHgrow(field, Priority.ALWAYS);

        row.getChildren().addAll(icon, field);

        return row;
    }

    /**
     * Creates a styled combo box row consisting of an icon and
     * selectable dropdown field.
     *
     * @param iconFile Image used as the row icon.
     * @param comboBox ComboBox used for user selection.
     * @return Configured combo box row.
     */
    private HBox createComboRow(
            String iconFile,
            ComboBox<String> comboBox
    ) {

        HBox row = new HBox(8);
        row.getStyleClass().add("input-row");
        row.setAlignment(Pos.CENTER_LEFT);

        ImageView icon = createIcon(iconFile, 22);

        HBox.setHgrow(comboBox, Priority.ALWAYS);

        row.getChildren().addAll(icon, comboBox);

        return row;
    }

    /**
     * Loads and formats an icon image for display within the UI.
     *
     * @param fileName Image resource file name.
     * @param size Desired icon size in pixels.
     * @return Configured ImageView object.
     */
    private ImageView createIcon(String fileName, int size) {

        ImageView icon = new ImageView(loadImage(fileName));
        icon.setFitWidth(size);
        icon.setFitHeight(size);
        icon.setPreserveRatio(true);
        icon.setSmooth(true);

        return icon;
    }

    /**
     * Loads an image resource from the application's resources folder.
     *
     * @param fileName Name of the image resource.
     * @return Loaded Image object.
     */
    private Image loadImage(String fileName) {

        return new Image(
                getClass()
                        .getResource(fileName)
                        .toExternalForm()
        );
    }

    /**
     * Processes user input and displays sunrise and sunset results.
     *
     * This method validates user input, constructs a location query,
     * retrieves geographic coordinates, determines the appropriate
     * timezone, performs sunrise/sunset calculations, and updates
     * the results section of the interface.
     */
    private void showResults() {

        try {
            String street = streetField.getText().trim();
            String city = cityField.getText().trim();
            String state = stateField.getText().trim();
            String country = getCountryValue();

            // Validate required location information.
            if (city.isEmpty() || country.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Please enter at least city and country."
                );
                alert.showAndWait();
                return;
            }

            // Build a complete address string for geocoding.
            StringBuilder addressBuilder = new StringBuilder();

            if (!street.isEmpty()) {
                addressBuilder.append(street).append(", ");
            }

            addressBuilder.append(city);

            if (!state.isEmpty()) {
                addressBuilder.append(", ").append(state);
            }

            addressBuilder.append(", ").append(country);

            String address = addressBuilder.toString();

            // Initialize backend services.
            GeocodingService geocodingService = new GeocodingService();
            TimeZoneService timeZoneService = new TimeZoneService();
            SolarCalculator solarCalculator = new SolarCalculator();

            // Retrieve coordinates from the geocoding service.
            LocationResult location =
                    geocodingService.getLocationFromAddress(address);

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Determine the timezone associated with the coordinates.
            ZoneId zoneId =
                    timeZoneService.getTimeZone(latitude, longitude);

            // Calculate sunrise and sunset times for the selected date.
            SunriseSunsetResult result =
                    solarCalculator.calculateSunriseSunset(
                            latitude,
                            longitude,
                            datePicker.getValue(),
                            zoneId
                    );

            // Replace any previous results with the newly calculated values.
            resultsSection.getChildren().clear();

            resultsSection.getChildren().add(
                    buildDynamicResultsSection(
                            location,
                            result,
                            zoneId
                    )
            );

            resultsSection.setVisible(true);
            resultsSection.setManaged(true);

        } catch (Exception ex) {

            // Display an error dialog if processing fails.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to calculate sunrise/sunset.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            ex.printStackTrace();
        }
    }

    /**
     * Retrieves the country value entered or selected by the user.
     *
     * @return Country value entered by the user.
     */
    private String getCountryValue() {

        if (countryBox.getEditor() != null
                && !countryBox.getEditor().getText().trim().isEmpty()) {

            return countryBox.getEditor().getText().trim();
        }

        if (countryBox.getValue() != null) {
            return countryBox.getValue().trim();
        }

        return "";
    }

    /**
     * Restores all user input fields to their default state and
     * clears any displayed results.
     */
    private void resetForm() {

        streetField.clear();
        cityField.clear();
        stateField.clear();
        countryBox.setValue("usa");
        countryBox.getEditor().clear();
        datePicker.setValue(LocalDate.now());

        resultsSection.getChildren().clear();
        resultsSection.setVisible(false);
        resultsSection.setManaged(false);
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
