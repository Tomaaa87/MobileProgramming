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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddTimerScreen() {
    val duration = remember { mutableStateOf("") }
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val showErrors = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Add Timer", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create a new cooking timer. Example duration: 10 min or 00:10:00",
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = duration.value,
            onValueChange = { duration.value = it },
            label = { Text("Timer Duration") },
            placeholder = { Text("10 min") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrors.value && duration.value.isBlank()
        )
        if (showErrors.value && duration.value.isBlank()) {
            Text(text = "Duration is required", color = Color.Red, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Timer Title") },
            placeholder = { Text("Pasta") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrors.value && title.value.isBlank()
        )
        if (showErrors.value && title.value.isBlank()) {
            Text(text = "Title is required", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Timer Description") },
            placeholder = { Text("Boil water, then cook pasta...") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrors.value && description.value.isBlank()
        )
        if (showErrors.value && description.value.isBlank()) {
            Text(text = "Description is required", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { showErrors.value = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Timer")
        }
    }
}