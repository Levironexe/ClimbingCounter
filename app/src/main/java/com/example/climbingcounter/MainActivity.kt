package com.example.climbingcounter

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.ui.graphics.toArgb
import android.animation.AnimatorInflater
import android.view.View
import android.animation.ObjectAnimator
import android.view.animation.CycleInterpolator
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    private lateinit var scoreText: TextView
    private lateinit var holdCountText: TextView
    private lateinit var scoreTitle: TextView
    private lateinit var fallButton: Button
    private lateinit var climbButton: Button
    private lateinit var resetButton: Button
    private lateinit var settingsButton: ImageButton

    private var climbSoundEffect: MediaPlayer? = null
    private var fallSoundEffect: MediaPlayer? = null
    private var resetSoundEffect: MediaPlayer? = null

    private val viewModel: ClimbingCounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize context and language before setting up UI
        viewModel.setContext(this)
        viewModel.initializeLanguage()

        initializeViews()
        initializeSounds()
        setupClickListeners()
        observeViewModel()

        viewModel.initializeSound(this)
    }

    private fun initializeViews() {
        scoreText = findViewById(R.id.scoreText)
        scoreTitle = findViewById(R.id.scoreTitle)
        holdCountText = findViewById(R.id.holdCountText)  // Matches XML ID exactly
        fallButton = findViewById(R.id.fallButton)
        climbButton = findViewById(R.id.climbButton)
        resetButton = findViewById(R.id.resetButton)
        settingsButton = findViewById(R.id.settingsButton)
    }

    private fun initializeSounds() {
        climbSoundEffect = MediaPlayer.create(this, R.raw.click)
        fallSoundEffect = MediaPlayer.create(this, R.raw.fall)
        resetSoundEffect = MediaPlayer.create(this, R.raw.reset)
    }
    private fun levelUp(view: View) {
        val popSize = AnimatorInflater.loadAnimator(view.context, R.animator.level_up)
        popSize.setTarget(view)
        popSize.start()
    }
    private fun shakeView(view: View) {
        val shakeAnimator = AnimatorInflater.loadAnimator(view.context, R.animator.shake_animation)
        shakeAnimator.setTarget(view)

        if (view is TextView) {
            val colorAnimator = ObjectAnimator.ofArgb(
                view as TextView,
                "textColor",
                viewModel.getScoreColor().toArgb(),
                Color.White.toArgb(),
                Color.Red.toArgb(),
                Color.White.toArgb(),
                Color.Red.toArgb(),
                viewModel.getScoreColor().toArgb()
            ) .apply {
                duration = 500
            }
            colorAnimator.start()
        }
        shakeAnimator.start()
    }

    private fun setupClickListeners() {
        fun Button.setupTouchAnimation() {
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        AnimatorInflater.loadAnimator(context, R.animator.button_press).apply {
                            setTarget(v)
                            start()
                        }
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        AnimatorInflater.loadAnimator(context, R.animator.button_release).apply {
                            setTarget(v)
                            start()
                        }
                        v.performClick()
                        true
                    }
                    else -> false
                }
            }
        }




        climbButton.setupTouchAnimation()
        fallButton.setupTouchAnimation()
        resetButton.setupTouchAnimation()
        climbButton.setOnClickListener {
            viewModel.onAction(ButtonAction.Climb)
            climbSoundEffect?.start()
            levelUp(scoreText)
        }

        fallButton.setOnClickListener {
            viewModel.onAction(ButtonAction.Fall)
            shakeView(scoreText)
            fallSoundEffect?.start()
        }

        resetButton.setOnClickListener {
            viewModel.onAction(ButtonAction.Reset)
            resetSoundEffect?.start()
        }

        settingsButton.setOnClickListener {
            showDialog()
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            scoreText.text = state.score
            scoreText.setTextColor(state.scoreColor.toArgb())


            // Use the correct hold count text method from ViewModel
            holdCountText.text = viewModel.getHoldCountText()

            // Check if there's a notification to show
            state.notification.takeIf { it.isNotEmpty() }?.let { message ->
                DialogHelper.showDialog(
                    context = this,
                    message = message,
                    currentStrings = viewModel.currentStrings
                )
            }
        }

        viewModel.currentLanguage.observe(this) {
            // Update UI text when language changes
            updateLanguageStrings()
        }
    }

    private fun updateLanguageStrings() {
        Log.d("MainActivity", "Updating language strings")
        scoreTitle.text = viewModel.getScoreTitle()
        fallButton.text = viewModel.getFallButtonText()
        climbButton.text = viewModel.getClimbButtonText()
        resetButton.text = viewModel.getResetButtonText()
        holdCountText.text = viewModel.getHoldCountText()
    }

    private fun showDialog() {
        SettingDialog.showDialog(
            context = this,
            currentLanguage = viewModel.currentLanguage.value ?: Language.ENGLISH,
            onLanguageChange = viewModel::onLanguageChange
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        climbSoundEffect?.release()
        fallSoundEffect?.release()
        resetSoundEffect?.release()
    }
}