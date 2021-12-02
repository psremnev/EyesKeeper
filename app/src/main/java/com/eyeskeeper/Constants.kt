package com.eyeskeeper

import io.reactivex.subjects.BehaviorSubject

object Constants {
    val subject: BehaviorSubject<Observable> = BehaviorSubject.create()
    const val PERIOD: String = "period"
    const val PERIOD_TIME: String = "periodTime"
    const val CHARACTER: String = "character"
    const val VIBRATE: String = "vibrate"
    const val DEFAULT_PERIOD: Int = 15
    const val DEFAULT_PERIOD_TIME: Int = 1
    val CHARACTER_TYPE_MAP = mapOf<String, Int>(
        CharacterType.CLASSIC.value to 0,
        CharacterType.CAT.value to 1,
        CharacterType.PANDA.value to 2)
    val DIALOG_MESSAGE_MAP = mapOf<Int, Int>(
        1 to R.string.blink,
        2 to R.string.circle_bacward,
        3 to R.string.circle_forward,
        4 to R.string.diagonal,
        5 to R.string.left_right,
        6 to R.string.up_down,
    )
    const val millisecondInSec = 60000

   enum class PERIODS(val value: Int) {
        FIFTEEN(900000),
        HALF_HOUR(1800000),
        HOUR(3600000),
        ONE_AND_HALF_HOUR(5400000),
        TWO_HOUR(7200000)
   }

    enum class CharacterType(val value: String) {
        CLASSIC("Classic"),
        CAT("Cat"),
        PANDA("Panda")
    }

    enum class CharacterImageId(val value: Int) {
        CLASSIC(R.drawable.classic),
        CAT(R.drawable.cat),
        PANDA(R.drawable.panda)
    }

    interface Observable {
        val timerRestart: Boolean
    }

    interface Character {
        var isChecked: Boolean
        val checkBoxText: String
        val characterType: Int
    }

    interface SettingsData {
        val period: Int?
        val periodTime: Int?
        val character: Int?
        val vibrate: Boolean?
    }
}