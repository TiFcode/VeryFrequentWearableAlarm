package ro.tif.veryfrequentwearablealarm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import ro.tif.veryfrequentwearablealarm.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start the VibrationService
        val intent = Intent(this, VibrationService::class.java)
        startForegroundService(intent)

    }
    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, VibrationService::class.java))
    }
}