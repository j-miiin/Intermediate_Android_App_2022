package com.example.alarm_part3_chapter03

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // step0 뷰를 초기화해주기
        initOnOffButton()
        initChangeAlarmTimeButton()

        // step1 데이터 가져오기
        val model = fetchDataFromSharedPreferences()
        renderView(model)

        // step2 뷰에 데이터를 그려주기

    }

    private fun initOnOffButton() {
        val onOffButton = findViewById<Button>(R.id.onOffButton)
        onOffButton.setOnClickListener {

            // as로 형변환
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())

            // 데이터를 확인
            renderView(newModel)

            // 온오프에 따라 작업을 처리
            if (newModel.onOff) {
                // 켜진 경우 -> 알람을 등록
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)

                    // 이미 지난 시간이면 다음날 알람으로 설정
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)  // 기존 것이 있으면 새로 있는 intent로 업데이트

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,    // 절대시간
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,  // 하루 있다가 실행
                    pendingIntent
                )
            } else {
                // 꺼진 경우 -> 알람을 제거
                cancelAlarm()
            }

            // 데이터를 저장 -> 위에 saveAlarmModel로 저장까지 완료
        }
    }

    private fun initChangeAlarmTimeButton() {
        val changeAlarmTimeButton = findViewById<Button>(R.id.changeAlarmTimeButton)
        changeAlarmTimeButton.setOnClickListener {

            // 현재 시간을 일단 가져옴
            val calendar = Calendar.getInstance()

            // TimePickDialog를 띄워서 시간을 설정하도록 하고, 그 시간을 가져와서
            TimePickerDialog(this, { picker, hour, minute ->

                // 데이터를 생성하고 SharedPreference에 저장
                val model = saveAlarmModel(hour, minute, false)

                // 뷰를 업데이트
                renderView(model)

                // 기존에 있던 알람을 삭제
                cancelAlarm()

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                .show()

        }
    }

    private fun saveAlarmModel(
        hour: Int,
        minute: Int,
        onOff: Boolean
    ): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffDBValue
        )

        // 보정 예외처리
        val pendingIntent = PendingIntent.getBroadcast(this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE)

        if ((pendingIntent == null) and alarmModel.onOff) {
            // 알람은 꺼져있는데 데이터는 켜져있는 경우
            alarmModel.onOff = false
        } else if ((pendingIntent != null) and alarmModel.onOff.not()) {
            // 알람은 켜져 있는데 데이터는 꺼져있는 경우
                // 알람을 취소함
            pendingIntent.cancel()
        }

        return alarmModel
    }

    private fun renderView(model: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTextView).apply {
            text = model.ampmText
        }

        findViewById<TextView>(R.id.timeTextView).apply {
            text = model.timeText
        }

        findViewById<Button>(R.id.onOffButton).apply {
            text = model.onOffText
            tag = model
        }
    }

    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE)
        pendingIntent?.cancel()
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val ALARM_REQUEST_CODE = 1000
    }
}