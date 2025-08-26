package com.example.analyzephoto.presentation.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<INTENT : BaseViewIntent, STATE : BaseViewState> : ViewModel() {
    private val _state = MutableStateFlow<STATE?>(null)
    val state: StateFlow<STATE?> = _state.asStateFlow()

    protected fun setState(state: STATE) {
        _state.value = state
    }

    abstract fun processIntent(intent: INTENT)
}