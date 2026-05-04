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
import com.example.cookingtimer.presentation.view_model.home.HomeShortcutViewModel
import com.example.cookingtimer.presentation.view_model.timer.TimerViewModel
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
        composable(Screen.HomeShortcut.route) {
            val viewModel: HomeShortcutViewModel = hiltViewModel()
            HomeShortcutScreen(
                viewModel = viewModel,
                onScreenClick = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable(Screen.Timers.route) {
            val viewModel: TimerViewModel = hiltViewModel()
            TimerScreen(
                viewModel = viewModel,
                onAddTimerClick = {
                    navController.navigate(Screen.AddTimer.createRoute("TimerList"))
                }
            )
        }
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
            DashboardScreen(name = name, email = email)
        }
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigate = {
                    navController.navigate(Screen.HomeShortcut.route) {
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
                    navController.navigate(Screen.HomeShortcut.route) {
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