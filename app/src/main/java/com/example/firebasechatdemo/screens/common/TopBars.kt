package com.example.firebasechatdemo.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebasechatdemo.R
import com.example.firebasechatdemo.ui.theme.blueBgColorLight
import com.example.firebasechatdemo.ui.theme.fontSemiBold
import com.example.firebasechatdemo.ui.theme.whiteColor
import com.example.firebasechatdemo.utils.FirebaseKeyConstants

@Composable
fun ChatDetailTopBar(
    name: MutableState<String>,
    status: MutableState<String>,
    navController: NavController,
    onDialogClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = blueBgColorLight)
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .offset(x = (-8).dp)
                .fillMaxWidth()
                .weight(0.9f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_white_arrow),
                contentDescription = "",
                colorFilter = ColorFilter.tint(whiteColor),
                modifier = Modifier
                    .rotate(270f)
                    .size(35.dp)
                    .clickable(
                        onClick = {
                            navController.popBackStack()
                        })
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                NormalText(
                    label = name.value,
                    style = fontSemiBold.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(top = 1.dp)
                )

                val userStatus = if (status.value == FirebaseKeyConstants.ONLINE) FirebaseKeyConstants.ONLINE else FirebaseKeyConstants.OFFLINE
                val userStatusColor = if (status.value == FirebaseKeyConstants.ONLINE) whiteColor    else Color.Black
                NormalText(
                    label = userStatus,
                    style = fontSemiBold.copy(fontSize = 12.sp, color = userStatusColor),
                    modifier = Modifier.padding(top = 1.dp, start = 2.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
                .weight(0.1f)
                .height(30.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .border(
                    width = 1.dp, color = whiteColor, shape = RoundedCornerShape(8.dp)
                )
                .padding(3.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        onDialogClick()
                    }), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "",
                colorFilter = ColorFilter.tint(whiteColor),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
