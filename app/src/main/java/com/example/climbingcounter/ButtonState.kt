package com.example.climbingcounter

import android.app.Notification
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import android.content.Context

data class ButtonState (
    val score: String = "0",
    val scoreColor: Color = Color.Black,
    val hold: String = "0",
    val isFallen: Boolean = false,
    val notification: String = ""
)