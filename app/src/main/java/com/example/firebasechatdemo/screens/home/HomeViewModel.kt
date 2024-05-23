package com.example.firebasechatdemo.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasechatdemo.response.AddChatMessageModel
import com.example.firebasechatdemo.response.UserResponse
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.updateStatus
import com.example.firebasechatdemo.utils.SessionManagerClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val sessionManagerClass: SessionManagerClass,
) : ViewModel() {
    val listOfUserIds = mutableListOf<UserResponse>()
    var users by mutableStateOf<List<UserResponse>>(emptyList())
    var currentUser by mutableStateOf("")

    var messagesList by mutableStateOf<MutableList<AddChatMessageModel>>(SnapshotStateList())
    init {
        viewModelScope.launch {
            updateStatus("online")
        }
    }
}
