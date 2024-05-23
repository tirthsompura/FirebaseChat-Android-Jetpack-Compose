package com.example.firebasechatdemo.screens.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.firebasechatdemo.navigations.AuthenticationScreens
import com.example.firebasechatdemo.response.AddChatMessageModel
import com.example.firebasechatdemo.response.UserResponse
import com.example.firebasechatdemo.screens.common.NormalText
import com.example.firebasechatdemo.screens.common.deleteConfirmationDialog
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.getAllMessages
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.getAllUsersFromFirebase
import com.example.firebasechatdemo.screens.common.getDataFromFirestore.getCurrentUserId
import com.example.firebasechatdemo.utils.AuthFirebase
import com.example.firebasechatdemo.utils.SessionManagerClass
import com.example.firebasechatdemo.utils.getTimeFromTimestamp
import com.example.firebasechatdemo.utils.toPrettyJson
import com.example.firebasechatdemo.R
import com.example.firebasechatdemo.ui.theme.fontMedium
import com.example.firebasechatdemo.ui.theme.lightGrey
import com.google.firebase.auth.FirebaseUser

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    userData: MutableState<FirebaseUser?>,
    sessionManagerClass: SessionManagerClass,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    //Get All Users with short by without current user.
    val auth = AuthFirebase.auth
    LaunchedEffect(Unit) {
        viewModel.users = getAllUsersFromFirebase()
        viewModel.currentUser = getCurrentUserId()
        val currentUserIndex = viewModel.users.indexOfFirst { it.userId == viewModel.currentUser }

        if (currentUserIndex != -1) {
            viewModel.users =
                viewModel.users.filterIndexed { index, _ -> index != currentUserIndex }
                    .sortedBy { it.name }
        } else {
            viewModel.listOfUserIds.addAll(viewModel.users.sortedBy { it.userId })
        }
    }

    val openConfirmationLogoutDialog = remember {
        mutableStateOf(false)
    }

    fun onClickConfirmationLogoutDismissDialog() {
        openConfirmationLogoutDialog.value = false
    }

    if (openConfirmationLogoutDialog.value) {
        deleteConfirmationDialog(
            dialogTitle = stringResource(R.string.are_you_sure_you_want_to_logout),
            onDismiss = { onClickConfirmationLogoutDismissDialog() },
            onAccept = {
                auth.signOut()
                userData.value = null
                viewModel.sessionManagerClass.loginUserData = null
                sessionManagerClass.clearSessionData()
                navController.navigate(AuthenticationScreens.LoginScreen.route)
                openConfirmationLogoutDialog.value = false
            }
        )
    }
    //Get All Messages.
    LaunchedEffect(Unit) {
        viewModel.messagesList = getAllMessages()
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            HomeTopBar(
                sessionManagerClass,
                onClick = {
                    openConfirmationLogoutDialog.value = true
                },
            )
        },
        content = { HomeScreenBody(viewModel, navController) },
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenBody(
    viewModel: HomeViewModel,
    navController: NavController,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Column(
        modifier = Modifier
            .padding(top = 100.dp, start = 5.dp, end = 5.dp)
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (viewModel.users.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .offset(y = (-40).dp)
                        .fillMaxWidth()
                ) {
                    items(viewModel.users.size) { index ->
                        val userListData = viewModel.users[index]
                        val lastMessageForUser =
                            getLastMessageForUser(userListData.userId, viewModel.messagesList)
                        Column {
                            TabViewTile(
                                lastMessageForUser,
                                dateTime = getTimeFromTimestamp(lastMessageForUser?.last_time ?: 0),
                                unReadCount = userListData.unreadCount.toString(),
                                name = userListData.name,
                                backgroundColor = Color.Transparent,
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = {
                                        val userDataClass =
                                            UserResponse(
                                                userId = userListData.userId,
                                                name = userListData.name,
                                                online = userListData.online,
                                                email = userListData.email,
                                            )
                                        navController.navigate(
                                            AuthenticationScreens.ChatScreen.getUserDataClassString(
                                                userDataClass.toPrettyJson()
                                            )
                                        )
                                    }
                                ),
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp), contentAlignment = Alignment.Center
                ) {
                    NormalText(
                        label = stringResource(R.string.no_data_found),
                        style = fontMedium.copy(color = lightGrey, fontSize = 16.sp)
                    )
                }
            }
        }
    }
}

fun getLastMessageForUser(
    userId: String,
    messagesList: List<AddChatMessageModel>,
): AddChatMessageModel? {
    val messagesForUser = messagesList.filter { it.from_uid == userId || it.to_uid == userId }
    return messagesForUser.maxByOrNull { it.last_time }
}