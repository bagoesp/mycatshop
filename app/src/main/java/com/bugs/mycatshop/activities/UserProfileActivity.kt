package com.bugs.mycatshop.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bugs.mycatshop.R
import com.bugs.mycatshop.models.User
import com.bugs.mycatshop.utils.Constants
import com.bugs.mycatshop.utils.GlideLoader
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    lateinit var editLastName: EditText
    lateinit var editFirstName: EditText
    lateinit var editEmail: EditText
    lateinit var editPhone: EditText
    lateinit var imageUserPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        editFirstName = findViewById(R.id.et_first_name)
        editLastName = findViewById(R.id.et_last_name)
        editEmail = findViewById(R.id.et_email)
        editPhone = findViewById(R.id.et_phone)
        imageUserPhoto = findViewById(R.id.iv_user_photo)

        var userDetails: User = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            // Get the user details from intent as a ParcelableExtra.
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
            
        }

        editFirstName.isEnabled = false
        editFirstName.setText(userDetails.firstName)

        editLastName.isEnabled = false
        editLastName.setText(userDetails.lastName)

        editEmail.isEnabled = false
        editEmail.setText(userDetails.email)

        imageUserPhoto.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo -> {
                    // Here we will check if the permission is already allowed or we need to request of it.
                    // First of all we will check the READ_EXTERNAL_STORAGE permission and if it is not allowed
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED ) {
                        //showErrorSnackBar("You already have the storage permission.", false)
                        Constants.showImageChooser(this)
                    } else {
                        // Request permission to be granted to this application. These permissions
                        // must be requested in your manifest, they should not be granted to your app,
                        // and they sould have protection level
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //if permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //showErrorSnackBar("The storage permission is granted.", false)
                Constants.showImageChooser(this)
            } else {
                //diplaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        // The Uri of selected image from phone storage.
                        val selectedImageFileUri = data.data!!

                        //imageUserPhoto.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(selectedImageFileUri, imageUserPhoto)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            "Image selection failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}