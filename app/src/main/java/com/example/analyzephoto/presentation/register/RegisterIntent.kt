package com.example.analyzephoto.presentation.register

import com.example.analyzephoto.presentation.base.BaseViewIntent

sealed class RegisterIntent : BaseViewIntent {
    data class Register(val username: String, val password: String, val confirmPassword: String) : RegisterIntent()
    data object NavigateToLogin : RegisterIntent()
}