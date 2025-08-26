package com.example.analyzephoto.presentation.login

import com.example.analyzephoto.presentation.base.BaseViewIntent

// presentation/login/LoginIntent.kt
sealed class LoginIntent : BaseViewIntent {
    data class Login(val username: String, val password: String) : LoginIntent()
    object NavigateToRegister : LoginIntent()
}