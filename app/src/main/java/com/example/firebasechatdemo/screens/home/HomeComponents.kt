package com.example.firebasechatdemo.screens.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebasechatdemo.response.AddChatMessageModel
import com.example.firebasechatdemo.screens.common.SpacerHorizontal
import com.example.firebasechatdemo.screens.common.SpacerVertical
import com.example.firebasechatdemo.utils.SessionManagerClass
import com.example.firebasechatdemo.R
import com.example.firebasechatdemo.ui.theme.fontRegular
import com.example.firebasechatdemo.ui.theme.fontSemiBold
import com.example.firebasechatdemo.ui.theme.lightGrey
import com.example.firebasechatdemo.ui.theme.loginBgColor

@Composable
fun TabViewTile(
    lastMessageForUser: AddChatMessageModel?,
    dateTime: String,
    unReadCount: String,
    name: String,
    backgroundColor: Color,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .weight(0.6f)
            ) {
                Text(
                    text = name,
                    style = fontSemiBold.copy(color = Color.Black, fontSize = 18.sp),
                    modifier = modifier.background(backgroundColor)
                )
                SpacerVertical(3.dp)
                if (lastMessageForUser?.imageUrl?.isNotEmpty() == true) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.gallery),
                            contentDescription = "",
                            modifier.size(15.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                        SpacerHorizontal(5.dp)
                        Text(
                            text = stringResource(R.string.photo),
                            style = fontRegular.copy(color = Color.Black, fontSize = 14.sp),
                            modifier = modifier.background(backgroundColor),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else if (lastMessageForUser?.last_msg?.isNotEmpty() == true) {
                    Text(
                        text = lastMessageForUser.last_msg,
                        style = fontRegular.copy(color = Color.Black, fontSize = 14.sp),
                        modifier = modifier.background(backgroundColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = "",
                        style = fontRegular.copy(color = Color.Black, fontSize = 14.sp),
                        modifier = modifier.background(backgroundColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(
                modifier
                    .fillMaxWidth()
                    .weight(0.2f), horizontalAlignment = Alignment.End
            ) {
                if (unReadCount == "0") {
                    Box(
                        modifier = modifier.size(20.dp), contentAlignment = Alignment.Center
                    ) {}
                } else {
                    Box(
                        modifier = modifier
                            .size(20.dp)
                            .clip(shape = CircleShape)
                            .background(loginBgColor), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = unReadCount,
                            style = fontRegular.copy(color = Color.White, fontSize = 12.sp),
                            modifier = modifier.background(backgroundColor)
                        )
                    }
                }

                SpacerVertical(8.dp)
                if (lastMessageForUser?.last_msg?.isNotEmpty() == true){
                    Text(
                        text = dateTime,
                        style = fontRegular.copy(color = lightGrey, fontSize = 12.sp),
                        modifier = modifier.background(backgroundColor)
                    )
                }
            }
        }
        Divider(
            modifier = Modifier
                .padding(end = 10.dp, start = 10.dp, top = 5.dp)
                .height(0.5.dp)
                .background(Color.Cyan)
        )
    }
}

@Composable
fun HomeTopBar(
    sessionManagerClass: SessionManagerClass,
    onClick: () -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(
                    width = 2.dp, color = loginBgColor, shape = RoundedCornerShape(0.dp)
                )
                .padding(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hey, ${sessionManagerClass.loginUserData?.name}",
                        fontWeight = FontWeight.W700,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.urbanist_semibold)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.9f),
                    )

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f)
                        .height(40.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color.Transparent)
                        .border(
                            width = 1.dp, color = loginBgColor, shape = RoundedCornerShape(8.dp)
                        )
                        .padding(3.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                onClick()
                            }), contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.power_off),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(loginBgColor),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}