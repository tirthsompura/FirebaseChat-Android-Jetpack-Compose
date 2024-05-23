package com.example.firebasechatdemo.screens.authentication.signup

import android.content.Context
import com.example.firebasechatdemo.utils.FirebaseKeyConstants
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.ONLINE
import com.example.firebasechatdemo.utils.showToast
import com.example.firebasechatdemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

fun signUpAuth(
    context: Context,
    auth: FirebaseAuth,
    viewModel: SignUpViewModel,
    onSignedIn: (FirebaseUser) -> Unit,
) {
    auth.createUserWithEmailAndPassword(viewModel.emailAddress.value, viewModel.password.value)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                // Create a user profile in Firestore
                val userProfile = hashMapOf(
                    FirebaseKeyConstants.Name to viewModel.nameMsg.value,
                    FirebaseKeyConstants.Email to viewModel.emailAddress.value,
                    FirebaseKeyConstants.USER_ID to user?.uid,
                    FirebaseKeyConstants.LastSeenAt to 0,
                    FirebaseKeyConstants.Online to ONLINE,
                )
                val fireStore = FirebaseFirestore.getInstance()
                fireStore.collection(FirebaseKeyConstants.USERS_KEY)
                    .document(user!!.uid)
                    .set(userProfile)
                    .addOnSuccessListener {
                        val data = viewModel.sessionManagerClass.loginUserData
                        data?.email = user.email.toString()
                        data?.name = viewModel.nameMsg.value
                        data?.userId = user.uid
                        data?.online = ONLINE
                        data?.lastSeenAt = 0
                        data?.profile_pic = ""
                        viewModel.sessionManagerClass.loginUserData = data
                        onSignedIn(user)
                    }
                    .addOnFailureListener {
                        // handle exception
                        it.printStackTrace()
                    }
            } else {
                // Handle sign-up failure
                showToast(context, context.getString(R.string.failed_to_create_an_account))
            }
        }
}