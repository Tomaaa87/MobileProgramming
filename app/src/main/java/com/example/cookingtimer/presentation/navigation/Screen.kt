package com.example.cookingtimer.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")

    object Dashboard : Screen("dashboard_screen")
    object Profile : Screen("profile_screen")
    object AddTimer : Screen("add_timer_screen")
    
    object Timers : Screen("timers_screen")
    object Achievements : Screen("achievements_screen")
    object Habits : Screen("habits_screen")
    object Quests : Screen("quests_screen")
    object HomeShortcut : Screen("home_shortcut_screen")
    object TimerDetail : Screen("timer_detail_screen/{timerId}") {
        fun createRoute(timerId: Int) = "timer_detail_screen/$timerId"
    }
}
