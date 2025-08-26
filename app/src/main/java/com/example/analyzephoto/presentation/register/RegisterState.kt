package com.example.analyzephoto.presentation.register

import com.example.analyzephoto.presentation.base.BaseViewState

data class RegisterState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registerSuccess: Boolean = false,
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = ""
) : BaseViewState