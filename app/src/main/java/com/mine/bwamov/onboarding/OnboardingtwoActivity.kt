package com.mine.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mine.bwamov.R
import kotlinx.android.synthetic.main.activity_onboardingtwo.*

class OnboardingtwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboardingtwo)

        btnNext.setOnClickListener {
            val intent = Intent(this@OnboardingtwoActivity,
                OnboardingthreeActivity::class.java)
            startActivity(intent)
        }
    }
}