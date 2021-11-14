package com.example.eyeskeeper

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class DataHelper (baseContext: Context) {
    private val settingsPreferences: SharedPreferences = baseContext.getSharedPreferences(Companion.PREFERENCES_FILE_NAME, AppCompatActivity.MODE_PRIVATE)
    val defaultCharacter: Int = Constants.CHARACTER_TYPE_MAP[Constants.CHARACTER_TYPE.CLASSIC.value]!!

    private fun initSettings() {
        val preferenceEditor: SharedPreferences.Editor = settingsPreferences.edit()
        preferenceEditor.putInt(Constants.PERIOD, Constants.DEFAULT_PERIOD)
        preferenceEditor.putInt(Constants.PERIOD_TIME, Constants.DEFAULT_PERIOD_TIME)
        preferenceEditor.putInt(Constants.CHARACTER, defaultCharacter)
        preferenceEditor.putBoolean(Constants.VIBRATE, false)
        preferenceEditor.commit()
    }

    fun getSettings(): Constants.SettingsData {
        val period =
            settingsPreferences.getInt(Constants.PERIOD, Constants.DEFAULT_PERIOD)
        val periodTime: Int =
            settingsPreferences.getInt(Constants.PERIOD_TIME, Constants.DEFAULT_PERIOD_TIME)
        val character: Int =
            settingsPreferences.getInt(Constants.CHARACTER, defaultCharacter)
        val vibrate: Boolean =
            settingsPreferences.getBoolean(Constants.VIBRATE, false)
        if (period === 0 || periodTime === 0) {
            initSettings()
            return object: Constants.SettingsData  {
                override val period = Constants.DEFAULT_PERIOD
                override val periodTime: Int = Constants.DEFAULT_PERIOD_TIME
                override val character: Int = defaultCharacter
                override val vibrate: Boolean = false
            }
        } else {
            return object: Constants.SettingsData  {
                override val period = period
                override val periodTime: Int = periodTime
                override val character: Int = character
                override val vibrate: Boolean = vibrate
            }
        }
    }

    fun saveSettings(period: Int?, periodTime: Int?, characterType: Int?, vibrate: Boolean?) {
        val editor: SharedPreferences.Editor = settingsPreferences.edit()
        if (period !== null) {
            editor.putInt(Constants.PERIOD, period)
        }
        if (periodTime !== null) {
            editor.putInt(Constants.PERIOD_TIME, periodTime)
        }
        if (characterType !== null) {
            editor.putInt(Constants.CHARACTER, characterType)
        }
        if (vibrate !== null) {
            editor.putBoolean(Constants.VIBRATE, vibrate)
        }
        editor.commit()
    }

    companion object {
        const val PREFERENCES_FILE_NAME: String = "EyesKeeper";
    }
}