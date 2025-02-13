package com.example.climbingcounter

import android.app.Notification
import androidx.compose.ui.graphics.Color

data class ButtonState (
    val score: String = "0",
    val scoreColor: Color = Color.White,
    val hold: String = "0",
    val isFallen: Boolean = false,
    val notification: String = ""
)