package com.example.analyzephoto.presentation.register

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun processIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Register -> register(intent.username, intent.password, intent.confirmPassword)
            RegisterIntent.NavigateToLogin -> navigateToLogin()
        }
    }

    private fun register(username: String, password: String, confirmPassword: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            delay(1000) // Simulate network call

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Tüm alanlar doldurulmalıdır"
                )
                return@launch
            }

            if (password != confirmPassword) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Şifreler eşleşmiyor"
                )
                return@launch
            }

            if (password.length < 6) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Şifre en az 6 karakter olmalıdır"
                )
                return@launch
            }

            _state.value = _state.value.copy(
                isLoading = false,
                registerSuccess = true
            )
        }
    }

    private fun navigateToLogin() {
        // Navigation handled in screen
    }
}