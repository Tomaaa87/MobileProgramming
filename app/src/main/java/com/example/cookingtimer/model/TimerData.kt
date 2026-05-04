package com.example.cookingtimer.model

data class TimerData(
    val id: Int,
    val title: String,
    val duration: String,
    val description: String,
    val isRunning: Boolean = false
)
