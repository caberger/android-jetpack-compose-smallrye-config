package at.aberger.smallrye.config.ui.layout

import android.util.Log
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
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject

class ActivityMain {
    fun contentView(
        activity: ComponentActivity
    ): ComposeView {
        val viewModel = ViewModelProvider(activity).get(HelloWorldViewModel::class.java).model
        fun onClick() {
            viewModel.onNext(HelloWorldModel("new Greeting"))
        }
        val contentView = ComposeView(activity)
        contentView.setContent {
            SmallryeConfigTheme {
                val model = viewModel.subscribeAsState(initial = HelloWorldModel()).value
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(model.greeting, { onClick() })
                }
            }
        }
        return contentView
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val tag: String? = ActivityMain::class.simpleName;
    SmallryeConfigTheme {
        Greeting("Android", { Log.i(tag, "onclick()")})
    }
}


@Composable
fun Greeting(greeting: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = greeting,
            modifier = modifier
        )
        Button(onClick = { onClick() } ) {
            Text("ok")
        }
    }
}
