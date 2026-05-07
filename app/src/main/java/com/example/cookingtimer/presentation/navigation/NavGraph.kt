package com.example.cookingtimer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cookingtimer.presentation.ui.screens.*
import com.example.cookingtimer.presentation.view_model.auth.login.LoginViewModel
import com.example.cookingtimer.presentation.view_model.auth.registration.RegistrationViewModel
import com.example.cookingtimer.presentation.view_model.profile.ProfileViewModel
import com.example.cookingtimer.presentation.view_model.dashboard.DashboardViewModel
import com.example.cookingtimer.presentation.view_model.timer.AddTimerViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.Dashboard.route,
            arguments = listOf(
                navArgument("name") { 
                    type = NavType.StringType
                    defaultValue = "Tom"
                },
                navArgument("email") { 
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "Tom"
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = viewModel,
                name = name, 
                email = email,
                onShortcutClick = { shortcut ->
                    when(shortcut) {
                        "Add Timer" -> navController.navigate(Screen.AddTimer.createRoute("Dashboard"))
                        "Profile" -> navController.navigate(Screen.Profile.route)
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigate = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            val viewModel: RegistrationViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigate = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.AddTimer.route,
            arguments = listOf(
                navArgument("source") {
                    type = NavType.StringType
                    defaultValue = "Default"
                }
            )
        ) { backStackEntry ->
            val source = backStackEntry.arguments?.getString("source") ?: "Default"
            val viewModel: AddTimerViewModel = hiltViewModel()
            AddTimerScreen(
                source = source,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
