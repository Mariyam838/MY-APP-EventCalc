package com.example.eventcalc

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {

    // Define the splash screen duration in milliseconds
    private val SPLASH_DURATION = 2500L

    private lateinit var ivLogo: ImageView
    private lateinit var tvAppName: TextView
    private lateinit var tvTagline: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize views
        ivLogo = findViewById(R.id.ivLogo)
        tvAppName = findViewById(R.id.tvAppName)
        tvTagline = findViewById(R.id.tvTagline)

        // Load animations
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val scaleUp = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        scaleUp.duration = 1000

        // Set animations
        ivLogo.startAnimation(scaleUp)
        tvAppName.startAnimation(fadeIn)
        tvTagline.startAnimation(fadeIn)

        // Navigate to main activity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DURATION)
    }
}