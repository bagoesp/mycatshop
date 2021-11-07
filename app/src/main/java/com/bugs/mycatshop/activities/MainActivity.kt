package com.bugs.mycatshop.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bugs.mycatshop.R
import com.bugs.mycatshop.utils.Constants

class MainActivity : AppCompatActivity() {

    lateinit var tvMain : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain = findViewById(R.id.tv_main)

        val sharedPreferences = getSharedPreferences(Constants.MYCATSHOP_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        tvMain.text = "Hello, $username."
    }
}