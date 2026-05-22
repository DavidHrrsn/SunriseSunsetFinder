package sunrisesunsetupdated;

import java.time.LocalDate;
import java.time.ZoneId;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SunriseSunsetApp extends Application {

    private TextField streetField;
    private TextField cityField;
    private TextField stateField;
    private TextField countryField;
    private DatePicker datePicker;
    private TextArea resultArea;

    private final GeocodingService geocodingService =
            new GeocodingService();

    private final TimeZoneService timeZoneService =
            new TimeZoneService();

    private final SolarCalculator solarCalculator =
            new SolarCalculator();

    @Override
    public void start(Stage stage) {

        Label titleLabel =
                new Label("Sun Rise/Set Time Finder");

        titleLabel.setStyle(
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: black;" +
                "-fx-effect: dropshadow(gaussian, white, 2, 0.5, 1, 1);"
        );

        streetField = new TextField();
        streetField.setPromptText("Street Address (Optional)");

        cityField = new TextField();
        cityField.setPromptText("City");

        stateField = new TextField();
        stateField.setPromptText("State / Province / Region (Optional)");

        countryField = new TextField();
        countryField.setPromptText("Country");

        datePicker = new DatePicker(LocalDate.now());

        Button searchButton =
                new Button("Find Sunrise/Sunset");

        searchButton.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );

        searchButton.setOnAction(e -> findSunriseSunset());

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(220);

        VBox inputBox = new VBox(12);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(20));
        inputBox.setMaxWidth(500);

        inputBox.setStyle(
                "-fx-background-color: rgba(255,255,255,0.88);" +
                "-fx-background-radius: 15;"
        );

        inputBox.getChildren().addAll(
                new Label("Enter Location Information"),
                streetField,
                cityField,
                stateField,
                countryField,
                new Label("Select Date"),
                datePicker,
                searchButton,
                resultArea
        );

        VBox mainLayout = new VBox(25);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));
        mainLayout.getChildren().addAll(titleLabel, inputBox);

        StackPane root = new StackPane(mainLayout);

        String imagePath =
                "file:/C:/Users/David/eclipse-workspace/sunrisesunsetupdated/src/sunrisesunsetupdated/north_america.jpg";

        root.setStyle(
                "-fx-background-image: url('" + imagePath + "');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center center;"
        );

        Scene scene = new Scene(root, 950, 700);

        stage.setTitle("Sun Rise/Set Time Finder");
        stage.setScene(scene);
        stage.show();
    }

    private void findSunriseSunset() {

        try {
            String street =
                    streetField.getText().trim();

            String city =
                    cityField.getText().trim();

            String state =
                    stateField.getText().trim();

            String country =
                    countryField.getText().trim();

            if (city.isEmpty() || country.isEmpty()) {

                resultArea.setText(
                        "Please enter at least city and country."
                );

                return;
            }

            StringBuilder addressBuilder =
                    new StringBuilder();

            if (!street.isEmpty()) {
                addressBuilder
                        .append(street)
                        .append(", ");
            }

            addressBuilder.append(city);

            if (!state.isEmpty()) {
                addressBuilder
                        .append(", ")
                        .append(state);
            }

            addressBuilder
                    .append(", ")
                    .append(country);

            String address =
                    addressBuilder.toString();

            LocalDate selectedDate =
                    datePicker.getValue();

            LocationResult location =
                    geocodingService.getLocationFromAddress(address);

            double latitude =
                    location.getLatitude();

            double longitude =
                    location.getLongitude();

            ZoneId zoneId =
                    timeZoneService.getTimeZone(
                            latitude,
                            longitude
                    );

            SunriseSunsetResult result =
                    solarCalculator.calculateSunriseSunset(
                            latitude,
                            longitude,
                            selectedDate,
                            zoneId
                    );

            resultArea.setText(
                    "Location Found:\n" +
                    location.getDisplayName() +

                    "\n\nCoordinates Used:" +
                    "\nLatitude: " + latitude +
                    "\nLongitude: " + longitude +

                    "\n\nTimezone: " + zoneId +

                    "\n\nDate: " + selectedDate +

                    "\n\nSunrise: " +
                    result.getFormattedSunrise() +

                    "\nSunset: " +
                    result.getFormattedSunset() +

                    "\nDay Length: " +
                    result.getFormattedDayLength()
            );

        } catch (Exception ex) {

            resultArea.setText(
                    "Error: " + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}