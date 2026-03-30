package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cookingtimer.presentation.ui.components.FormCard
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.ValidatedTextField

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showErrors = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                androidx.compose.material3.Text("Login")
            }
        }
    }
}