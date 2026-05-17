package com.example.cookingtimer.model.repository.timer

data class TimerData(
    val id: Int,
    val title: String,
    val totalSeconds: Long,
    val remainingSeconds: Long,
    val description: String,
    val isRunning: Boolean = false
) {
    val durationText: String
        get() {
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            return "%02d:%02d".format(minutes, seconds)
        }

    val progress: Float
        get() = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds.toFloat() else 0f
}