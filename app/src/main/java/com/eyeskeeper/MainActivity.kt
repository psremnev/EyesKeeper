package com.eyeskeeper

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.DialogFragment.STYLE_NORMAL

class MainActivity : AppCompatActivity() {
    var serviceIsRun: Boolean? = null
    var enableBtn: SwitchCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Инициализируем кнопку включения */
        enableBtn = findViewById(R.id.enableSwitcher)
        serviceIsRun = isMyServiceRunning(KeeperService::class.java)
        enableBtn?.isChecked = serviceIsRun!!
        val animAlpha: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        enableBtn?.setOnClickListener {
            val isChecked: Boolean = enableBtn?.isChecked as Boolean
            if (isChecked) {
                startService(Intent(this, KeeperService::class.java))
            } else {
                stopService(Intent(this, KeeperService::class.java))
            }
        }

        /** Инициализируем кнопку настроек */
        val settingsBtn: Button = findViewById(R.id.settingsBtn)
        settingsBtn.setOnClickListener {
            it.startAnimation(animAlpha);
            val frg: DialogFragment = Settings();
            frg.setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            frg.show(supportFragmentManager, "settings")
        }

        /** Инициализируем кнопку о программе */
        val aboutBtn: Button = findViewById(R.id.aboutBtn)
        aboutBtn.setOnClickListener {
            val frg: DialogFragment = About();
            frg.setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Dialog_NoActionBar)
            frg.show(supportFragmentManager, "about")
        }
    }

    /** Определяем запущен ли сервис или нет
     * @param serviceClass {Class<*>}
     */
    @Suppress("DEPRECATION")
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}