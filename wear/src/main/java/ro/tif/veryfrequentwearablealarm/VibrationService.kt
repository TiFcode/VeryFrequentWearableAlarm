package ro.tif.veryfrequentwearablealarm
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat

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
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Set the service as a foreground service
        val notification = NotificationCompat.Builder(this, _channelId)
            .setContentTitle("VibrationService")
            .setContentText("Vibrating the device")
            .setSmallIcon(R.drawable.ic_vibrate)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()
        startForeground(1, notification)

        // Schedule vibrations to occur at regular intervals
        val intervalInMilliseconds: Long = 15 * 60  * 1000 // 15 minutes in milliseconds
        val handler = Handler(Looper.getMainLooper())
        val vibrator: Vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val vibrationEffect1 = VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE)
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
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
