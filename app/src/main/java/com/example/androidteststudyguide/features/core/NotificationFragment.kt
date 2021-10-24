package com.example.androidteststudyguide.features.core


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.androidteststudyguide.R
import com.example.androidteststudyguide.databinding.FragmentNotificationBinding
import com.example.androidteststudyguide.features.MainActivity
import android.graphics.BitmapFactory


/**
 *  Steps:
 *
 *   1 create a channel
 *   2 create a notification builder
 *
 *
 */

class NotificationFragment: Fragment() {

    private lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNotificationChannel()




        binding.btn1.text = "Click to send text notification"
        binding.btn1.setOnClickListener {
            createTextNotification()
        }

        binding.btn2.text = "Click to send image notification"
        binding.btn2.setOnClickListener {
            createImageNotification()
        }

    }

    private fun createTextNotification(){

        // Create an explicit intent for an Activity in your app
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        /*
            A PendingIntent is a token that you give to a foreign application
            (e.g. NotificationManager, AlarmManager, Home Screen AppWidgetManager,
            or other 3rd party applications),
            which allows the foreign application to use your application's permissions
            to execute a predefined piece of code.
         */
        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)


        val builder = NotificationCompat.Builder(requireContext(), getString(R.string.notification_channel_id))
            .setSmallIcon(R.drawable.ic_heart)
            .setContentTitle("This is title")
            // .setContentText("Much longer text that cannot fit one line...Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line....Much longer text that cannot fit one line..." +
                        "Much longer text that cannot fit one line...Much longer text that cannot fit one line..." +
                        "Much longer text that cannot fit one line...Much longer text that cannot fit one line..." +
                        "Much longer text that cannot fit one line...Much longer text that cannot fit one line..." +
                        "Much longer text that cannot fit one line...Much longer text that cannot fit one line..." +
                        "Much longer text that cannot fit one line...Much longer text that cannot fit one line..." +
                        "Much longer text that cannot fit one line...Much longer text that cannot fit one line..." +
                        "Much longer text that cannot fit one line...Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            //automatically removes the notification when the user taps it.
            .setAutoCancel(true)
        // can offer up to three action buttons
        //.addAction(0, "Snooze" , snoozePendingIntent)

        with(NotificationManagerCompat.from(requireContext())) {
            // notificationId is a unique int for each notification that you must define
            notify(456, builder.build())
            requireActivity().finish()
        }
    }


    private fun createImageNotification(){

        // Create an explicit intent for an Activity in your app
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        /*
            A PendingIntent is a token that you give to a foreign application
            (e.g. NotificationManager, AlarmManager, Home Screen AppWidgetManager,
            or other 3rd party applications),
            which allows the foreign application to use your application's permissions
            to execute a predefined piece of code.
         */
        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)


        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_moutain)

        val builder = NotificationCompat.Builder(requireContext(), getString(R.string.notification_channel_id))
            .setSmallIcon(R.drawable.ic_heart)
            .setContentTitle("This is title")
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            //automatically removes the notification when the user taps it.
            .setAutoCancel(true)
        // can offer up to three action buttons
        //.addAction(0, "Snooze" , snoozePendingIntent)

        with(NotificationManagerCompat.from(requireContext())) {
            // notificationId is a unique int for each notification that you must define
            notify(33, builder.build())
            requireActivity().finish()
        }
    }

    /**
     * you should execute this code as soon as your app starts.
     * It's safe to call this repeatedly because creating an existing notification channel performs no operation.
     */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                getString(R.string.notification_channel_id),
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = getString(R.string.notification_channel_desc)
            }
            // Register the channel with the system
            // I am using application class's context here
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}