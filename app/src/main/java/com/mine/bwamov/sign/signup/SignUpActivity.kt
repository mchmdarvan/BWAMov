package com.mine.bwamov.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import com.mine.bwamov.R
import com.mine.bwamov.sign.signin.User
import com.mine.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    lateinit var sUsername: String
    lateinit var sPassword: String
    lateinit var sNama:String
    lateinit var sEmail:String

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mFirebaseDatabase = mFirebaseInstance.getReference("user")

        preferences = Preferences(this)

        actionSignUp.setOnClickListener {
            sUsername = etUsername.text.toString()
            sPassword = etPassword.text.toString()
            sNama = etName.text.toString()
            sEmail = etEmail.text.toString()

            if (sUsername == ""){
                etUsername.error = "Please Input Your Username"
                etUsername.requestFocus()
            } else if (sPassword == ""){
                etPassword.error = "Please Input Your Password"
                etPassword.requestFocus()
            } else if (sNama == ""){
                etName.error = "Please Input Your Name"
                etName.requestFocus()
            } else if (sEmail == ""){
                etEmail.error = "Please Input Your Email"
                etEmail.requestFocus()
            } else {
                saveUser(sUsername, sPassword, sNama, sEmail)
            }
        }
    }

    private fun saveUser(sUsername: String, sPassword: String, sNama: String, sEmail: String) {
        val user = User()

        user.username = sUsername
        user.password = sPassword
        user.nama = sNama
        user.email = sEmail

        if (sUsername != null){
            checkingUsername(sUsername, user)
        }
    }

    private fun checkingUsername(sUsername: String, data: User) {
        mFirebaseDatabase.child(sUsername).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null){
                    mFirebaseDatabase.child(sUsername).setValue(data)

                    preferences.setValues("nama", data.nama.toString())
                    preferences.setValues("user", data.username.toString())
                    preferences.setValues("email", data.email.toString())
                    preferences.setValues("url", "")
                    preferences.setValues("status", "1")

                    val intent = Intent(this@SignUpActivity,
                        SignUpPhotoScreenActivity::class.java).putExtra("nama", data.nama)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUpActivity, "Your Username is Already Taken", Toast.LENGTH_SHORT).show()
                    etUsername.error = "Please Change Your Username"
                    etUsername.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}