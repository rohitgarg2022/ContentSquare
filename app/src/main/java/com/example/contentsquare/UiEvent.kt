package com.example.contentsquare


import androidx.lifecycle.ViewModel

sealed class Event {
    abstract class UiEvent() : Event()
}

abstract class EventHandlerViewModel<in EVENT : Event.UiEvent>() : ViewModel() {
    abstract fun handleEvent(event: EVENT)
}
