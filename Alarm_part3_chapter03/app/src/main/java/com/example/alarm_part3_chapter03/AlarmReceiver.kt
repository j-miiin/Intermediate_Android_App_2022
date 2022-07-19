package com.example.alarm_part3_chapter03

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "1000"
    }

    override fun onReceive(context: Context, intent: Intent) {

        // API 26 이상에서는 channel이 필요함
        createNotificationChannel(context)
        notifyNotification(context)
    }

    private fun createNotificationChannel(context: Context) {
        // activity가 아니므로 context(지금 실행하는 우리 앱의 상태나 맥락)를 가져와야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val noticificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "가상 알람",
                NotificationManager.IMPORTANCE_HIGH
            )

            NotificationManagerCompat.from(context).createNotificationChannel(noticificationChannel)
        }
    }

    private fun notifyNotification(context: Context) {
        with (NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("알람")
                .setContentText("일어날 시간입니다.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notify(NOTIFICATION_ID, build.build())
        }
    }
}