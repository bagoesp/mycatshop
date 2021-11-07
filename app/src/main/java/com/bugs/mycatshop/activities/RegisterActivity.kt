package com.bugs.mycatshop.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.bugs.mycatshop.R
import com.bugs.mycatshop.firestore.FirestoreClass
import com.bugs.mycatshop.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    lateinit var tvLogin : TextView
    lateinit var tbRegister: androidx.appcompat.widget.Toolbar
    lateinit var edtFirstName: EditText
    lateinit var edtLastName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtPassword: EditText
    lateinit var edtConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var cbTerms: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvLogin = findViewById(R.id.tv_login)
        tbRegister = findViewById(R.id.toolbar_register)
        edtFirstName = findViewById(R.id.et_first_name)
        edtLastName = findViewById(R.id.et_last_name)
        edtEmail = findViewById(R.id.et_email)
        edtPassword = findViewById(R.id.et_password)
        edtConfirmPassword = findViewById(R.id.et_confirm_password)
        btnRegister = findViewById(R.id.btn_register)
        cbTerms = findViewById(R.id.check_agree)

        setupActionBar()

        btnRegister.setOnClickListener {
            registerUser()
        }

        tvLogin.setOnClickListener() {
            onBackPressed()
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(tbRegister)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_navigate_before_24)
        }

        tbRegister.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * a function to validate a new user
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(edtFirstName.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_first_name), true)
                false
            }

            TextUtils.isEmpty(edtLastName.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_last_name), true)
                false
            }

            TextUtils.isEmpty(edtEmail.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_email), true)
                false
            }

            TextUtils.isEmpty(edtPassword.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
                false
            }

            TextUtils.isEmpty(edtConfirmPassword.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_confirm_password), true)
                false
            }

            edtPassword.text.toString().trim { it <= ' '} != edtConfirmPassword.text.toString()
                .trim { it <= ' '} -> {
                    showErrorSnackBar(
                        resources.getString(R.string.err_msg_password_and_confirm_password_missmatch),
                        true
                    )
                false
                }

            !cbTerms.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
                showErrorSnackBar(resources.getString(R.string.register_successfull), false)
                true
            }
        }
    }

    private fun registerUser() {

        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            showProgressDialog()

            val email: String = edtEmail.text.toString().trim { it <= ' ' }
            val password: String = edtPassword.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                id = firebaseUser.uid,
                                firstName = edtFirstName.text.toString().trim { it <= ' ' },
                                lastName = edtLastName.text.toString().trim { it <= ' ' },
                                email = edtEmail.text.toString().trim { it <= ' ' }
                            )

                            FirestoreClass().registerUser(this@RegisterActivity, user)

                            //FirebaseAuth.getInstance().signOut()
                            //finish()

                        } else {
                            hideProgressDialog()
                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        }
    }

    fun userRegistrationSuccess() {

        // hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.register_successfull_2),
            Toast.LENGTH_LONG
        ).show()

    }

}