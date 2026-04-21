package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookingtimer.presentation.ui.components.FormCard
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.ValidatedTextField

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String) -> Unit,
    onLoginClick: () -> Unit
) {

    var email by rememberSaveable { mutableStateOf("") }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }


    var step by remember { mutableIntStateOf(1) }
    var termsAccepted by remember { mutableStateOf(false) }


    val fullName by remember { derivedStateOf { "$firstName $lastName".trim() } }
    

    val isFormValid by remember {
        derivedStateOf {
            email.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank() && 
            username.isNotBlank() && phoneNumber.isNotBlank() && password.isNotBlank() && termsAccepted
        }
    }


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

        if (fullName.isNotBlank()) {
            item {
                Text(
                    text = "Welcome, $fullName!",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        item {
            FormCard {
                Text("Registration Step: $step", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                
                Spacer(modifier = Modifier.height(8.dp))

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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it }
                    )
                    Text("Accept Terms & Conditions", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { 
                        showErrors = true
                        if (isFormValid) {
                            step++
                            onRegisterClick(firstName, email)
                        }
                    },
                    enabled = isFormValid || !showErrors,
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
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}