package com.example.cookingtimer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cookingtimer.presentation.ui.screens.*

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
            DashboardScreen(name = name, email = email)
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { name, email ->
                    navController.navigate(Screen.Dashboard.createRoute(name, email)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = { name, email ->
                    navController.navigate(Screen.Dashboard.createRoute(name, email)) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.AddTimer.route) {
            AddTimerScreen()
        }
    }
}