package com.example.analyzephoto.presentation.login

// presentation/login/LoginViewModel.kt
@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<LoginIntent, LoginState>() {

    override fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> login(intent.username, intent.password)
            LoginIntent.NavigateToRegister -> navigateToRegister()
        }
    }

    private fun login(username: String, password: String) {
        setState(LoginState(isLoading = true))
        viewModelScope.launch {
            delay(1000) // Simulate network call
            if (username.isNotEmpty() && password.isNotEmpty()) {
                setState(LoginState(loginSuccess = true))
            } else {
                setState(LoginState(error = "Username or password cannot be empty"))
            }
        }
    }

    private fun navigateToRegister() {
        // Navigation handled in Screen
    }
}