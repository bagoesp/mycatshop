package com.bugs.mycatshop.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bugs.mycatshop.R
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(imageUri: Uri, imageView: ImageView){
        try {
            // Load the user image from imageview.
            Glide
                .with(context)
                .load(imageUri) // URI of the image
                .centerCrop() // Scale type of the image
                .placeholder(R.drawable.img_user_placehoder) // a default placeholder
                .into(imageView) // the view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}