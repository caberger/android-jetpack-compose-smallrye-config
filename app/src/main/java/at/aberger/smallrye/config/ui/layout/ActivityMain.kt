package at.aberger.smallrye.config.ui.layout

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import at.aberger.smallrye.config.HelloWorldModel
import at.aberger.smallrye.config.HelloWorldViewModel
import at.aberger.smallrye.config.ui.theme.SmallryeConfigTheme

class ActivityMain {
    fun contentView(activity: ComponentActivity): ComposeView {

        val contentView = ComposeView(activity)

        contentView.setContent {
            SmallryeConfigTheme {
                val viewModel = ViewModelProvider(activity)[HelloWorldViewModel::class.java]
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(viewModel)
                }
            }
        }
        return contentView
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val viewModel = HelloWorldViewModel()
    SmallryeConfigTheme {
        Greeting(viewModel)
    }
    viewModel.next { it.greeting = "Demo Preview" }
}


@Composable
fun Greeting(viewModel: HelloWorldViewModel, modifier: Modifier = Modifier) {
    val state = viewModel.store.subscribeAsState(initial = HelloWorldModel())
    Column {
        Text(
            text = state.value.greeting,
            modifier = modifier
        )
        Button(onClick = { viewModel.next { it.greeting = "greeting changed" } } ) {
            Text("ok")
        }
    }
}
