package at.aberger.smallrye.config.ui.layout

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import at.aberger.smallrye.config.HelloWorldModel
import at.aberger.smallrye.config.HelloWorldViewModel
import at.aberger.smallrye.config.ui.theme.SmallryeConfigTheme

class ActivityMain {
    fun contentView(
        activity: ComponentActivity,
        viewModel: HelloWorldViewModel
    ): ComposeView {
        var contentView = ComposeView(activity)
        contentView.setContent {
            SmallryeConfigTheme {
                val model = viewModel.model.subscribeAsState(initial = HelloWorldModel()).value
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(model.greeting)
                }
            }
        }
        return contentView
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmallryeConfigTheme {
        Greeting("Android")
    }
}

@Composable
fun Greeting(greeting: String, modifier: Modifier = Modifier) {
    Text(
        text = greeting,
        modifier = modifier
    )
}
