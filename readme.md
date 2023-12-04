# Android example using microprofile config

This example shows an Android application implemented in Java using Jetpack Compose for the views, [Reactive Extensions](https://reactivex.io/) for [Java](https://github.com/ReactiveX/RxJava) and the [SmallRye config](https://smallrye.io/smallrye-config) implementation of [microprofile-config](https://microprofile.io/) for configuration.

## Configuration

The app can be configured in the [src/main/resources/application.properties](./app/src/main/resources/application.properties) file. According to the standard also a [META-INF/microprofile-config.properties](./app/src/main/resources/META-INF/microprofile-config.properties) is present.

## Problem with Android missing java.nio.file jar provider

in the [ApplicationConfiguration.java](./app/src/main/java/at/aberger/smallrye/config/ApplicationConfiguration.java) we cannot use
the usual way to get the config:
``` java
var config = ConfigProvider.getConfig().unwrap(SmallRyeConfig.class);
```

This crashes with a java.nio.file.ProviderNotFoundException propably because the android implementation of java.nio.file contains no jar file provider. For Details see [Jar and Zip FileSystemProvider not present on API 26+](https://issuetracker.google.com/issues/153773248).

As a workaround we implement our own ConfigSourceProvider.
