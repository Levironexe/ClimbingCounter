package com.example.climbingcounter

sealed class StringResources {

    // Existing properties
    abstract val scoreTitle: String
    abstract val holdLabel: String
    abstract val fallButton: String
    abstract val climbButton: String
    abstract val resetButton: String
    abstract val holdCount: String

    // Add these new properties for dialogs
    abstract val dialogTitle: String
    abstract val dialogButtonGotIt: String
    abstract val dialogSettingsTitle: String
    abstract val dialogSelectLanguage: String
    abstract val dialogWinMessage: String
    abstract val dialogCannotFallMessage: String
    abstract val dialogResetOnlyMessage: String

    data class English(
        // Existing values
        override val scoreTitle: String = "Score",
        override val holdLabel: String = "Hold",
        override val fallButton: String = "Fall",
        override val climbButton: String = "Climb",
        override val resetButton: String = "Reset",
        override val holdCount: String = "Hold: %d",

        // Add dialog strings
        override val dialogTitle: String = "Message",
        override val dialogButtonGotIt: String = "Got it!",
        override val dialogSettingsTitle: String = "Settings",
        override val dialogSelectLanguage: String = "Select Language",
        override val dialogWinMessage: String = "The Climber has reached the top. Winnerrr!",
        override val dialogCannotFallMessage: String = "Climber cannot fall until 1st hold. Please keep climbing!",
        override val dialogResetOnlyMessage: String = "Score can only be reset if the Climber fell or won"
    ) : StringResources()

    data class French(
        // Existing values
        override val scoreTitle: String = "Score",
        override val holdLabel: String = "Maintenir",
        override val fallButton: String = "Chuter",
        override val climbButton: String = "Grimper",
        override val resetButton: String = "Réinitialiser",
        override val holdCount: String = "Maintenir: %d",

        // Add dialog strings
        override val dialogTitle: String = "Message",
        override val dialogButtonGotIt: String = "Compris !",
        override val dialogSettingsTitle: String = "Paramètres",
        override val dialogSelectLanguage: String = "Choisir la langue",
        override val dialogWinMessage: String = "Le grimpeur a atteint le sommet. Gagné !",
        override val dialogCannotFallMessage: String = "Le grimpeur ne peut pas tomber avant la 1ère prise. Continuez à grimper !",
        override val dialogResetOnlyMessage: String = "Le score ne peut être réinitialisé que si le grimpeur est tombé ou a gagné"
    ) : StringResources()

    data class Vietnamese(
        // Existing values
        override val scoreTitle: String = "Điểm",
        override val holdLabel: String = "Giữ",
        override val fallButton: String = "Rơi",
        override val climbButton: String = "Leo",
        override val resetButton: String = "Đặt lại",
        override val holdCount: String = "Giữ: %d",

        // Add dialog strings
        override val dialogTitle: String = "Thông báo",
        override val dialogButtonGotIt: String = "Đã hiểu!",
        override val dialogSettingsTitle: String = "Cài đặt",
        override val dialogSelectLanguage: String = "Chọn ngôn ngữ",
        override val dialogWinMessage: String = "Người leo núi đã lên đến đỉnh. Chiến thắng!",
        override val dialogCannotFallMessage: String = "Người leo núi không thể ngã cho đến điểm bám đầu tiên. Hãy tiếp tục leo!",
        override val dialogResetOnlyMessage: String = "Chỉ có thể đặt lại điểm nếu người leo núi đã ngã hoặc đã thắng"
    ) : StringResources()
}