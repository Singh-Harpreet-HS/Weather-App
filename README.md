
# Weather Application

This project is a Weather Application which efficiently fetches the weather data based on user's location and also by asking the user of the location of their choice.

## Core Features

- Retrieve current weather based on device location.
- Prompt user to enable location settings if they are disabled.
- Display weather information in a user-friendly UI.
- Handle location and network permissions gracefully.

## Tech Stack

- **Programming Language**: Kotlin
- **User Interface**: Jetpack Compose
- **Dependency Injection**: Dagger-Hilt
- **Navigation**: Jetpack Navigation
- **Permissions and Services**: Google Play Services Location API
- **Network Requests**: Retrofit
- **State Management**: StateFlow and ViewModel
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles

## Architecture

The project follows the MVVM (Model-View-ViewModel) architecture, coupled with Clean Architecture principles to separate the different layers of the application clearly. 

1. **View Layer**:
   - Uses Jetpack Compose for creating UI components.
   - Observes LiveData/StateFlow from ViewModel to update the UI.
  
2. **ViewModel Layer**:
   - Manages UI-related data and business logic.
   - Uses Kotlin coroutines for asynchronous operations.
   - Injected with repositories for data access.
  
3. **Model Layer**:
   - Abstracts data sources from network or local database.
   - Uses Retrofit for network requests.

## Permissions

The application requires the following permissions:

- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `INTERNET`

These permissions are required to fetch the user's location and retrieve weather data from the internet.

#### Location Handling
The app initially checks for location permissions. If the permissions are granted, it will retrieve the last known location. If no recent location is available or permissions are not granted, it will request new location data or request the necessary permissions.

#### ViewModel Logic
Handle Intent: Based on the received intent, it fetches weather data.
Fetch Weather: Uses repositories to fetch weather data from APIs.
Location Settings Check: Ensures location settings are enabled before attempting to fetch the user's location.
Flow Diagram


## Getting Started

### Prerequisites

- Android Studio
- Kotlin 1.5+
- Gradle 7.0+
- Google Play Services


![showing_weather](https://github.com/user-attachments/assets/df0bf54a-1c80-4ebf-bff9-4ac52897c65d)

The above picture shows that the weather data is being shown according to the user's location.

![different_weather](https://github.com/user-attachments/assets/06e12d19-de41-4965-b597-a50ea84ac20a)
![different_location](https://github.com/user-attachments/assets/25c64306-7e22-4106-806c-fb0c19dc6d98)

The above picture shows that the weather data is being shown according to the user's manually entered data, background image is also being changed according the weather condition.

![showing_error](https://github.com/user-attachments/assets/f0c4fafb-a84c-4bf5-b5c6-60864852994d)
![asking_user_to_turn_location](https://github.com/user-attachments/assets/2047e623-06fa-442d-bf6d-d9e950bd4bbb)

The above picture shows that it throws error when it is not able to get the location access and prompts a request to grant the location access.




### Clone the repository




```bash
git clone https://github.com/Singh-Harpreet-HS/Weather-App



