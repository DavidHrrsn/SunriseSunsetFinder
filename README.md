# \# Sun Rise/Set Time Finder

# 

# \## Project Overview

# 

# The Sun Rise/Set Time Finder is a Java desktop application designed to calculate and display sunrise and sunset times for user-specified locations and dates. The application combines a JavaFX graphical user interface with backend services that perform geocoding, timezone determination, and local astronomical calculations.

# 

# Users can enter a street address, city, state/province (optional), country, and date to receive:

# 

# \* Sunrise time

# \* Sunset time

# \* Day length

# \* Timezone information

# \* Latitude and longitude coordinates

# \* Formatted location information

# 

# This project is being developed as part of CMSC 495 Capstone Project.

# 

# \---

# 

# \## Project Team

# 

# \* David Harrison – Backend Developer

# \* Samuel Garmoe – Project Manager

# \* Jay Briggs – Frontend Developer

# \* Jeremy Ross – Testing Lead

# \* Apurva Dave – Documentation and Presentation Lead

# 

# \---

# 

# \## Features

# 

# \### Current Phase I Features

# 

# \* JavaFX graphical user interface

# \* Street address, city, state/province, and country input

# \* City and country-only searches

# \* OpenStreetMap geocoding integration

# \* Coordinate-based timezone determination

# \* Local sunrise and sunset calculations

# \* Day length calculation

# \* Error handling and validation

# \* GitHub version control integration

# 

# \### Planned Future Features

# 

# \* Date range calculations

# \* Enhanced error reporting

# \* Improved user interface styling

# \* Additional testing automation

# \* Search history support

# 

# \---

# 

# \## Project Structure

# 

# ```text

# src/

# └── sunrisesunsetupdated/

# &#x20;   ├── SunriseSunsetApp.java

# &#x20;   ├── GeocodingService.java

# &#x20;   ├── TimeZoneService.java

# &#x20;   ├── SolarCalculator.java

# &#x20;   ├── LocationResult.java

# &#x20;   ├── SunriseSunsetResult.java

# &#x20;   ├── BackendTest.java

# &#x20;   └── Main.java

# ```

# 

# \### Class Descriptions

# 

# | Class               | Purpose                                          |

# | ------------------- | ------------------------------------------------ |

# | SunriseSunsetApp    | Main JavaFX application                          |

# | GeocodingService    | Converts user-entered locations into coordinates |

# | TimeZoneService     | Determines timezone based on coordinates         |

# | SolarCalculator     | Calculates sunrise, sunset, and day length       |

# | LocationResult      | Stores geocoding results                         |

# | SunriseSunsetResult | Stores formatted sunrise/sunset results          |

# | BackendTest         | Backend testing utility                          |

# | Main                | Application launcher                             |

# 

# \---

# 

# \## Development Environment

# 

# \### Required Software

# 

# | Dependency  | Version              |

# | ----------- | -------------------- |

# | Java JDK    | 21 or later          |

# | JavaFX SDK  | 23.0.1               |

# | Eclipse IDE | Current Java Edition |

# | Git         | Current Version      |

# | GitHub      | Current Version      |

# 

# \### APIs Used

# 

# \#### OpenStreetMap Nominatim API

# 

# Purpose:

# 

# \* Address geocoding

# \* Coordinate lookup

# 

# Documentation:

# https://nominatim.org/release-docs/latest/api/Overview/

# 

# \#### TimeAPI

# 

# Purpose:

# 

# \* Timezone lookup based on coordinates

# 

# Documentation:

# https://timeapi.io/

# 

# \---

# 

# \## Installation Instructions

# 

# \### Step 1 – Clone Repository

# 

# ```bash

# git clone https://github.com/DavidHrrsn/SunriseSunsetFinder.git

# ```

# 

# \### Step 2 – Import into Eclipse

# 

# 1\. Open Eclipse

# 2\. Select File → Import

# 3\. Choose Existing Projects into Workspace

# 4\. Select the project folder

# 5\. Finish import

# 

# \### Step 3 – Install JavaFX

# 

# Download JavaFX SDK 23.0.1:

# 

# https://openjfx.io/

# 

# \### Step 4 – Configure JavaFX VM Arguments

# 

# Add the following VM arguments to your Eclipse Run Configuration:

# 

# ```text

# \--module-path "PATH\_TO\_JAVAFX\_LIB"

# \--add-modules javafx.controls,javafx.fxml

# ```

# 

# Replace:

# 

# ```text

# PATH\_TO\_JAVAFX\_LIB

# ```

# 

# with your local JavaFX lib folder.

# 

# Example:

# 

# ```text

# \--module-path "C:\\javafx-sdk-23.0.1\\lib"

# \--add-modules javafx.controls,javafx.fxml

# ```

# 

# \---

# 

# \## Running the Application

# 

# 1\. Launch Eclipse

# 2\. Open the project

# 3\. Run:

# 

# ```text

# SunriseSunsetApp.java

# ```

# 

# 4\. Enter:

# 

# \* Street Address (optional)

# \* City

# \* State/Province (optional)

# \* Country

# \* Date

# 

# 5\. Click:

# 

# ```text

# Find Sunrise/Sunset

# ```

# 

# 6\. Review results displayed in the interface.

# 

# \---

# 

# \## Backend Testing

# 

# The project includes:

# 

# ```text

# BackendTest.java

# ```

# 

# This utility allows backend functionality to be tested independently of the JavaFX interface.

# 

# Current tests include:

# 

# \* Address validation

# \* Geocoding verification

# \* Timezone verification

# \* Sunrise calculations

# \* Sunset calculations

# \* Day length calculations

# \* Error handling

# 

# Example test locations:

# 

# \* Baltimore, Maryland, USA

# \* Dallas, Texas, USA

# \* Tucson, Arizona, USA

# \* Tokyo, Japan

# 

# \---

# 

# \## Known Limitations

# 

# \* Internet connection required for geocoding and timezone services

# \* API availability depends on external providers

# \* Polar regions may produce special sunrise/sunset cases

# \* Accuracy depends on geocoding precision and timezone data

# 

# \---

# 

# \## Future Improvements

# 

# \* Formal JUnit test suite

# \* Date range support

# \* Improved exception handling

# \* Enhanced GUI styling

# \* Additional output formatting options

# 

# \---

# 

# \## References

# 

# OpenStreetMap Nominatim API Documentation

# https://nominatim.org/release-docs/latest/api/Overview/

# 

# TimeAPI Documentation

# https://timeapi.io/

# 

# NOAA Solar Calculation Details

# https://gml.noaa.gov/grad/solcalc/calcdetails.html

# 

# United States Naval Observatory Astronomical Applications Department

# https://aa.usno.navy.mil/



