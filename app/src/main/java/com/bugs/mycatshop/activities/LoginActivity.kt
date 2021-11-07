package com.bugs.mycatshop.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.bugs.mycatshop.R
import com.bugs.mycatshop.firestore.FirestoreClass
import com.bugs.mycatshop.models.User
import com.bugs.mycatshop.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var edtEmail : EditText
    lateinit var edtPassword: EditText

    lateinit var txtForgotPass: TextView
    lateinit var btnLogin: Button
    lateinit var txtRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edit_email)
        edtPassword = findViewById(R.id.edit_password)
        txtForgotPass = findViewById(R.id.tv_forgot_pass)
        btnLogin = findViewById(R.id.btn_login)
        txtRegister = findViewById(R.id.tv_register)

        // Click event assigned to Forgot Password text
        txtRegister.setOnClickListener(this)
        // Click event assigned to Login Button
        btnLogin.setOnClickListener(this)
        // Click event assigned to Register text
        txtRegister.setOnClickListener(this)
    }

    // In Login Screen the clickable components are login button,forgot password text, and register button
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_forgot_pass -> {
                    // do something
                }

                R.id.btn_login -> {
                    loginRegisteredUser()
                }

                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(edtEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
                false
            }

            TextUtils.isEmpty(edtPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
                false
            }

            else -> {
                true
            }
        }
    }

    private fun loginRegisteredUser() {
        if (validateLoginDetails()) {
            // Show the progress dialog
            showProgressDialog()

            // Get the text from edittext and trim the space
            val email = edtEmail.text.toString().trim { it <= ' ' }
            val password = edtPassword.text.toString().trim { it <= ' ' }

            // Login using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //showErrorSnackBar("You are logged in successfully.", false)
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLogedInSuccess(user: User) {

        // Hide the progress bar dialog
        hideProgressDialog()

        // Print the user details in the log as of now
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        if (user.profileCompleted == 0) {
            // If the user profile is incomplete then launch the UserProfileActivity
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            // Redirect to UserMainActivity after log in.
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}