package com.example.climbingcounter

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.climbingcounter.ui.theme.MainBlue
import com.example.climbingcounter.ui.theme.MainGreen
import com.example.climbingcounter.ui.theme.MainRed

class ClimbingCounterViewModel : ViewModel() {
    private val _state = MutableLiveData(ButtonState())
    val state: LiveData<ButtonState> = _state

    private var winSound: MediaPlayer? = null
    private val _currentLanguage = MutableLiveData<Language>(Language.ENGLISH)
    val currentLanguage: LiveData<Language> = _currentLanguage
    private val _strings = MutableLiveData<StringResources>()
    val strings: LiveData<StringResources> = _strings

    val currentStrings: StringResources
        get() = when (_currentLanguage.value) {
            Language.ENGLISH -> StringResources.English()
            Language.FRENCH -> StringResources.French()
            Language.VIETNAMESE -> StringResources.Vietnamese()
            null -> StringResources.English()
        }

    init {
        _currentLanguage.value = Language.ENGLISH
        _strings.value = StringResources.English()
    }

    private var _context: Context? = null

    fun setContext(context: Context) {
        _context = context
    }

    fun initializeLanguage() {
        _currentLanguage.value = _currentLanguage.value ?: Language.ENGLISH
    }

    fun onLanguageChange(newLanguage: Language) {
        _currentLanguage.value = newLanguage
        _strings.value = when (newLanguage) {
            Language.ENGLISH -> StringResources.English()
            Language.FRENCH -> StringResources.French()
            Language.VIETNAMESE -> StringResources.Vietnamese()
        }
    }

    private fun showMessage(message: String) {
        _context?.let { context ->
            DialogHelper.showDialog(
                context = context,
                message = message,
                currentStrings = currentStrings
            )
        }
    }

    fun initializeSound(context: Context) {
        winSound = MediaPlayer.create(context, R.raw.win).apply {
            setOnCompletionListener { seekTo(0) }
        }
    }

    private fun playWinSound() {
        winSound?.start()
    }

    override fun onCleared() {
        super.onCleared()
        winSound?.release()
        winSound = null
    }

    fun onAction(action: ButtonAction) {
        when (action) {
            is ButtonAction.Climb -> increaseScore()
            is ButtonAction.Fall -> decreaseScore()
            is ButtonAction.Reset -> resetScore()
            is ButtonAction.LanguageChange -> {}
            is ButtonAction.DisplayMode -> {}
        }
    }

    private fun increaseScore() {
        val currentState = _state.value ?: return
        val currentScore = currentState.score.toIntOrNull() ?: 0
        fun levelUp(view: View) {
            val popSize = AnimatorInflater.loadAnimator(view.context, R.animator.level_up)
            popSize.setTarget(view)
            popSize.start()
        }
        // Prevent further score increase if already at or beyond 18
        if (currentScore >= 18) {
            showMessage("Maximum height reached!")
            return
        }

        val points = when (currentScore) {
            in 0..2 -> 1    // Blue zone (holds 1-3)
            in 3..8 -> 2    // Green zone (holds 4-6)
            in 9..17 -> 3   // Red zone (holds 7-9)
            else -> 0       // Beyond max holds
        }
        if (currentScore < 18 && !currentState.isFallen) {
            val newScore = currentScore + points
            val newHold = (currentState.hold.toIntOrNull() ?: 0) + 1
            val newColor = when (newScore) {
                in 0..0 -> Color.White
                in 1..2 -> MainBlue
                in 3..8 -> MainGreen
                in 9..17 -> MainRed
                else -> MainRed
            }

            val updatedState = currentState.copy(
                score = minOf(newScore, 18).toString(),
                scoreColor = newColor,
                hold = newHold.toString()
            )

            if (newScore >= 18) {
                updatedState.copy(
                    score = "18",
                    isFallen = true,
                ).also { finalState ->
                    _state.value = finalState
                    playWinSound()
                    showMessage(currentStrings.dialogWinMessage)
                }
            } else {
                _state.value = updatedState
            }
        }

    }

    fun getScoreColor(): Color {
        val currentState = _state.value ?: return Color.White  // provide a default color
        return currentState.scoreColor
    }
    fun getScore(): String {
        val currentScore = _state.value?: return "0"
        return currentScore.score
    }

    private fun decreaseScore() {
        val currentState = _state.value ?: return
        val currentScore = currentState.score.toIntOrNull() ?: 0

        if (currentScore == 0 && !currentState.isFallen) {
            showMessage(currentStrings.dialogCannotFallMessage)
            return
        }

        if (!currentState.isFallen) {
            val points = 3
            val newScore = when (currentScore) {
                in 0..2 -> 0
                else -> currentScore - points
            }

            val newColor = when(newScore) {
                in 0..0 -> Color.White
                in 1..2 -> MainBlue
                in 3..8 -> MainGreen
                else -> MainRed
            }

            _state.value = currentState.copy(
                score = newScore.toString(),
                scoreColor = newColor,
                isFallen = true
            )
        }
    }

    private fun resetScore() {
        val currentState = _state.value ?: return

        Log.d("ViewModel", "Current Score: ${currentState.score}")
        Log.d("ViewModel", "Is Fallen: ${currentState.isFallen}")

        if (currentState.score.toIntOrNull() == 18 || currentState.isFallen) {
            _state.value = currentState.copy(
                score = "0",
                hold = "0",
                isFallen = false,
                scoreColor = Color.White
            )
            Log.d("ViewModel", "Reset successful")
        } else {
            showMessage(currentStrings.dialogResetOnlyMessage)
            Log.d("ViewModel", "Reset not allowed")
        }
    }

    fun getHoldCountText(): String {
        val currentState = _state.value ?: return ""
        return currentStrings.holdCount.format(currentState.hold.toIntOrNull() ?: 0)
    }

    fun getScoreTitle(): String = currentStrings.scoreTitle
    fun getFallButtonText(): String = currentStrings.fallButton
    fun getClimbButtonText(): String = currentStrings.climbButton
    fun getResetButtonText(): String = currentStrings.resetButton
}