package com.example.eyeskeeper

object Constants {
    val PERIOD: String = "period"
    val PERIOD_TIME: String = "periodTime"
    val CHARACTER: String = "character"
    val VIBRATE: String = "vibrate"
    val DEFAULT_PERIOD: Int = 15;
    val DEFAULT_PERIOD_TIME: Int = 1;
    val CHARACTER_TYPE_MAP = mapOf<String, Int>(CHARACTER_TYPE.CLASSIC.value to 0, CHARACTER_TYPE.CAT.value to 1)
    val DIALOG_MESSAGE_MAP = mapOf<Int, Int>(
        1 to R.string.blink,
        2 to R.string.circle_bacward,
        3 to R.string.circle_forward,
        4 to R.string.diagonal,
        5 to R.string.left_right,
        6 to R.string.up_down,
    )
    val millisecondInSec = 60000

   enum class PERIODS(val value: Int) {
        FIFTEEN(900000),
        HALF_HOUR(1800000),
        HOUR(3600000),
        ONE_AND_HALF_HOUR(5400000),
        TWO_HOUR(7200000)
   }

    enum class CHARACTER_TYPE(val value: String) {
        CLASSIC("classic"),
        CAT("cat")
    }

    enum class CHARACTER_IMAGE_ID(val value: Int) {
        CLASSIC(R.drawable.classic),
        CAT(R.drawable.cat)
    }

    interface Timer {
        val timerRestart: Boolean
    }

    interface Character {
        var isChecked: Boolean
        val checkBoxText: String
        val characterType: Int
    }

    interface SettingsData {
        val period: Int
        val periodTime: Int
        val character: Int
        val vibrate: Boolean
    }
}