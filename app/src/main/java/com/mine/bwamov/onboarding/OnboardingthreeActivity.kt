package com.mine.bwamov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mine.bwamov.R
import com.mine.bwamov.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboardingthree.*

class OnboardingthreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboardingthree)

        btnSign.setOnClickListener {
            finishAffinity()

            val intent = Intent(this@OnboardingthreeActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }
    }
}