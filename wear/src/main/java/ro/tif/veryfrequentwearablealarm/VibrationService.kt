package ro.tif.veryfrequentwearablealarm
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.PackageManagerCompat.LOG_TAG

class VibrationService : Service() {
    private val _channelId : String = R.string.channel_id.toString()

    override fun onCreate() {
        super.onCreate()
        // Create the notification channel
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(_channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            Constants.STARTFOREGROUND_ACTION -> {
                // The start service code
                Log.i("TiFlog", "Received START foreground service intent");

                // Set the service as a foreground service
                val notification = NotificationCompat.Builder(this, _channelId)
                    .setContentTitle("Strain your muscles!")
                    .setContentText("It's time to strain your muscles! This notification is triggered each 15 minutes by the foreground service of the Very Frequent Wearable Alarm application. It is accompanied by a long vibration (3 seconds). To disable this, open the app (click this notification) and disable the switch.")
                    .setSmallIcon(R.drawable.ic_vibrate)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .build()
                startForeground(1, notification)

                // Schedule vibrations to occur at regular intervals
                val intervalInMilliseconds: Long = 15 * 60 * 1000 // 15 minutes in milliseconds
                val handler = Handler(Looper.getMainLooper())
                val vibrator: Vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                val vibrationEffect1 =
                    VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE)
                val runnable = object : Runnable {
                    override fun run() {
                        // Vibrate the watch
                        vibrator.vibrate(vibrationEffect1)
                        // Schedule the next vibration
                        handler.postDelayed(this, intervalInMilliseconds)
                    }
                }
                handler.postDelayed(runnable, intervalInMilliseconds)
                return START_STICKY
            }
            Constants.STOPFOREGROUND_ACTION -> {
                // The stop service code
                Log.i("TiFlog", "Received STOP foreground service intent");
                return START_NOT_STICKY
            }
        }
        Log.i("TiFlog", "[ERROR] The received intent content is not defined. Throwing exception!");
        throw IllegalArgumentException("[ERROR] The received intent content is not defined. Throwing exception!")
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}
