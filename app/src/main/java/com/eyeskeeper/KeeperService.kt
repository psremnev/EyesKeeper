package com.eyeskeeper

import android.app.Service
import android.os.CountDownTimer
import android.os.IBinder
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import android.app.Notification
import androidx.core.app.NotificationCompat.Builder
import android.content.*

class KeeperService: Service() {
    private var settings: Constants.SettingsData? = null
    var timer: CountDownTimer? = null
    var dialogIntent: Intent? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initDialogSettings()
        initObservableForTimer()
        dialogIntent = getIntentDialog()
        timer = getDialogTimer()
        val intentFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(getScreenStateReceiver(), intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer?.start()
        startKeepServiceForeground()
        return START_STICKY;
    }

    override fun onDestroy() {
        stopForeground(true)
        timer?.cancel()
        timer = null
        super.onDestroy()
    }

    /** Получить таймер показа диалога тренировки */
    private fun getDialogTimer(): CountDownTimer {
        val period = (settings?.period!! * Constants.millisecondInSec).toLong()
        return object: CountDownTimer(period, Constants.millisecondInSec.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                return
            }

            override fun onFinish() {
                startActivity(dialogIntent)
            }
        }
    }

    /** Инициализировать данные по статусу таймера */
    private fun initObservableForTimer() {
        Constants.subject.subscribeOn(Schedulers.newThread())
            .subscribe(object: DisposableObserver<Constants.Observable>() {
                override fun onNext(t: Constants.Observable) {
                    if (t.timerRestart) {
                        restartTimer()
                    }
                }

                override fun onError(e: Throwable) {
                    return
                }

                override fun onComplete() {
                    return
                }
            })
    }

    /** Перезапустить таймер */
    fun restartTimer() {
        timer?.cancel()
        timer?.start()
    }

    /** Инициализировать настройки диалога */
    private fun initDialogSettings() {
        settings = DataHelper(applicationContext).getSettings()
    }

    /** Старт сервиса в фоновом режиме */
    private fun startKeepServiceForeground() {
        val notification: Notification
        val builder = Builder(this, "1")
        notification = builder.build()
        startForeground(1, notification)
    }

    /** Получить интент диалога тренировки */
    private fun getIntentDialog(): Intent {
        val intent = Intent(applicationContext, Dialog::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }

    /** Получить подписку на блокировку экрана и ее обработка */
    private fun getScreenStateReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action === Intent.ACTION_SCREEN_OFF) {
                    timer?.cancel()
                } else if (intent?.action === Intent.ACTION_SCREEN_ON) {
                    timer?.start()
                }
            }
        }
    }
}
