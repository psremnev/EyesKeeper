package com.example.eyeskeeper

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.DialogFragment.STYLE_NORMAL

class MainActivity : AppCompatActivity() {
    var serviceIsRun: Boolean? = null
    var enableBtn: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableBtn = findViewById(R.id.enableSwitcher)
        serviceIsRun = isMyServiceRunning(KeeperService::class.java)
        enableBtn?.isChecked = serviceIsRun!!
        val settingsBtn: Button = findViewById(R.id.settingsBtn)
        val animAlpha: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        enableBtn?.setOnClickListener {
            val isChecked: Boolean = enableBtn?.isChecked as Boolean
            if (isChecked) {
                startService(Intent(this, KeeperService::class.java))
            } else {
                stopService(Intent(this, KeeperService::class.java))
            }
        }

        settingsBtn.setOnClickListener {
            it.startAnimation(animAlpha);
            val frg: DialogFragment = Settings();
            frg.setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            frg.show(supportFragmentManager, "settings")
        }
    }

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