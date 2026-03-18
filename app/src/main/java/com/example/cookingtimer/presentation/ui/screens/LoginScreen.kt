package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showErrors = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Login", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            placeholder = { Text("example@email.com") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrors.value && email.value.isBlank()
        )
        if (showErrors.value && email.value.isBlank()) {
            Text(text = "Email is required", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            placeholder = { Text("Enter password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = showErrors.value && password.value.isBlank()
        )
        if (showErrors.value && password.value.isBlank()) {
            Text(text = "Password is required", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showErrors.value = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}
