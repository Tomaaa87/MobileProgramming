package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun RegisterScreen() {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        ScreenHeader(
            title = "Register",
            subtitle = "Create an account to save your timers."
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
                value = firstName,
                onValueChange = { firstName = it },
                label = "First Name",
                placeholder = "Tom",
                error = if (showErrors && firstName.isBlank()) "First name is required" else null
            )

            Spacer(modifier = Modifier.height(10.dp))

            ValidatedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = "Last Name",
                placeholder = "Smith",
                error = if (showErrors && lastName.isBlank()) "Last name is required" else null
            )

            Spacer(modifier = Modifier.height(10.dp))

            ValidatedTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                placeholder = "tom87",
                error = if (showErrors && username.isBlank()) "Username is required" else null
            )

            Spacer(modifier = Modifier.height(10.dp))

            ValidatedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number",
                placeholder = "5551234567",
                error = if (showErrors && phoneNumber.isBlank()) "Phone number is required" else null
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
                Text("Create Account")
            }
        }
    }
}