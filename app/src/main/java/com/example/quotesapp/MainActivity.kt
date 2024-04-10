package com.example.quotesapp

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.quotesapp.screens.QuoteDetail
import com.example.quotesapp.screens.QuoteListScreen
import com.example.quotesapp.ui.theme.QuotesAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            DataManager.loadAssetsFromFile(applicationContext)
        }
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    if (DataManager.isDataLoaded.value) {
        if (DataManager.currentPage.value == Pages.LISTING) {
            QuoteListScreen(data = DataManager.data) {
                DataManager.switchPages(it)
            }
        } else {
            DataManager.currentQuote?.let { QuoteDetail(quote = it) }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(1f)
        ) {
            Text(
                text = "Loading.......",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

enum class Pages {
    LISTING,
    DETAIL
}

@Composable
fun LaunchEffectComposable() {
    val counter = remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = Unit) {
        Log.d("Launch Effect composable", "Started......")
        try {
            for (i in 0 until 10) {
                counter.value++
                delay(100)
            }
        } catch (e: Exception) {
            Log.d("Launch Effect composable", "Exception ${e.message.toString()}")
        }
    }
    var text = "Counter is running ${counter.value}"
    if (counter.value == 10) {
        text = "Counter stopped"
    }
    Text(text = text)

}

@Composable
fun coroutineScopeComposable() {
    val counter = remember {
        mutableStateOf(0)
    }
    var scope = rememberCoroutineScope()

    var text = "Counter is running ${counter.value}"
    if(counter.value == 10) {
        text = "Counter stopped"
    }
    Column {
        Text(text = text)
        Button(onClick = {
            scope.launch {
                Log.d("Coroutinescopecomposable","Started.....")
                try {
                    for(i in 1 until 10) {
                        counter.value++
                        delay(1000)
                    }
                } catch (e: Exception) {
                    Log.d("Coroutinescopecomposable","Exception ${e.message.toString()}")
                }
            }
        }) {
            Text(text = text)
        }
    }
}

@Composable
fun AppState() {
    val counter = remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = Unit) {
        delay(2000)
        counter.value = 10
    }
    Counter(counter.value)
}

@Composable
fun Counter(value: Int) {
    val state = rememberUpdatedState(newValue = value)
    LaunchedEffect(key1 = Unit) {
        delay(5000)
        Log.d("Deb", state.value.toString())
    }
    Text(text = value.toString())
}

@Composable
fun AppDisposable() {
    MediaComposable()
}

@Composable
fun MediaComposable() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val mediaPlayer = MediaPlayer.create(context,R.raw.)
        mediaPlayer.start()

        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}
