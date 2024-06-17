package com.example.contentsquare

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.delay

@Destination
@Composable
fun HomeScreen(
    navController: NavController
) {

    LaunchedEffect(key1 = Unit) {
        delay(5000)
        navController.navigate("ServiceSelectionScreenDestination")
    }

    Text(text = "home screen is getting called")
}