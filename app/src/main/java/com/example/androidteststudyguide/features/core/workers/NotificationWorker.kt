package com.example.androidteststudyguide.features.core.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.androidteststudyguide.R

/*
This is where you put the code for the actual work you want to perform in the background.
You'll extend this class and override the doWork() method.
 */
class NotificationWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {


    override fun doWork(): Result {


        // getting input
        val title = inputData.getString("title")
        val content = inputData.getString("content")
        val sleepTime = inputData.getLong("sleepTime", 1000)
        Thread.sleep(sleepTime)



        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "VERBOSE_NOTIFICATION_CHANNEL_NAME"
            val description = "VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("CHANNEL_ID", name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_heart)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        // Show the notification
        val NOTIFICATION_ID = 1
        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, builder.build())
        return Result.success()
    }



}
