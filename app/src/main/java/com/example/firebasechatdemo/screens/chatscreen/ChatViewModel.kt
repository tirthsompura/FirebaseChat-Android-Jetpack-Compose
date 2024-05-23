package com.example.firebasechatdemo.screens.chatscreen

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatdemo.response.AddChatMessageModel
import com.example.firebasechatdemo.response.UserResponse
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.deleteConversation
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.getAllMessages
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.getCurrentUserId
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.monitorUserStatus
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.updateUnreadCount
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.MESSAGES_KEY
import com.example.firebasechatdemo.utils.SessionManagerClass
import com.example.firebasechatdemo.utils.ValidationConstants
import com.example.firebasechatdemo.utils.fromPrettyJson
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val sessionManagerClass: SessionManagerClass,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val chatMessageValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(""))
    val imageUriValue = mutableStateOf("")

    val db = FirebaseFirestore.getInstance()
    var messagesList by mutableStateOf<MutableList<AddChatMessageModel>>(SnapshotStateList())

    var selectedImageUri = mutableStateOf<Uri?>(null)
    var selectedDocUri = mutableStateOf<Uri?>(null)

    var name = mutableStateOf("")
    var status = mutableStateOf("")
    var userId = mutableStateOf("")

    init {
        (savedStateHandle.get<String>(ValidationConstants.UserDataString)
            ?.fromPrettyJson() as UserResponse?)?.let { user ->
            name.value = user.name
            status.value = user.online
            userId.value = user.userId

            monitorUserStatus(
                userId = userId.value
            ){
                status.value = it
            }

            viewModelScope.launch {
                updateUnreadCount(userId.value)
            }
        }
        messagesList = getAllMessages()


    }

    fun removeAllChats() {
        viewModelScope.launch {
            deleteConversation(
                to = userId.value,
                from = getCurrentUserId()
            )
        }
    }

    private fun uploadImageToFirebase(
        imageUri: Uri?,
        onSuccess: (imageUrl: Uri) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {

        if (imageUri == null) {
            onFailure(Exception("ImageUri is not available"))
            return
        }

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/${imageUri.lastPathSegment}")

        val uploadTask = imagesRef.putFile(imageUri)

        uploadTask.addOnFailureListener { exception ->
            onFailure(exception)
        }.addOnSuccessListener { _ ->
            imagesRef.downloadUrl.addOnSuccessListener {
                onSuccess(it)
            }
        }
    }

    fun sendImageMessage() {
        viewModelScope.launch {
            uploadImageToFirebase(
                selectedImageUri.value,
                onSuccess = { imageUrl ->
                    sendMessageToFirebase(imageUrl.toString())
                },
                onFailure = { exception ->
                    sendMessageToFirebase("")
                }
            )
        }
    }

    private fun sendMessageToFirebase(imgPath: String){
        val message = AddChatMessageModel(
            imageUrl = imgPath,
            from_name = sessionManagerClass.loginUserData?.name ?: "",
            from_uid = sessionManagerClass.loginUserData?.userId ?: "",
            to_uid = userId.value,
            to_name = name.value,
            last_time = System.currentTimeMillis(),
            last_msg = chatMessageValue.value.text,
        )

        messagesList.add(message)
        db.collection(MESSAGES_KEY)
            .add(message)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot added with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

        chatMessageValue.value = TextFieldValue("")
        imageUriValue.value = ""
    }
}