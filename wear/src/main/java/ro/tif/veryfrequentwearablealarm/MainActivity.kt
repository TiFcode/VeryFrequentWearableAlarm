package ro.tif.veryfrequentwearablealarm

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.provider.Contacts.Intents.Insert.ACTION
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.util.Log
import android.widget.Switch
import ro.tif.veryfrequentwearablealarm.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    var isBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switch = findViewById<Switch>(R.id.EnableVibrationService_Switch)

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Start the service
                startVibrationService()
            } else {
                // Stop the service
                stopVibrationService()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("TiFlog", "onDestroy of MainActivity we stop the vibration service!");
        stopVibrationService()
    }
    fun startVibrationService() {
        val intent = Intent(this, VibrationService::class.java)
        intent.action = Constants.STARTFOREGROUND_ACTION
        startForegroundService(intent)
    }
    fun stopVibrationService() {
        val intent = Intent(this, VibrationService::class.java)
        intent.action = Constants.STOPFOREGROUND_ACTION
        startService(intent)
    }
}