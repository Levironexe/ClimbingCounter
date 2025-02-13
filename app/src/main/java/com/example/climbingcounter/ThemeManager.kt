package com.example.climbingcounter

import android.content.Context
import android.util.Log

object ThemeManager {
    private const val PREF_NAME = "theme_pref"
    private const val KEY_IS_DARK_MODE = "is_dark_mode"

    fun setDarkMode(context: Context, isDarkMode: Boolean) {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(KEY_IS_DARK_MODE, isDarkMode).apply()

        if (isDarkMode) {
            context.setTheme(R.style.Theme_ClimbingCounter_dark)
        } else {
            context.setTheme(R.style.Theme_ClimbingCounter_light)
        }

        // Log the theme change
        Log.d("ThemeManager", "Theme changed to: ${if (isDarkMode) "Dark" else "Light"} mode")
    }

    fun isDarkMode(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(KEY_IS_DARK_MODE, false)
    }
}