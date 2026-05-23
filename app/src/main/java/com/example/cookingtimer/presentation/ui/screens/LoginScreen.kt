package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookingtimer.presentation.ui.components.FormCard
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.ValidatedTextField
import com.example.cookingtimer.presentation.ui.components.LoadingScreen
import com.example.cookingtimer.presentation.ui.components.ErrorScreen
import com.example.cookingtimer.presentation.view_model.auth.login.LoginViewModel
import com.example.cookingtimer.presentation.view_model.auth.login.LoginUiState
import com.example.cookingtimer.presentation.view_model.auth.login.LoginNavigationEvent
import com.example.cookingtimer.presentation.util.AuthValidators

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigate: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val emailError = AuthValidators.validateEmail(email)
    val passwordError = AuthValidators.validatePassword(password)
    val isLoginEnabled = emailError == null && passwordError == null

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LoginNavigationEvent.Navigate -> onNavigate()
                is LoginNavigationEvent.NavigateBack -> { /* Do nothing */ }
            }
        }
    }

    when (uiState) {
        is LoginUiState.Loading -> LoadingScreen()
        is LoginUiState.Error -> ErrorScreen(
            message = (uiState as LoginUiState.Error).message,
            onRetry = { viewModel.resetUiState() }
        )
        else -> {
            StatelessLoginScreen(
                email = email,
                onEmailChange = { email = it },
                emailError = emailError,
                password = password,
                onPasswordChange = { password = it },
                passwordError = passwordError,
                isLoginEnabled = isLoginEnabled,
                onLoginClick = { viewModel.onLoginClick(email, password) },
                onRegisterClick = onRegisterClick
            )
        }
    }
}

@Composable
fun StatelessLoginScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String?,
    isLoginEnabled: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var showErrors by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        ScreenHeader(
            title = "Login",
            subtitle = "Welcome back! Please enter your details."
        )

        FormCard {
            ValidatedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                placeholder = "example@email.com",
                error = if (showErrors) emailError else null
            )

            Spacer(modifier = Modifier.height(10.dp))

            ValidatedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Password",
                placeholder = "Enter password",
                visualTransformation = PasswordVisualTransformation(),
                error = if (showErrors) passwordError else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    showErrors = true
                    if (isLoginEnabled) onLoginClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Don't have an account? Register")
            }
        }
    }
}
