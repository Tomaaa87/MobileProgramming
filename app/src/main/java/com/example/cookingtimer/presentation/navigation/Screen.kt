package com.example.cookingtimer.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")

    object Dashboard : Screen("dashboard_screen?name={name}&email={email}") {
        fun createRoute(name: String, email: String) = "dashboard_screen?name=$name&email=$email"
    }
    object Profile : Screen("profile_screen")
    object AddTimer : Screen("add_timer_screen?source={source}") {
        fun createRoute(source: String) = "add_timer_screen?source=$source"
    }
    
    object Timers : Screen("timers_screen")
    object Achievements : Screen("achievements_screen")
    object Habits : Screen("habits_screen")
    object Quests : Screen("quests_screen")
    object HomeShortcut : Screen("home_shortcut_screen")
}
