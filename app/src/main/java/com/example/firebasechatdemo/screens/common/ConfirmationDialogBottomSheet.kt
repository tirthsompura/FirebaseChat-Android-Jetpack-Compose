package com.example.firebasechatdemo.screens.common

import android.view.Gravity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.example.firebasechatdemo.R
import com.example.firebasechatdemo.ui.theme.fontRegular
import com.example.firebasechatdemo.ui.theme.fontSemiBold
import com.example.firebasechatdemo.ui.theme.loginBgColor


@Composable
fun deleteConfirmationDialog(
    dialogHeight: Dp = 150.dp,
    dialogTitle: String = "",
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.CENTER)
        Card(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .height(dialogHeight),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NormalText(
                    label = dialogTitle,
                    style = fontSemiBold.copy(
                        color = loginBgColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    maxLine = 2,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DeleteButtonWithIconTile(
                            style = fontRegular.copy(
                                color = Color.Black, fontSize = 18.sp,
                                lineHeight = 24.sp,
                            ),
                            height = 50.dp,
                            borderRadious = 12.dp,
                            label = stringResource(R.string.cancel),
                            modifier = Modifier
                                .weight(0.1f)
                                .border(
                                    width = 1.dp,
                                    color = loginBgColor,
                                    shape = RoundedCornerShape(12.dp)
                                ), icon = null
                        ) {
                            onDismiss()
                        }
                        SpacerHorizontal(width = 12.dp)

                        GradientButtonWithIcon1(
                            style = fontRegular.copy(
                                color = Color.White, fontSize = 18.sp,
                                lineHeight = 24.sp,
                            ),
                            height = 50.dp,
                            icon = null,
                            label = "Yes",
                            iconTint = Color.White,
                            borderRadious = 12.dp,
                            modifier = Modifier.weight(0.1f),
                        ) {
                            onAccept()
                        }
                    }
                }
            }
        }
    }
}