package at.aberger.smallrye.config;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import at.aberger.smallrye.config.ui.layout.ActivityMain;

public class MainActivity extends ComponentActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            consumeAsInputStreams(getClass().getClassLoader(), "META-INF/microprofile-config.properties", inputStream -> {
                var properties = new Properties();
                try {
                    properties.load(inputStream);
                    properties.forEach((key, value) -> Log.i(TAG, String.format("%s -> %s", key, value)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var viewModel = new ViewModelProvider(this).get(HelloWorldViewModel.class);
        setContentView(new ActivityMain().contentView(this));
        var config = new ApplicationConfiguration();
        viewModel.model.onNext(new HelloWorldModel(config.greeting));
    }
    void consumeAsInputStreams(ClassLoader cl, String resource, Consumer<InputStream> consumer) throws IOException {
        final var resources = cl.getResources(resource);
        while (resources.hasMoreElements()) {
            final var res = resources.nextElement();
            try (var is = res.openStream()) {
                consumer.accept(is);
            }
        }
    }
}
