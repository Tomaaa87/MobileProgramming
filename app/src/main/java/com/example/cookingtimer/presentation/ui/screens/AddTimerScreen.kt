package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookingtimer.presentation.ui.components.DropdownField
import com.example.cookingtimer.presentation.ui.components.FormCard
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.TimerCard
import com.example.cookingtimer.presentation.ui.components.ValidatedTextField

@Composable
fun AddTimerScreen() {

    var durationValue by remember { mutableFloatStateOf(10f) }
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }


    var counter by remember { mutableIntStateOf(0) }
    val doubleCounter by remember { derivedStateOf { counter * 2 } }


    var isChecked by remember { mutableStateOf(false) }

    val ringtones = listOf("Classic Bell", "Digital Beep", "Piano Melody", "Forest Birds", "Ocean Waves")
    var selectedRingtone by remember { mutableStateOf(ringtones[0]) }

    val categories = listOf("General", "Baking", "Grilling", "Boiling", "Steaming")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    val scrollState = rememberScrollState()
    val durationDisplay by remember { derivedStateOf { "${durationValue.toInt()} min" } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        ScreenHeader(
            title = "Add Timer",
            subtitle = "Create a new cooking timer with custom options."
        )

        if (title.isNotBlank() || description.isNotBlank()) {
            Text(
                text = "Preview",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TimerCard(
                    title = title.ifBlank { "Title" },
                    duration = durationDisplay,
                    description = description.ifBlank { "Description" }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Ringtone: $selectedRingtone • Category: $selectedCategory",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }


        FormCard {
            Text(
                text = "Timer Duration: $durationDisplay",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Slider(
                value = durationValue,
                onValueChange = { durationValue = it },
                valueRange = 1f..120f,
                steps = 119,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ValidatedTextField(
                value = title,
                onValueChange = { title = it },
                label = "Timer Title",
                placeholder = "Pasta",
                error = if (showErrors && title.isBlank()) "Title is required" else null
            )

            Spacer(modifier = Modifier.height(10.dp))

            ValidatedTextField(
                value = description,
                onValueChange = { description = it },
                label = "Timer Description",
                placeholder = "Boil water, then cook pasta...",
                error = if (showErrors && description.isBlank()) "Description is required" else null
            )

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Quantity: $counter (Double: $doubleCounter)")
                Row {
                    Button(onClick = { if (counter > 0) counter-- }) { Text("-") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { counter++ }) { Text("+") }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text("Repeat timer daily")
            }

            Spacer(modifier = Modifier.height(16.dp))

            DropdownField(
                label = "Ringtone",
                options = ringtones,
                selectedOption = selectedRingtone,
                onOptionSelected = { selectedRingtone = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownField(
                label = "Category",
                options = categories,
                selectedOption = selectedCategory,
                onOptionSelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showErrors = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Timer")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}