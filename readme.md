# Android example using microprofile config

This example shows an Android application implemented in Java using Jetpack Compose for the views, [Reactive Extensions](https://reactivex.io/) for [Java](https://github.com/ReactiveX/RxJava) and the [SmallRye config](https://smallrye.io/smallrye-config) implementation of [microprofile-config](https://microprofile.io/) for configuration.

## Building

Install gradle and gradle wrapper

```bash
brew install gradle
gradle wrapper
```

Download the latest version of Android Studio and open the project. Then create a simulator device and debug it on that device.

## Configuration

The app can be configured in the [src/main/resources/application.properties](./app/src/main/resources/application.properties) file. According to the standard also a [META-INF/microprofile-config.properties](./app/src/main/resources/META-INF/microprofile-config.properties) is present.

## Problem with Android missing java.nio.file jar provider

in the [ApplicationConfiguration.java](./app/src/main/java/at/aberger/smallrye/config/ApplicationConfiguration.java) we cannot use
the usual way to get the config:

```java
var config = ConfigProvider.getConfig().unwrap(SmallRyeConfig.class);
```

To see the problem remove the comment in the above source code line and try to uses this as a config instead of the current one.

This crashes with a java.nio.file.ProviderNotFoundException propably because the android implementation of java.nio.file contains no jar file provider. For Details see [Jar and Zip FileSystemProvider not present on API 26+](https://issuetracker.google.com/issues/153773248).

As a workaround we implement our own ConfigSourceProvider.

## How to use microprofile config in a new empty Android start project

- Download [Android Studio](https://developer.android.com/studio)
- Start Android Studio
- create a new Project "Empty Activity"
- Under "Gradle Scripts" open build.gradle.tk (Module :app)
- add the following line to dependencies:

``` gradle
implementation("io.smallrye.config:smallrye-config:3.4.4")
```

- add the following line ```excludes += "META-INF/INDEX.LIST"``` to the same build.gradle.tk so that the section reads:

``` gradle
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
        }
    }
```

- open MainActivity.kt and add the following line after ```super.onCreate(savedInstanceState)```:

```kotlin
    val config = ConfigProvider.getConfig().unwrap(SmallRyeConfig::class.java)
```

- Start the project, it will display "Hello, Android!
- In Project Explorer switch from "Android" to "Project" view
- Navigate to "app/src/main" and add a folder "resources" and below resources add a folder "META-INF"
- Add a file "microprofile-config.properties" to the folder app/src/main/resources/META-INF
- start the project again
```
