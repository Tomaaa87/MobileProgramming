package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.cookingtimer.presentation.view_model.auth.registration.RegistrationViewModel
import com.example.cookingtimer.presentation.view_model.auth.registration.RegistrationUiState
import com.example.cookingtimer.presentation.view_model.auth.registration.RegistrationNavigationEvent
import com.example.cookingtimer.presentation.util.AuthValidators

@Composable
fun RegisterScreen(
    viewModel: RegistrationViewModel,
    onNavigate: () -> Unit,
    onLoginClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val nameError = AuthValidators.validateName(fullName)
    val emailError = AuthValidators.validateEmail(email)
    val passwordError = AuthValidators.validatePassword(password)
    val confirmPasswordError = if (password != confirmPassword) "Passwords do not match" else null

    val isRegisterEnabled = nameError == null && emailError == null && 
                           passwordError == null && confirmPasswordError == null

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is RegistrationNavigationEvent.Navigate -> onNavigate()
                is RegistrationNavigationEvent.NavigateBack -> { /* Do nothing */ }
            }
        }
    }

    when (uiState) {
        is RegistrationUiState.Loading -> LoadingScreen()
        is RegistrationUiState.Error -> ErrorScreen(
            message = (uiState as RegistrationUiState.Error).message,
            onRetry = { viewModel.resetUiState() }
        )
        else -> {
            StatelessRegisterScreen(
                fullName = fullName,
                onFullNameChange = { fullName = it },
                nameError = nameError,
                email = email,
                onEmailChange = { email = it },
                emailError = emailError,
                password = password,
                onPasswordChange = { password = it },
                passwordError = passwordError,
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                confirmPasswordError = confirmPasswordError,
                isRegisterEnabled = isRegisterEnabled,
                onRegisterClick = { viewModel.onRegisterClick(fullName, email, password) },
                onLoginClick = onLoginClick
            )
        }
    }
}

@Composable
fun StatelessRegisterScreen(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    nameError: String?,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String?,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordError: String?,
    isRegisterEnabled: Boolean,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var showErrors by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            ScreenHeader(
                title = "Register",
                subtitle = "Create an account to save your timers."
            )
        }

        item {
            FormCard {
                ValidatedTextField(
                    value = fullName,
                    onValueChange = onFullNameChange,
                    label = "Full Name",
                    placeholder = "John Doe",
                    error = if (showErrors) nameError else null
                )

                Spacer(modifier = Modifier.height(10.dp))

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

                Spacer(modifier = Modifier.height(10.dp))

                ValidatedTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = "Confirm Password",
                    placeholder = "Confirm password",
                    visualTransformation = PasswordVisualTransformation(),
                    error = if (showErrors) confirmPasswordError else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showErrors = true
                        if (isRegisterEnabled) onRegisterClick()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Account")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Already have an account? Login")
                }
            }
        }
    }
}
