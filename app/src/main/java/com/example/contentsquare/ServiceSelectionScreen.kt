package com.example.contentsquare

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.contentsquare.android.compose.analytics.TriggeredOnResume
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ServiceDetails(
   val mainText: String, val subText: String, val image: String
)


@Composable
private fun FreeTag(serviceId: String) {
    Text(text = serviceId.toString())
}

@Composable
private fun ServiceCardComposable(serviceDetails: ServiceDetails, onPress: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(30.dp))
            Column {
                Row {
                    Text(
                        text = "mainText",
                    )
                    FreeTag(serviceDetails.mainText)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "subtext",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun TopAsset(
    wasLoaded: Boolean,
    delay: Int,
    durationMillis: Int
) {
    Column(modifier = Modifier.fillMaxHeight(0.368f)) {
        Text(text = "TopAsset")
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)) {
            ShouldSlideHorizontallyFadeAnimation(
                wasLoaded = wasLoaded,
                delay = delay + 200,
                posDiv = 2,
                durationMillis = durationMillis
            ) {
                Text(
                    text = "LANDING_PAGE_HEADER_1",
                )
            }

            ShouldSlideHorizontallyFadeAnimation(
                wasLoaded = wasLoaded,
                delay = delay + 200,
                posDiv = 2,
                durationMillis = durationMillis
            ) {
                Text(
                    text = "LANDING_PAGE_HEADER_2",
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun ShouldSlideHorizontallyFadeAnimation(
    wasLoaded: Boolean,
    delay: Int,
    posDiv: Int,
    durationMillis: Int,
    content: @Composable () -> Unit
) {
    if (wasLoaded.not()) {
    } else {
        content.invoke()
    }
}

@SuppressLint("HardwareIds")
@Composable
private fun BottomAsset(
    viewModel: LandingPageViewModel,
    wasLoaded: Boolean,
    isAnimationCompleted: Boolean,
    isLoginAvailable: Boolean,
    isLoginAddDetailsAvailable: Boolean,
    delay: Int,
    durationMillis: Int,
    sendUiLoadedEvent: () -> Unit,
    onTrueCallerLogin: () -> Unit,
) {

    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
        }
    }
    val showAnimation = remember(wasLoaded) {
        wasLoaded.not()
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, fill = false)
        ) {
            ShouldFadeSlideVerticalAnimatedView(
                showAnimation = wasLoaded.not(),
                delay = delay + 200,
                durationMillis = durationMillis
            ) {
                ServiceCardComposable(serviceDetails = ServiceDetails(
                    mainText = "kks", subText = "9", image = "9"
                ), onPress = {
                    if (isAnimationCompleted) {
                    }
                })
            }
            ShouldFadeSlideVerticalAnimatedView(
                showAnimation = wasLoaded.not(),
                delay = delay + 400,
                durationMillis = durationMillis
            ) {
                ServiceCardComposable(serviceDetails = ServiceDetails(
                    mainText = "kks", subText = "9", image = "9"
                ), onPress = {
                    if (isAnimationCompleted) {
                        viewModel.showNotificationPermissionPopup(context)
                    }
                })
            }
            ShouldFadeSlideVerticalAnimatedView(
                showAnimation = wasLoaded.not(),
                delay = delay + 600,
                durationMillis = durationMillis
            ) {
                ServiceCardComposable(serviceDetails = ServiceDetails(
                    mainText = "kks", subText = "9", image = "9"
                ), onPress = {
                    if (isAnimationCompleted) {
                        viewModel.showNotificationPermissionPopup(context)
                    }
                })
            }
            ShouldFadeSlideVerticalAnimatedView(
                showAnimation = wasLoaded.not(),
                delay = delay + 800,
                durationMillis = durationMillis
            ) {
                ServiceCardComposable(serviceDetails = ServiceDetails(
                    mainText = "kks", subText = "9", image = "9"
                ), onPress = {
                })
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ShouldFadeSlideVerticalAnimatedView(
    showAnimation: Boolean, delay: Int, durationMillis: Int, content: @Composable () -> Unit
) {
    if (showAnimation) {
    } else {
        content.invoke()
    }
}

@RootNavGraph(start = true)
@Destination
@Composable
fun ServiceSelectionScreen(
    viewModel: LandingPageViewModel = hiltViewModel(),
    isWhatsAppVerified: Boolean = false
) {
    val loaded by viewModel.loaded.collectAsStateWithLifecycle()
    val isAnimationComplete by viewModel.isAnimationCompleted2.collectAsStateWithLifecycle()
    val isLoginAvailable by viewModel.isLoginAvailable.collectAsStateWithLifecycle()
    val isLoginAddDetailsAvailable by viewModel.isLoginAddDetailsAvailable.collectAsStateWithLifecycle()

    TriggeredOnResume {
        Log.v("rohit garg", "TriggeredOnResumeTriggeredOnResume")
        viewModel.trackScreen()
    }

    LaunchedEffect(key1 = Unit) {
    }
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.isLoginAvailable()
                    viewModel.isAddDetailsScreenAvailable()
                }

                else -> {}
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LandingPageScreen(
        loaded = loaded,
        isAnimationCompleted = isAnimationComplete,
        onAnimationComplete = viewModel::setAnimationCompleted
    )
    Column {
        TopAsset(wasLoaded = isAnimationComplete, delay = 1500, durationMillis = 1500)
        MidAsset(wasLoaded = isAnimationComplete, delay = 1500, durationMillis = 1500)
        BottomAsset(
            viewModel,
            wasLoaded = isAnimationComplete,
            isAnimationCompleted = isAnimationComplete,
            isLoginAvailable = isLoginAvailable,
            isLoginAddDetailsAvailable = isLoginAddDetailsAvailable,
            delay = 1500,
            durationMillis = 800,
            sendUiLoadedEvent = viewModel::setUiLoadedToTrue,
            onTrueCallerLogin = viewModel::trueCallerSuccessfullyLoggedIn,
        )
    }
}

@Composable
fun MidAsset(
    wasLoaded: Boolean, delay: Int, durationMillis: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        ShouldFadeInAnimation(wasLoaded = wasLoaded, delay + 500, durationMillis) {
            Text(
                text = "LANDING_PAGE_TITLE",
            )
        }

        ShouldFadeInAnimation(wasLoaded = wasLoaded, delay + 500, durationMillis) {
            Text(
                text = "LANDING_PAGE_SUBTITLE",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ShouldFadeInAnimation(
    wasLoaded: Boolean,
    delay: Int,
    durationMillis: Int,
    content: @Composable () -> Unit
) {
    if (wasLoaded.not()) {
    } else {
        content.invoke()
    }
}

@Composable
fun LandingPageScreen(
    loaded: Boolean,
    isAnimationCompleted: Boolean,
    onAnimationComplete: () -> Unit
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val density = LocalDensity.current.density
    val coroutineScope = rememberCoroutineScope()
    val characterAnimProgress = remember {
        mutableStateOf(0.0f)
    }
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            delay(1500)
            characterAnimProgress.value = 0.7f
        }
    }

    val animationPadding = remember(key1 = screenHeightDp, key2 = density) {
        derivedStateOf {
            val screenHeightPixels = (screenHeightDp * density).toInt()
            if (screenHeightPixels < 1190) 22.dp else 0.dp
        }

    }

    val animationComplete by remember {
        derivedStateOf {
//            characterAnimProgress.progress >= 0.6f
        }
    }

    if (characterAnimProgress.value >= 0.6f) {
        onAnimationComplete.invoke()
    }

}
