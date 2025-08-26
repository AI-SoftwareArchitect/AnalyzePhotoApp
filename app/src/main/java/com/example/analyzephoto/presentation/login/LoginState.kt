package com.example.analyzephoto.presentation.login

import com.example.analyzephoto.presentation.base.BaseViewState

// presentation/login/LoginState.kt
data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
) : BaseViewState