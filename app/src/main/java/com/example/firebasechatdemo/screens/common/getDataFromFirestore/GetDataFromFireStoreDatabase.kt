package com.example.firebasechatdemo.screens.common.getDataFromFirestore

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.firebasechatdemo.response.AddChatMessageModel
import com.example.firebasechatdemo.response.UserResponse
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.LastSeenAt
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.MESSAGES_KEY
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.Online
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.UNREAD_COUNT
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.USERS_KEY
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.USER_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


suspend fun getAllUsersFromFirebase(): List<UserResponse> {
    val users = mutableListOf<UserResponse>()
    val fireStore = Firebase.firestore
    val usersCollection = fireStore.collection(USERS_KEY)
    val querySnapshot = usersCollection.get().await()

    for (document in querySnapshot.documents) {
        val user = document?.toObject<UserResponse>()
        user?.let {
            users.add(it)
        }
    }
    return users
}

fun getCurrentUserId(): String {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    return currentUser?.uid ?: ""
}

fun getAllMessages(): SnapshotStateList<AddChatMessageModel> {
    val expenses = SnapshotStateList<AddChatMessageModel>()
    val fireStore = Firebase.firestore
    val expenseCollection = fireStore.collection(MESSAGES_KEY)

    expenseCollection.addSnapshotListener { querySnapshot, _ ->
        querySnapshot?.let {
            expenses.clear()
            for (document in querySnapshot.documents) {
                val expens = document.toObject<AddChatMessageModel>()
                expens?.let { expenses.add(it) }
            }
        }
    }

    return expenses
}

suspend fun updateStatus(status: String) {

    val currentUserId = getCurrentUserId()

    if (currentUserId.isEmpty()) {
        return
    }

    try {
        val userQuery = FirebaseFirestore.getInstance().collection(USERS_KEY)
            .whereEqualTo(USER_ID, getCurrentUserId())
            .get()
            .await()

        val map = mutableMapOf<String, Any>()
        map[Online] = status
        map[LastSeenAt] = System.currentTimeMillis()

        if (userQuery.documents.isNotEmpty()) {
            for (document in userQuery) {
                try {
                    FirebaseFirestore.getInstance().collection(USERS_KEY)
                        .document(document.id).set(
                            map,
                            SetOptions.merge()
                        ).await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun deleteConversation(to: String, from: String) {

    if (to.isEmpty() || from.isEmpty()) {
        return
    }

    try {
        FirebaseFirestore.getInstance().collection(MESSAGES_KEY)
            .whereEqualTo("from_uid", from)
            .whereEqualTo("to_uid", to)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        FirebaseFirestore.getInstance().collection(MESSAGES_KEY)
                            .document(document.id).delete()
                    }
                }
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun monitorUserStatus(userId: String, userStatus: (String) -> Unit) {

    val fireStore = Firebase.firestore
    val expenseCollection = fireStore.collection(USERS_KEY)
    expenseCollection
        .whereEqualTo(USER_ID, userId)
        .addSnapshotListener { value, error ->
            value?.let { userDocument->
                for (document in userDocument) {
                    val user = document.toObject<UserResponse>()
                    userStatus(user.online)
                }
            }
        }
}

suspend fun updateUnreadCount(userId: String, unreadCount: Int = 0) {

    try {
        val userQuery = FirebaseFirestore.getInstance().collection(USERS_KEY)
            .whereEqualTo(USER_ID, userId)
            .get()
            .await()

        var newUnreadCount = unreadCount

        if (userQuery.documents.isNotEmpty()) {

            if (unreadCount != 0){
                for (document in userQuery.documents) {
                    val user = document.toObject<UserResponse>()
                    user.let {
                        newUnreadCount = it?.unreadCount ?: 0
                        newUnreadCount++
                    }
                }
            }

            val map = mutableMapOf<String, Any>()
            map[UNREAD_COUNT] = newUnreadCount

            for (document in userQuery) {
                try {
                    FirebaseFirestore.getInstance().collection(USERS_KEY)
                        .document(document.id).set(
                            map,
                            SetOptions.merge()
                        ).await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}