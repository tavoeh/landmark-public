# Landmark Remark

Install Landmark Remark App on your device and enjoy saving short notes of your favourite places in your city. Find your notes easily by searching using a user name or contained text.

### Requirements

* As a user (of the application) I can see my current location on a map
* As a user I can save a short note at my current location
* As a user I can see notes that I have saved at the location they were saved on the map
* As a user I can see the location, text, and user-name of notes other users have saved
* As a user I have the ability to search for a note based on contained text or user-name

### implicit Requirements
* As a user I can save a short note in any location of the map
* As a user I can save a note only if I have a valid user account and I am authenticated
* As a user I can save a note of a maximum of 280 characters
* As a user I can read the notes by tapping on the markers displayed on the map
* As a user I can create a user account
* As a user I can log in using my user account
* As a user I can enable or deny location permissions of the application
* As a user I can be notified about any error in the app 

### Time: 22H
*  4H: Architecture definition and implementation 
*  2H: Unit tests
*  1H: Map setup (Google maps)
*  4H: Notes Feature (Read, create)
*  3H: Search Feature
*  3H: User Authentication (Sign In, Sign Up, Logout)
*  2H: Backend set up (Firebase)
*  2H: Testing and bug fixes
*  1H Documentation

## Project setup
* Clone the repo and open the project in Android Studio
* Get a Google Maps API Key following this steps described in the [Google Maps documentation](https://developers.google.com/maps/documentation/android-sdk/get-api-key) and add it to the `google_maps_api.xml` file
* This project uses **Firebase Cloud Firestore** and **Email/Password authentication** as backend services. To configure this services go to the [Firebase Console](https://console.firebase.google.com/u/0/) and create a new projectCreate a new project. Once the project has been created created, enable the **Email/Password** Sign-in method and create a database with a single collection called **Notes**. The project will use this collection to store the notes created in the map. 
* Finally, download the `google-services.json` file from Firebase and add it to the project or follow the steps specified in the [Firebase documentation](https://firebase.google.com/docs/android/setup)

## Architecture

The application architecture is based on Clean Architecture. It is composed of one Android module that includes three main layers:

* **Presentation**: This layer contains the UI elements ( Activities, Fragments, Views, Adapters, etc... ) and the Android [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel) from the Android Architecture Components. The ViewModel receives events from the Views and proceeds to get the required data which is then turned into view states in the form of [Livedata](https://developer.android.com/topic/libraries/architecture/livedata). The Views either listen to or react to directly via [Data Binding](https://developer.android.com/topic/libraries/data-binding).
* **Domain**: This holds the business logic. This layer contains the business models ( entities, Kotlin Data classes ), the contract definition for the Repositories and the business use cases.   
* **Data**: This layer handles the data: Where it comes from and how to get it. This logic will conform to the contracts defined in the domain layer. The Repositories can choose whether the data should be fetched from a local o remote data sources. 

### Data Flow
On the presentation side the ViewModel will react to a user action or UI event and use use cases to get some data through the repositories. The information traveling from the data layer, through the domain layer and into the ViewModel in the presentation layer is contained in a Result object, which is either a Success or an Error.     

 Exceptions and errors happening along the way are caught and wrapped into custom Error objects which can be acted upon appropriately.
 
The communication between layers is done with Kotlin [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) in order to perform asynchronous tasks in a structured way. 

To get the data from the data sources the system uses one-shot components which means that an action will perform an operation only once, therefore refresh-data operations have to be done manually (eg. press a refresh button).
Multiple-values components or Real-time data handling is out of the scope could be implemented using Courtine [Flows](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/).
 

## Technology stack


### front-end

Android Native Application developed using Android Studio 4.0.1 and Google libraries.

* Kotlin 1.3.72
* Gradle 6.1.1
* minSdkVersion 26
* targetSdkVersion 29  

**lbraries**

```
Navigation Components
implementation 'androidx.navigation:navigation-fragment-ktx:$2.3.0'
implementation 'androidx.navigation:navigation-ui-ktx:$2.3.0'
    
Google Maps
implementation 'com.google.android.gms:play-services-maps:17.0.0'

Location
implementation "com.google.android.gms:play-services-location:17.0.0"

Material Components
implementation 'com.google.android.material:material:1.1.0'

Dependency injection
implementation 'com.google.dagger:dagger:2.28'
```

### backend

Firebase for Android.

**lbraries**

```
Storage
implementation 'com.google.firebase:firebase-firestore-ktx:21.5.0'

User Authentication
implementation 'com.google.firebase:firebase-auth-ktx:19.3.2'
```

## Testing
To run unit tests type in the command line:
```./gradlew test  ```

The architecture implemented in this application is based on SOLID principles, therefore, the business rules and the system layers can be unit tested.

The components included as part of the unit test coverage are:

* Presentation layer: Viewmodels and LiveData objects that hold the data that will be displayed in the UI 
* Domain layer: Use cases and business logic tests
* Data layer: Repositories and Datasources tests
* Extension functions: Helper classes and extension functions with business logic

**Note:** Components that are not included as part of the unit test coverage:

* Autogenerated classes by the DI framework and Databinding 
* UI and VIew components such us Activities, Fragments and Custom Views
* Third-party libraries

**lbraries**

```
testImplementation 'org.mockito:mockito-core:3.3.3'
testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.7'
testImplementation 'com.google.truth:truth:1.0.1
testImplementation 'junit:junit:4.13
```

## Design

Based on the [Material Components](https://material.io/develop/android) Specification.

*Dark/Ligth* modes supported.

## Accesibility
Based on the [Android accesibility](https://developer.android.com/guide/topics/ui/accessibility) Guidelines. 

## Out of the scope
* Localisation
* Real-time updates
* Note selection from the Search Notes List
* User Location updates
* Error handling depending on the Exception type, at the moment the app uses a DomainError to manage all types of exceptions

## Known issues
* In some instances Google Maps MapFragment is extremely laggy on returning from the backstack.