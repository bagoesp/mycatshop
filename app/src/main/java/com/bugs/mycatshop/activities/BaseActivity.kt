package com.bugs.mycatshop.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bugs.mycatshop.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var pbDialog: Dialog

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.ungu
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.jingga
                )
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(){
        pbDialog = Dialog(this)

        /* Set the screen content from the layout resource
            The resource will be inflated, adding all top-level views to the screen.
         */
        pbDialog.setContentView(R.layout.dialog_progress)

        pbDialog.setCancelable(false)
        pbDialog.setCanceledOnTouchOutside(false)

        // Start the dialog and display it on screen
        pbDialog.show()
    }

    fun hideProgressDialog(){
        pbDialog.dismiss()
    }
}