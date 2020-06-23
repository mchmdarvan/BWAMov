package com.mine.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mine.bwamov.R
import com.mine.bwamov.sign.signin.SignInActivity
import com.mine.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_onboardingone.*

class OnboardingoneActivtiy : AppCompatActivity() {

    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboardingone)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")){
            finishAffinity()

            val intent = Intent(this@OnboardingoneActivtiy,
                SignInActivity::class.java)
            startActivity(intent)
        }
        btnHome.setOnClickListener{
            finishAffinity()

            val intent = Intent(this@OnboardingoneActivtiy,
                SignInActivity::class.java)
            startActivity(intent)
        }

        btnNext.setOnClickListener{
            val intent = Intent(this@OnboardingoneActivtiy,
                OnboardingtwoActivity::class.java)
            startActivity(intent)
        }
    }
}