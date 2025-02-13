package com.example.climbingcounter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat

object DialogHelper {
    private var currentDialog: AlertDialog? = null

    fun showDialog(
        context: Context,
        message: String,
        currentStrings: StringResources,
    ) {

        currentDialog?.dismiss()

        val titleView = TextView(context).apply {
            text = currentStrings.dialogTitle
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(48, 32, 48, 16)
        }

        val messageView = TextView(context).apply {
            text = message
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            setLineSpacing(0f, 1.25f)
            setTextColor(0x99000000.toInt())
            setPadding(48, 0, 48, 32)
        }

        currentDialog = AlertDialog.Builder(context)
            .setCustomTitle(titleView)
            .setView(messageView)
            .setPositiveButton(currentStrings.dialogButtonGotIt) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        currentDialog?.setOnShowListener { dialog ->
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.post {
                positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                positiveButton.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                positiveButton.setBackgroundResource(R.drawable.rounded_dialog_button)
            }
        }

        currentDialog?.show()
        currentDialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)

    }
}
