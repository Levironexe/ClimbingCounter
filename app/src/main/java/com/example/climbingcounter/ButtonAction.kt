package com.example.climbingcounter

sealed class ButtonAction {
    object Climb: ButtonAction()
    object Fall: ButtonAction()
    object Reset: ButtonAction()
    object LanguageChange: ButtonAction()
    object DisplayMode: ButtonAction()
}