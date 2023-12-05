# Android example using microprofile config

This example shows an Android application implemented in Java using Jetpack Compose for the views, [Reactive Extensions](https://reactivex.io/) for [Java](https://github.com/ReactiveX/RxJava) and the [SmallRye config](https://smallrye.io/smallrye-config) implementation of [microprofile-config](https://microprofile.io/) for configuration.

## Building

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

## How to reproduce the problem in a new empty Android start project

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
- you will get the following Stack Trace:

```text
FATAL EXCEPTION: main
Process: at.htl.leonding.configtest, PID: 15026
java.lang.RuntimeException: Unable to start activity ComponentInfo{at.htl.leonding.configtest/at.htl.leonding.configtest.MainActivity}: java.nio.file.ProviderNotFoundException: Provider not found
at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3782)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3922)
at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:103)
at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:139)
at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:96)
at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2443)
at android.os.Handler.dispatchMessage(Handler.java:106)
at android.os.Looper.loopOnce(Looper.java:205)
at android.os.Looper.loop(Looper.java:294)
at android.app.ActivityThread.main(ActivityThread.java:8177)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:552)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:971)
Caused by: java.nio.file.ProviderNotFoundException: Provider not found
at java.nio.file.FileSystems.newFileSystem(FileSystems.java:407)
at io.smallrye.common.classloader.ClassPathUtils.processAsPath(ClassPathUtils.java:139)
at io.smallrye.common.classloader.ClassPathUtils.consumeAsPath(ClassPathUtils.java:102)
at io.smallrye.common.classloader.ClassPathUtils.consumeAsPaths(ClassPathUtils.java:86)
at io.smallrye.config.AbstractLocationConfigSourceLoader.tryClassPath(AbstractLocationConfigSourceLoader.java:139)
at io.smallrye.config.AbstractLocationConfigSourceLoader.loadConfigSources(AbstractLocationConfigSourceLoader.java:102)
at io.smallrye.config.AbstractLocationConfigSourceLoader.loadConfigSources(AbstractLocationConfigSourceLoader.java:85)
at io.smallrye.config.PropertiesConfigSourceProvider.<init>(PropertiesConfigSourceProvider.java:37)
at io.smallrye.config.PropertiesConfigSourceProvider.classPathResource(PropertiesConfigSourceProvider.java:69)
at io.smallrye.config.PropertiesConfigSourceProvider.classPathSources(PropertiesConfigSourceProvider.java:77)
at io.smallrye.config.SmallRyeConfigBuilder.getDefaultSources(SmallRyeConfigBuilder.java:186)
at io.smallrye.config.SmallRyeConfig$ConfigSources.buildSources(SmallRyeConfig.java:609)
at io.smallrye.config.SmallRyeConfig$ConfigSources.<init>(SmallRyeConfig.java:557)
at io.smallrye.config.SmallRyeConfig.<init>(SmallRyeConfig.java:68)
at io.smallrye.config.SmallRyeConfigBuilder.build(SmallRyeConfigBuilder.java:698)
at io.smallrye.config.SmallRyeConfigFactory$Default.getConfigFor(SmallRyeConfigFactory.java:60)
at io.smallrye.config.SmallRyeConfigProviderResolver.getConfig(SmallRyeConfigProviderResolver.java:76)
at io.smallrye.config.SmallRyeConfigProviderResolver.getConfig(SmallRyeConfigProviderResolver.java:64)
at org.eclipse.microprofile.config.ConfigProvider.getConfig(ConfigProvider.java:85)
at at.htl.leonding.configtest.MainActivity.onCreate(MainActivity.kt:21)
at android.app.Activity.performCreate(Activity.java:8595)
at android.app.Activity.performCreate(Activity.java:8573)
at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1456)
at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3764)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3922) 
at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:103) 
at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:139) 
at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:96) 
at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2443) 
at android.os.Handler.dispatchMessage(Handler.java:106) 
at android.os.Looper.loopOnce(Looper.java:205) 
at android.os.Looper.loop(Looper.java:294) 
at android.app.ActivityThread.main(ActivityThread.java:8177) 
at java.lang.reflect.Method.invoke(Native Method) 
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:552) 
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:971) 
```
