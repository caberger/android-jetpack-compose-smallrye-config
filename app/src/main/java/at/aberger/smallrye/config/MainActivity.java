package at.aberger.smallrye.config;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import at.aberger.smallrye.config.ui.layout.ActivityMain;

public class MainActivity extends ComponentActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classPathDemo();
        var viewModel = new ViewModelProvider(this).get(HelloWorldViewModel.class);
        viewModel.store.subscribe(model -> Log.i(TAG, String.format("greeting changed: %s", model.greeting)));
        setContentView(new ActivityMain().contentView(this));
        var config = new ApplicationConfiguration();
        viewModel.store.onNext(new HelloWorldModel(config.greeting));
    }
    void classPathDemo() {
        var utils = new ClassPathUtils();
        try {
            utils.consumeAsStreams(getClass().getClassLoader(), "META-INF/microprofile-config.properties", inputStream -> {
                var properties = new Properties();
                try {
                    properties.load(inputStream);
                    properties.forEach((key, value) -> Log.i(TAG, String.format("%s -> %s", key, value)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
class ClassPathUtils {
    void consumeAsStreams(ClassLoader cl, String resource, Consumer<InputStream> consumer) throws IOException {
        final var resources = cl.getResources(resource);
        while (resources.hasMoreElements()) {
            consumeStream(cl, resources.nextElement(), consumer);
        }
    }
    void consumeStream(ClassLoader cl, URL url, Consumer<InputStream> consumer) throws IOException {
        readStream(url, is -> {
            consumer.accept(is);
            return null;
        });
    }
    <R> R readStream(URL url, Function<InputStream, R> function) throws IOException {
        var urlConnection = url.openConnection();
        urlConnection.setUseCaches(false);
        try (var is = urlConnection.getInputStream()) {
            return function.apply(is);
        }
    }
 }
