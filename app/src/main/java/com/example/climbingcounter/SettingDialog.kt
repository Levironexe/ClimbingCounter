package com.example.climbingcounter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatDelegate

object SettingDialog {
    private var currentDialog: AlertDialog? = null

    fun showDialog(context: Context, currentLanguage: Language, onLanguageChange: (Language) -> Unit) {
        // Dismiss any existing dialog
        currentDialog?.dismiss()

        // Create title TextView
        val titleView = TextView(context).apply {
            text = "Settings"
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(48, 32, 48, 16)
        }

        // Create language selection container
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 16, 48, 32)
        }

        // Add "Select Language" label
        container.addView(TextView(context).apply {
            text = "Select Language"
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            typeface = Typeface.DEFAULT  // Changed from DEFAULT_MEDIUM to DEFAULT
            setPadding(0, 0, 0, 24)
        })

        // Add language buttons
        Language.values().forEach { language ->
            val button = Button(context).apply {
                text = when (language) {
                    Language.ENGLISH -> "English"
                    Language.FRENCH -> "Français"
                    Language.VIETNAMESE -> "Tiếng Việt"
                }
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)  // Add margins between buttons
                }

                if (language == currentLanguage) {
                    setBackgroundResource(R.drawable.rounded_dialog_button)
                    setTextColor(ContextCompat.getColor(context, android.R.color.white))
                } else {
                    setBackgroundResource(R.drawable.rounded_dialog_button_unselected)
                    setTextColor(ContextCompat.getColor(context, android.R.color.white))
                }

                setPadding(32, 16, 32, 16)
                setOnClickListener {
                    onLanguageChange(language)
                    currentDialog?.dismiss()
                }
            }
            container.addView(button)
        }

        // Build and show the dialog
        currentDialog = AlertDialog.Builder(context)
            .setCustomTitle(titleView)
            .setView(container)
            .create()

        currentDialog?.show()
        currentDialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)


        // Func to change theme
        val themeSwitch = TextView(context).apply {
            text = "Toggle Theme"
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            typeface = Typeface.DEFAULT
            setPadding(0, 24, 0, 24)
        }
        container.addView(themeSwitch)

        val themeButton = Button(context).apply {
            text = if (ThemeManager.isDarkMode(context)) "Light Mode" else "Dark Mode"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
            setBackgroundResource(R.drawable.rounded_dialog_button)
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            setPadding(32, 16, 32, 16)

            setOnClickListener {
                val newDarkMode = !ThemeManager.isDarkMode(context)
                ThemeManager.setDarkMode(context, newDarkMode)
                (context as Activity).recreate()
                currentDialog?.dismiss()
            }
        }
        container.addView(themeButton)
    }
}