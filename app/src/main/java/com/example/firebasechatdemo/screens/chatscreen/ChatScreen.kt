package com.example.firebasechatdemo.screens.chatscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.firebasechatdemo.screens.chatscreen.component.BottomBarItem
import com.example.firebasechatdemo.screens.chatscreen.component.CameraGalleryBottomSheet
import com.example.firebasechatdemo.screens.chatscreen.component.LeftChatItem
import com.example.firebasechatdemo.screens.chatscreen.component.RightChatItem
import com.example.firebasechatdemo.screens.common.ChatDetailTopBar
import com.example.firebasechatdemo.screens.common.deleteConfirmationDialog
import com.example.firebasechatdemo.R

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val openConfirmationLogoutDialog = remember {
        mutableStateOf(false)
    }

    fun onClickConfirmationLogoutDismissDialog() {
        openConfirmationLogoutDialog.value = false
    }

    if (openConfirmationLogoutDialog.value) {
        deleteConfirmationDialog(dialogTitle = stringResource(R.string.are_you_sure_you_want_to_clear_chat),
            onDismiss = { onClickConfirmationLogoutDismissDialog() },
            onAccept = {
                chatViewModel.removeAllChats()
                openConfirmationLogoutDialog.value = false
            }
        )
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                chatViewModel.imageUriValue.value = uri.toString()
            }
        }
    }

    val showSheet = remember { mutableStateOf(false) }

    if (showSheet.value) {
        CameraGalleryBottomSheet(
            context = context,
            showSheet,
            sendImageUri = {
                chatViewModel.selectedImageUri.value = it
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(galleryIntent)
                showSheet.value = false
            },
        )
    }

    Scaffold(
        topBar = {
            ChatDetailTopBar(
                chatViewModel.name,
                chatViewModel.status,
                navController,
            ) {
                openConfirmationLogoutDialog.value = true
            }
        },
        content = {
            ChatBody(chatViewModel)
        },
        bottomBar = {
            BottomBarItem(
                chatViewModel,
                onClick = {
                    chatViewModel.sendImageMessage()
                    chatViewModel.selectedImageUri.value = null
                },
                onSelectOptions = {
                    showSheet.value = true
                }
            )
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatBody(
    chatViewModel: ChatViewModel,
) {
    val sortedMessages = chatViewModel.messagesList
        .filter {
            (it.to_uid == chatViewModel.sessionManagerClass.loginUserData?.userId || it.from_uid == chatViewModel.sessionManagerClass.loginUserData?.userId) &&
                    (it.to_uid == chatViewModel.userId.value || it.from_uid == chatViewModel.userId.value)
        }
        .sortedByDescending { it.last_time }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 60.dp,
                bottom = if (!chatViewModel.imageUriValue.value.isNullOrEmpty()) 170.dp else 90.dp
            )
    ) {
        LazyColumn(
            modifier = Modifier,
            reverseLayout = true,
        ) {
            items(sortedMessages.size) { index ->
                val chatMsg = sortedMessages[index]
                if (chatMsg.from_uid == chatViewModel.sessionManagerClass.loginUserData?.userId) {
                    RightChatItem(chatMsg)
                } else {
                    LeftChatItem(chatMsg)
                }
            }
        }
    }
}



