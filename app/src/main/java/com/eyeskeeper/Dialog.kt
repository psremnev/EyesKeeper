package com.eyeskeeper

import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.net.Uri
import android.os.*
import android.widget.ImageView
import com.bumptech.glide.Glide


class Dialog : AppCompatActivity() {
    var isRestart: Boolean = false;
    var settings: Constants.SettingsData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog)

        settings = DataHelper(applicationContext).getSettings()
    }

    override fun onResume() {
        super.onResume()
        val periodTimer: Int = settings?.periodTime!! * Constants.millisecondInSec
        startTimeTimer(periodTimer)
        startImageTimer(periodTimer)
        initHiddenBtn()
    }

    override fun onDestroy() {
        // чтобы не перезапускать если закрыт по кнопке
        if (!isRestart) {
            Constants.subject.onNext(object: Constants.Observable {
                override val timerRestart: Boolean = true
            })
        }
        isRestart = false
        super.onDestroy()
    }

    /** Получить Дату в нужном формате
     * @param milliseconds {Int}
     */
    fun getTime(milliseconds: Int): String {
        val formatter = SimpleDateFormat("mm:ss")
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds.toLong()
        return formatter.format(calendar.time)
    }

    /** Получить Дату в нужном формате
     * @param character {String} - название персонажа
     * @param imageNum {Int} - номер картинки в папке
     */
    @Suppress("DEPRECATION")
    private fun changeTrainData(character: String, imageNum: Int) {
        val url = "file:///android_asset/${character}/$imageNum.gif"
        val trainImage: ImageView = findViewById(R.id.trainImage)
        val textImage: TextView = findViewById(R.id.description)
        if (settings?.vibrate == true) {
            vibrate(100)
        }
        Glide.with(applicationContext)
            .asGif()
            .load(Uri.parse(url))
            .into(trainImage)
        Constants.DIALOG_MESSAGE_MAP[imageNum]?.let { textImage.setText(it) }
    }

    /** Сделать вибрацию
     * @param milliseconds {Long}
     */
    @Suppress("DEPRECATION")
    private fun vibrate(milliseconds: Long) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()
        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(milliseconds)
            }
        }
    }

    /** Показать счетчик времени тренировки
     * @param periodTimer {Int}
     */
    private fun startTimeTimer(periodTimer: Int) {
        val timer: TextView = findViewById(R.id.timer)
        timer.text = getTime(periodTimer)
        val timeTimer = object: CountDownTimer(periodTimer.toLong(), 1000) {
            var lastPeriod: Int? = null
            override fun onTick(millisUntilFinished: Long) {
                lastPeriod = if (lastPeriod != null) {
                    lastPeriod!! - 1000
                } else {
                    periodTimer - 1000
                }
                timer.text = getTime(lastPeriod!!)

            }

            override fun onFinish() {
                finish()
            }
        }
        timeTimer.start()
    }

    /** Счетчик смены картинки
     * @param periodTimer {Int}
     */
    private fun startImageTimer(periodTimer: Int) {
        val maxImageInFolder = 6
        val character = Constants.CHARACTER_TYPE_MAP.keys.elementAt(settings?.character!!)
        changeTrainData(character, (1..maxImageInFolder).random())
        val halfMinute: Long = 30000
        val imageChangeTimer = object: CountDownTimer(periodTimer.toLong(), halfMinute) {
            override fun onTick(millisUntilFinished: Long) {
                changeTrainData(character, (1..maxImageInFolder).random())
            }

            override fun onFinish() {
                return
            }
        }
        imageChangeTimer.start()
    }

    /** Инициализация кнопки пропустить */
    private fun initHiddenBtn() {
        val hideBtn: Button = findViewById(R.id.hideBtn)
        hideBtn.setOnClickListener {
            isRestart = true
            Constants.subject.onNext(object: Constants.Observable {
                override val timerRestart: Boolean = true
            })
            finish()
        }
    }
}