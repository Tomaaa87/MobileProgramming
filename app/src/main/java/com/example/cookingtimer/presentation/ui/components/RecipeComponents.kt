package com.example.cookingtimer.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cookingtimer.model.data.local.entity.IngredientEntity
import com.example.cookingtimer.model.data.local.entity.RecipeStepEntity

@Composable
fun IngredientItem(ingredient: IngredientEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = ingredient.name, fontSize = 16.sp)
        Text(text = ingredient.amount, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun RecipeStepItem(step: RecipeStepEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = step.stepNumber.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = step.instruction, fontSize = 16.sp)
    }
}

@Composable
fun IngredientList(ingredients: List<IngredientEntity>) {
    Column {
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        ingredients.forEach { ingredient ->
            IngredientItem(ingredient = ingredient)
        }
    }
}

@Composable
fun RecipeStepList(steps: List<RecipeStepEntity>) {
    Column {
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        steps.forEach { step ->
            RecipeStepItem(step = step)
        }
    }
}
