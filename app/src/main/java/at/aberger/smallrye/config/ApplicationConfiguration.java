package at.aberger.smallrye.config;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.smallrye.config.PropertiesConfigSource;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;

/** Read the configuration of the app from microprofile config.
 * As an example we only read the greeting text.
 */
public class ApplicationConfiguration {
    public final String greeting;

    public ApplicationConfiguration() {
        //var config = ConfigProvider.getConfig().unwrap(SmallRyeConfig.class); // <=== does not work

        var config = new SmallRyeConfigBuilder()
                .forClassLoader(getClass().getClassLoader())
                .addDefaultInterceptors()
                .setAddDefaultSources(false)
                .withSources(new FixMissingJavaNioJarFileProviderConfigSourceProvider())
                .build();

        this.greeting = config.getValue("hello.world.greeting", String.class);
    }
}
/** Helper class to avoid the attempt of SmallRye config to read jar files on Android */
class FixMissingJavaNioJarFileProviderConfigSourceProvider implements ConfigSourceProvider {
    Iterable<ConfigSource> configSources;
    @Override
    public Iterable<ConfigSource> getConfigSources(ClassLoader forClassLoader) {
        if (configSources == null) {
            Function<URL, ConfigSource> createSource = url -> {
                try {
                    return new PropertiesConfigSource(url);
                } catch(IOException e) {
                    throw new CompletionException(e);
                }
            };
            configSources = Stream.of("application.properties", "META-INF/microprofile-config.properties")
                    .map(name -> getClass().getClassLoader().getResource(name))
                    .map(createSource)
                    .collect(Collectors.toList());
        }
        return configSources;
    }
}