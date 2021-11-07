package com.bugs.mycatshop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bugs.mycatshop.activities.LoginActivity
import com.bugs.mycatshop.activities.RegisterActivity
import com.bugs.mycatshop.models.User
import com.bugs.mycatshop.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FirestoreClass {

    private val mFireStore = Firebase.firestore

    fun registerUser(activity: RegisterActivity, userInfo: User){

        //val data = makeMap(userInfo)
        val document :String = userInfo.id

        // The "users" is collection name. If the collection is already collected, than it will not create the same one again
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(document)
            // Here the userInfo are Field and the setOption is set to merge. It is for if we wants to merge later on instead replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transfering the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variabel to assign the currentUserID if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document ID to get the Fields of user
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                // Here we have received the document snapshot which is converted into the User Data model object
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYCATSHOP_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // Key: logged_in_username
                // Value: bagus naga
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                // Pass the result to the login activity
                // START
                when (activity) {
                    is LoginActivity -> {
                        // Call the function of base activity for transfering the result to it.
                        activity.userLogedInSuccess(user)
                    }
                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error, and print the error in log
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details. ",
                    e
                )

            }
    }
}