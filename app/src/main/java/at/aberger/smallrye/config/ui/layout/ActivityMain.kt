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
    fun contentView(
        activity: ComponentActivity
    ): ComposeView {
        val viewModel = ViewModelProvider(activity).get(HelloWorldViewModel::class.java)
        val contentView = ComposeView(activity)
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

fun onClick() {
    TODO("Not yet implemented")
}

@Composable
fun Greeting(greeting: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = greeting,
            modifier = modifier
        )
        Button(onClick = { onClick() }) {
            Text("ok")
        }
    }
}
