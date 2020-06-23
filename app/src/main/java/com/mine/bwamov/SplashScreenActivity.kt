package com.mine.bwamov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mine.bwamov.onboarding.OnboardingoneActivtiy

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        
        var handler =  Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity,
            OnboardingoneActivtiy::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}