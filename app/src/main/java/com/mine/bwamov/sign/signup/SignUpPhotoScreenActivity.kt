package com.mine.bwamov.sign.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.mine.bwamov.home.HomeActivity
import com.mine.bwamov.R
import com.mine.bwamov.utils.Preferences
import kotlinx.android.synthetic.main.activity_photo_screen_sign_up.*
import java.util.*

class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd:Boolean = false
    lateinit var filePath:Uri

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_screen_sign_up)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        tvHello.text = "Welcome,\n"+intent.getStringExtra("nama")

        iv_add.setOnClickListener{
            if (statusAdd){
                statusAdd = false
                btnSave.visibility = View.INVISIBLE
                iv_add.setImageResource(R.drawable.ic_add_photo)
                imgProfile.setImageResource(R.drawable.user_upload)
            } else{
                ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }
        }

        btnLater.setOnClickListener {

            finishAffinity()

            val intent = Intent(this@SignUpPhotoScreenActivity,
                HomeActivity::class.java)
            startActivity(intent)
        }

        btnSave.setOnClickListener {
            if (filePath != null){
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoScreenActivity, "Uploaded", Toast.LENGTH_SHORT).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())
                        }

                        finishAffinity()
                        val intent = Intent(this@SignUpPhotoScreenActivity,
                            HomeActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener{e ->
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoScreenActivity, ""+e.message, Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                        progressDialog.setMessage("Uploaded "+progress.toInt() + "%")
                    }
            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "You can't Add Profile Picture", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this, "In Hurry? Just Click Upload Later", Toast.LENGTH_SHORT).show()
    }

//    @SuppressLint("MissingSuperCall")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
//            var bitmap = data?.extras?.get("data") as Bitmap
//            statusAdd = true
//
//            filePath = data.getData()!!
//            Glide.with(this)
//                .load(bitmap)
//                .apply(RequestOptions.circleCropTransform())
//                .into(imgProfile)
//
//            btnSave.visibility = View.VISIBLE
//            iv_add.setImageResource(R.drawable.ic_delete_photo)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            //Image Uri Will not Be NULL for Result OK
            statusAdd = true
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(imgProfile)

            Log.v("Arvan", "File uri upload "+filePath)


            btnSave.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_delete_photo)
        } else if (resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this@SignUpPhotoScreenActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@SignUpPhotoScreenActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}