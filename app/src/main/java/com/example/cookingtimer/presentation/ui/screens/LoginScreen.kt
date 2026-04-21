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
import com.example.cookingtimer.presentation.ui.components.FormCard
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.ValidatedTextField

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }
    var loginAttempts by remember { mutableIntStateOf(0) }
    var rememberMe by remember { mutableStateOf(false) }
        
    val isFormValid by remember {
        derivedStateOf { email.isNotBlank() && password.isNotBlank() }
    }
    val doubleAttempts by remember {
        derivedStateOf { loginAttempts * 2 }
    }

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
                onValueChange = { email = it },
                label = "Email",
                placeholder = "example@email.com",
                error = if (showErrors && email.isBlank()) "Email is required" else null
            )

            Spacer(modifier = Modifier.height(10.dp))

            ValidatedTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Enter password",
                visualTransformation = PasswordVisualTransformation(),
                error = if (showErrors && password.isBlank()) "Password is required" else null
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text("Remember me")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = if (isFormValid) "Form is valid ✅" else "Form is invalid ❌")
            Text("Login attempts: $loginAttempts")
            Text("Double attempts (derived): $doubleAttempts", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    showErrors = true
                    loginAttempts++
                    if (isFormValid) onLoginClick("User", email)
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