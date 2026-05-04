package com.example.cookingtimer.presentation.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cookingtimer.presentation.ui.components.FormCard
import com.example.cookingtimer.presentation.ui.components.ScreenHeader
import com.example.cookingtimer.presentation.ui.components.ValidatedTextField
import com.example.cookingtimer.presentation.ui.components.LoadingScreen
import com.example.cookingtimer.presentation.ui.components.ErrorScreen
import com.example.cookingtimer.presentation.view_model.profile.ProfileViewModel
import com.example.cookingtimer.presentation.view_model.profile.ProfileUiState
import com.example.cookingtimer.presentation.view_model.profile.ProfileData
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ProfileUiState.Loading -> LoadingScreen()
        is ProfileUiState.Error -> ErrorScreen(
            message = (uiState as ProfileUiState.Error).message,
            onRetry = { viewModel.resetUiState() }
        )
        is ProfileUiState.Success -> {
            StatelessProfileScreen(
                profileData = (uiState as ProfileUiState.Success).profileData,
                onLogoutClick = onLogoutClick
            )
        }
        else -> { /* Do nothing for Init or other states */ }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessProfileScreen(
    profileData: ProfileData,
    onLogoutClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Local UI state for fields that the user can edit before saving
    var displayName by remember { mutableStateOf(profileData.displayName) }
    var bio by remember { mutableStateOf(profileData.bio) }
    var notificationsEnabled by remember { mutableStateOf(profileData.notificationsEnabled) }
    var emailUpdates by remember { mutableStateOf(profileData.emailUpdates) }
    var selectedTheme by remember { mutableStateOf(profileData.selectedTheme) }
    var fontSize by remember { mutableFloatStateOf(profileData.fontSize) }
    
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out?") },
            text = { Text("You will need to sign in again to access your timers.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Log Out") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile Settings") },
                actions = {
                    Box {
                        IconButton(onClick = { showMoreMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(
                            expanded = showMoreMenu,
                            onDismissRequest = { showMoreMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Export Data") },
                                onClick = {
                                    showMoreMenu = false
                                    scope.launch { snackbarHostState.showSnackbar("Data exported") }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete Account", color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    showMoreMenu = false
                                    scope.launch { snackbarHostState.showSnackbar("Account deletion requested") }
                                }
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { scope.launch { snackbarHostState.showSnackbar("Changes saved") } }
            ) { Icon(Icons.Default.Check, contentDescription = "Save") }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ScreenHeader(
                    title = "Profile",
                    subtitle = "Personalize your cooking experience."
                )
            }

            item {
                SectionTitle("My Achievements")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(profileData.achievements) { achievement ->
                        BadgeChip(achievement)
                    }
                }
            }

            item {
                FormCard {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(12.dp), tint = Color.White)
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    null,
                                    modifier = Modifier.size(50.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = displayName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "@${displayName.lowercase().replace(" ", "")}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                SectionTitle("General Info")
                FormCard {
                    ValidatedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = "Display Name"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ValidatedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = "Bio"
                    )
                }
            }

            item {
                SectionTitle("Notifications")
                FormCard {
                    ToggleRow(
                        title = "Push Notifications",
                        subtitle = "Alerts for finished timers",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    ToggleRow(
                        title = "Email Updates",
                        subtitle = "Weekly recipe suggestions",
                        checked = emailUpdates,
                        onCheckedChange = { emailUpdates = it }
                    )
                }
            }

            item {
                SectionTitle("Theme")
                FormCard {
                    listOf("Light", "Dark", "System").forEachIndexed { index, theme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = theme == selectedTheme,
                                    onClick = { selectedTheme = theme },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = theme == selectedTheme, onClick = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(theme, fontSize = 16.sp)
                        }
                        if (index < 2) HorizontalDivider()
                    }
                }
            }

            item {
                SectionTitle("Appearance")
                FormCard {
                    Text("Sample Font Size: ${fontSize.toInt()}sp", fontSize = 14.sp)
                    Slider(
                        value = fontSize,
                        onValueChange = { fontSize = it },
                        valueRange = 12f..24f,
                        steps = 5
                    )
                    Text(
                        "Preheat oven to 200°C.",
                        fontSize = fontSize.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            item {
                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out")
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
private fun BadgeChip(name: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
