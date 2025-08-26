package com.example.analyzephoto.presentation.login

// presentation/login/LoginScreen.kt
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.loginSuccess) {
        if (state.loginSuccess) {
            onLoginSuccess()
        }
    }

    PhotoEditorTheme {
        Surface(color = Black) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GoldGradientIcon()
                Spacer(modifier = Modifier.height(32.dp))
                UsernameField { username ->
                    viewModel.processIntent(LoginIntent.Login(username, ""))
                }
                Spacer(modifier = Modifier.height(16.dp))
                PasswordField { password ->
                    viewModel.processIntent(LoginIntent.Login("", password))
                }
                Spacer(modifier = Modifier.height(24.dp))
                LoginButton(
                    isLoading = state.isLoading,
                    onLoginClick = { username, password ->
                        viewModel.processIntent(LoginIntent.Login(username, password))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                RegisterNavigationButton { onNavigateToRegister() }
            }
        }
    }
}

@Composable
private fun GoldGradientIcon() {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(
                brush = GoldGradient,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = "Camera Icon",
            tint = Black,
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
private fun UsernameField(onUsernameChange: (String) -> Unit) {
    var username by remember { mutableStateOf("") }
    OutlinedTextField(
        value = username,
        onValueChange = {
            username = it
            onUsernameChange(it)
        },
        label = { Text("Username", color = Gold) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Gold,
            unfocusedTextColor = Gold,
            focusedContainerColor = Black,
            unfocusedContainerColor = Black,
            focusedLabelColor = Gold,
            unfocusedLabelColor = Gold,
            focusedLeadingIconColor = Gold,
            unfocusedLeadingIconColor = Gold,
            focusedTrailingIconColor = Gold,
            unfocusedTrailingIconColor = Gold,
            focusedBorderColor = Gold,
            unfocusedBorderColor = Gold,
        )
    )
}