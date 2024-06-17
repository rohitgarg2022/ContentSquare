package com.example.contentsquare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

): ViewModel() {
    private val _isAnimationCompleted = MutableStateFlow(false)
    var isAnimationCompleted2: StateFlow<Boolean> = _isAnimationCompleted.asStateFlow()


    private val _isLoginAvailable = MutableStateFlow(true)
    var isLoginAvailable:StateFlow<Boolean> = _isLoginAvailable.asStateFlow()

    private val _isLoginAddDetailsAvailable2 = MutableStateFlow(false)
    var isLoginAddDetailsAvailable:StateFlow<Boolean> = _isLoginAddDetailsAvailable2.asStateFlow()

    fun setAnimationCompleted() {
        _isAnimationCompleted.update {
            true
        }
    }

    fun updateLoginAvailable() {
        _isLoginAvailable.update {
            true
        }
    }

    fun updateLoginDetailsAvailable() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(12000)
            _isLoginAddDetailsAvailable2.update {
                true
            }
        }

    }

}