package com.mine.bwamov.sign.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.mine.bwamov.home.HomeActivity
import com.mine.bwamov.R
import com.mine.bwamov.sign.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    lateinit var iUsername:String
    lateinit var iPassword:String

    lateinit var mDatabase: DatabaseReference
    lateinit var preferences: com.mine.bwamov.utils.Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance().getReference("user")
        preferences = com.mine.bwamov.utils.Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")){
            finishAffinity()

            val intent = Intent(this,
                HomeActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            iUsername = edtUsername.text.toString()
            iPassword = edtPassword.text.toString()

            if (iUsername.equals("")){
                edtUsername.error = "Silahkan tulis Username Anda"
                edtUsername.requestFocus()
            } else if (iPassword.equals("")){
                edtPassword.error = "Silahkan tulis Password Anda"
                edtPassword.requestFocus()
            } else{
                pushLogin(iUsername, iPassword)
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this@SignInActivity,
                SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(iUsername:String, iPassword:String){
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot){

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null){
                    Toast.makeText(this@SignInActivity, "User Not Found", Toast.LENGTH_SHORT).show()

                } else{
                    if (user.password.equals(iPassword)){
                        Toast.makeText(this@SignInActivity, "Welcome Back, "+ iUsername, Toast.LENGTH_SHORT).show()


                        preferences.setValues("nama", user.nama.toString())
                        preferences.setValues("user", user.username.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("saldo", user.saldo.toString())
                        preferences.setValues("status", "1")

                        finishAffinity()

                        val intent = Intent(this@SignInActivity,
                            HomeActivity::class.java)
                        startActivity(intent)
                    } else{
                        Toast.makeText(this@SignInActivity, "Wrong Password", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, ""+ error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}