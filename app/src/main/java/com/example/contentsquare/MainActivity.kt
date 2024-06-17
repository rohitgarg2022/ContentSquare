package com.example.contentsquare

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.contentsquare.android.Contentsquare
import com.contentsquare.android.compose.analytics.TriggeredOnResume
import com.example.contentsquare.destinations.HomeScreenDestination
import com.example.contentsquare.destinations.HomesScreenDestination
//import com.contentsquare.android.Contentsquare
//import com.contentsquare.android.compose.analytics.TriggeredOnResume
import com.example.contentsquare.ui.theme.ContentSquareTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.composable
import dagger.hilt.android.AndroidEntryPoint
//import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            Log.v("rohit garg", "updatingValueupdatingValue  = ${SessionDataManager.testingVariable}")

            ContentSquareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting("Android")
                }

                NavHost(
                    navController = navController,
                    startDestination = "ServiceSelectionScreenDestination"
                ) {
                    composable(
                        route = "ServiceSelectionScreenDestination"
                    ) {
                        ServiceSelectionScreen()
                    }
                    composable(route = "HomesScreenDestination") {
                        HomesScreen(navController = navController)
                    }
                    composable(
                        route = "HomeScreenDestination") {
                        HomeScreen(navController = navController)
                    }

                }

            }

            SessionDataManager.testingVariable.onEach {
                Log.v("rohit garg", "updatingValueupdatingValue inside  = $it")
            }


            }
        }
    }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContentSquareTheme {
        Greeting("Android")
    }
}



@Destination
@Composable
fun HomesScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {

    val scope = rememberCoroutineScope()

    SessionDataManager.testingVariable.onEach {
        Log.v("rohit garg", "updatingValueupdatingValue inside  = $it")
    }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            delay(2000)
            SessionDataManager.updateTestingVariable()
        }
    }

    val state = remember {
        mutableStateOf("abcd")
    }


    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val isAnimationComplete by viewModel.isAnimationCompleted2.collectAsStateWithLifecycle(LocalLifecycleOwner.current)
    val isLoginAvailable by viewModel.isLoginAvailable.collectAsStateWithLifecycle(LocalLifecycleOwner.current)
    val isLoginAddDetailsAvailable by viewModel.isLoginAddDetailsAvailable.collectAsStateWithLifecycle(LocalLifecycleOwner.current)


    ContentSquareTrackScreen {
        Log.v("rohit garg", "TriggeredOnResume is called")
        Contentsquare.send("screen1")
    }



     DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
         Log.v("rohit garg", lifecycle.currentState.toString())
        val observer = LifecycleEventObserver { owner, event ->
            Log.v("rohit garg", event.toString())
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.updateLoginAvailable()
                    viewModel.updateLoginDetailsAvailable()
                }

                else -> {}
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }

    }

    Column {
        showTextComponent(
            isLoginAvailable,
            isAnimationComplete,
            isLoginAddDetailsAvailable,
            viewModel::setAnimationCompleted
        )
        Spacer(modifier = Modifier)
        showTextComponent(
            isLoginAvailable,
            isAnimationComplete,
            isLoginAddDetailsAvailable,
            viewModel::setAnimationCompleted
        )
    }
}

@Composable
fun showTextComponent(
    isLoginAvailable: Boolean,
    isAnimationCompleted: Boolean,
    isLoginDetailsAvailble: Boolean,
    setAnimationCompleted: () -> Unit
) {

    val scope = rememberCoroutineScope()
    Log.v("rohit garg", "line 148 called")



    LaunchedEffect(key1 = Unit) {
        scope.launch {
            delay(10000)
            setAnimationCompleted()
        }
    }

    Row {
        Text(text = isLoginAvailable.toString())
        Text(text = isAnimationCompleted.toString())
        Text(text = isLoginDetailsAvailble.toString())
    }
}

@Composable
fun ContentSquareTrackScreen(
    executeScreenTracking: () -> Unit
) {
    TriggeredOnResume {
        executeScreenTracking()
    }
}

object SessionDataManager {
    private var _testingVariable = MutableStateFlow("default")
    val testingVariable = _testingVariable.asStateFlow()

    private var _skipLogin = false

    fun updateTestingVariable() {
        Log.v("rohit garg", "updateTestingVariable is called")
        _testingVariable.update {
            "testing done"
        }
    }

    val skipLogin get() = _skipLogin
    fun setLoginSkipped() {
        _skipLogin = true
    }
}