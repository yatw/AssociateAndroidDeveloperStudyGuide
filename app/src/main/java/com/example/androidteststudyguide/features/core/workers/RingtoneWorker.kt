package com.example.androidteststudyguide.features.core.workers

import android.content.Context
import android.media.MediaPlayer
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.media.AudioAttributes
import android.media.RingtoneManager

/*
This is where you put the code for the actual work you want to perform in the background.
You'll extend this class and override the doWork() method.
 */
class RingtoneWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {


    override fun doWork(): Result {

        Thread.sleep(1000)

        val uri = RingtoneManager.getActualDefaultRingtoneUri(applicationContext, RingtoneManager.TYPE_NOTIFICATION)
        val player = MediaPlayer.create(applicationContext, uri)
        player.start()

        return Result.success()
    }


    private fun getMediaPlayer(context: Context): MediaPlayer {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val mediaPlayer = MediaPlayer.create(context, notification)
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        return mediaPlayer
    }

}
