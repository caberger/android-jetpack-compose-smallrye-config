package at.aberger.smallrye.config;
import android.os.Bundle;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import at.aberger.smallrye.config.ui.layout.ActivityMain;

public class MainActivity extends ComponentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var viewModel = new ViewModelProvider(this).get(HelloWorldViewModel.class);
        setContentView(new ActivityMain().contentView(this));
        var config = new ApplicationConfiguration();
        viewModel.model.onNext(new HelloWorldModel(config.greeting));
    }
}
