package com.example.contentsquare

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
sealed class LandingPageEvent : Event.UiEvent() {
    @Immutable
    object OnTruecallerLogin : LandingPageEvent()
    @Immutable
    data class TrackCategoryClickedEvent(val action: String) : LandingPageEvent()
}

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val housingPrefStore: HousingPrefStore
) :
    EventHandlerViewModel<LandingPageEvent>() {

    private val _wasLoaded = MutableStateFlow(false)
    var loaded:StateFlow<Boolean> = _wasLoaded.asStateFlow()

    private val _isAnimationCompleted = MutableStateFlow(false)
    var isAnimationCompleted2:StateFlow<Boolean> = _isAnimationCompleted.asStateFlow()

    private val _isLoginAvailable = MutableStateFlow(true)
    var isLoginAvailable:StateFlow<Boolean> = _isLoginAvailable.asStateFlow()

    private val _isLoginAddDetailsAvailable2 = MutableStateFlow(true)
    var isLoginAddDetailsAvailable:StateFlow<Boolean> = _isLoginAddDetailsAvailable2.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO){
//            appAnalytics.trackWelcomeScreenEvent()
        }
    }


    fun trackScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.v("trackScreentrackScreen", "roit garg")
//            contentSquareAnalytics.trackScreen(ScreensTracking.WELCOME_SCREEN)
        }
    }

    override fun handleEvent(event: LandingPageEvent) {
        when (event) {

            is LandingPageEvent.OnTruecallerLogin -> {
                trueCallerSuccessfullyLoggedIn()
            }
            is LandingPageEvent.TrackCategoryClickedEvent -> {
//                appAnalytics.trackCategoryClickedEvent(action = event.action)
            }
        }
    }
    fun setUiLoadedToTrue(){
        _wasLoaded.update {
            true
        }
    }
    private  var isLoginAvailableJob : Job?= null
    fun isLoginAvailable() {
        isLoginAvailableJob?.cancel()
        isLoginAvailableJob =    viewModelScope.launch (Dispatchers.IO){
            if(isActive.not())
                return@launch


            val loginAuthToken = async {
                housingPrefStore.readValue<String>(PreferencesKeys.USER_SIGN_IN_DETAILS)
                    .firstOrNull()
            }

            val trueCallerLoginAuthToken = async {
                housingPrefStore.readValue<String>(PreferencesKeys.TRUECALLER_VALIDATE_DATA_RESPONSE)
                    .firstOrNull()
            }

            if(isActive.not())
                return@launch

            val isUserLoggedIn =
                !loginAuthToken.await().isNullOrEmpty() || !trueCallerLoginAuthToken.await().isNullOrEmpty()
            _isLoginAvailable.update {
                !isUserLoggedIn && !SessionDataManager.skipLogin
            }

        }
    }

    fun setAnimationCompleted() {
        _isAnimationCompleted.update {
            true
        }
    }

    fun isAddDetailsScreenAvailable() {
        viewModelScope.launch(Dispatchers.IO) {
            val   isAddDetailsScreenAvailable = async (Dispatchers.IO) {
                    housingPrefStore.readValue<String>(PreferencesKeys.IS_ADD_DETAILS_SCREEN_REQUIRED)
                        .firstOrNull()
            }

            val loginAuthToken = async  (Dispatchers.IO){
                housingPrefStore.readValue<String>(PreferencesKeys.USER_SIGN_IN_DETAILS)
                    .firstOrNull()
            }

            val trueCallerLoginAuthToken = async (Dispatchers.IO) {
                housingPrefStore.readValue<String>(PreferencesKeys.TRUECALLER_VALIDATE_DATA_RESPONSE)
                    .firstOrNull()
            }

            val isUserLoggedIn = async (Dispatchers.IO) {
                !loginAuthToken.await().isNullOrEmpty() || !trueCallerLoginAuthToken.await().isNullOrEmpty()

            }

            _isLoginAddDetailsAvailable2.update {
                false
            }

        }
    }

    fun trueCallerSuccessfullyLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            housingPrefStore.storeValue(PreferencesKeys.IS_LOGIN_REQUIRED, false.toString())
        }
    }

    fun showNotificationPermissionPopup(context: Context) {
        viewModelScope.launch {
        }
    }
}
