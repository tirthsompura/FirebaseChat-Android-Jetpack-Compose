package com.example.firebasechatdemo.screens.chatscreen.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.firebasechatdemo.R
import com.example.firebasechatdemo.response.AddChatMessageModel
import com.example.firebasechatdemo.screens.chatscreen.ChatViewModel
import com.example.firebasechatdemo.screens.common.NormalText
import com.example.firebasechatdemo.screens.common.SpacerHorizontal
import com.example.firebasechatdemo.screens.common.SpacerVertical
import com.example.firebasechatdemo.ui.theme.blueBgColor
import com.example.firebasechatdemo.ui.theme.darkBlueSenderBgColor
import com.example.firebasechatdemo.ui.theme.fontRegular
import com.example.firebasechatdemo.ui.theme.lightBlueReceiverBgColor
import com.example.firebasechatdemo.ui.theme.whiteColor
import com.example.firebasechatdemo.utils.getTimeFromTimestamp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun BottomBarItem(
    chatViewModel: ChatViewModel,
    onClick: () -> Unit,
    onSelectOptions: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .padding(bottom = 15.dp)
            .background(color = blueBgColor)
    ) {
        SpacerVertical(10.dp)
        if (chatViewModel.selectedImageUri.value != null) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .border(width = 2.dp, shape = RoundedCornerShape(12.dp), color = whiteColor)
                    .clickable(
                        onClick = {
                            chatViewModel.selectedImageUri.value = null
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberImagePainter(
                    data = chatViewModel.selectedImageUri.value,
                    builder = {}
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        }
        if (chatViewModel.selectedDocUri.value != null) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .border(width = 2.dp, shape = RoundedCornerShape(12.dp), color = whiteColor)
                    .clickable(
                        onClick = {
                            chatViewModel.selectedDocUri.value = null
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberImagePainter(
                    data = chatViewModel.selectedDocUri.value,
                    builder = {}
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        }
        SpacerVertical(10.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            ) {
                OutlinedTextField(
                    value = chatViewModel.chatMessageValue.value,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    placeholder = {
                        NormalText(
                            label = stringResource(R.string.message),
                            style = fontRegular.copy(color = whiteColor, fontSize = 16.sp)
                        )
                    },
                    onValueChange = {
                        chatViewModel.chatMessageValue.value = it
                    },
                    textStyle = fontRegular.copy(color = whiteColor),
                    trailingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_clip),
                            contentDescription = "",
                            modifier = Modifier.clickable(
                                onClick = {
                                    onSelectOptions()
                                }),
                            colorFilter = ColorFilter.tint(whiteColor)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = whiteColor.copy(alpha = 0.4f),
                        focusedBorderColor = whiteColor,
                        disabledBorderColor = whiteColor,
                        cursorColor = whiteColor
                        )
                )
            }

            SpacerHorizontal(10.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f)
                    .height(58.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(Color.Transparent)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = whiteColor.copy(alpha = 0.5f)
                    )
                    .clickable(onClick = {
                        if ((!chatViewModel.chatMessageValue.value.text.isNullOrEmpty()) || (!chatViewModel.imageUriValue.value.isNullOrEmpty())) {
                            onClick()
                        }
                    }), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(whiteColor),
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeftChatItem(chatMsg: AddChatMessageModel) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Uri.parse(chatMsg.imageUrl))
            .size(coil.size.Size.ORIGINAL)
            .build()
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(start = 15.dp, end = 35.dp, top = 8.dp, bottom = 8.dp)
                .clip(
                    shape = RoundedCornerShape(
                        25.dp
                    )
                )
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(
                        25.dp
                    ),
                    color = whiteColor.copy(alpha = 0.3f)
                )
                .background(color = if (chatMsg.imageUrl.isNotEmpty()) Color.Transparent else lightBlueReceiverBgColor)
                .padding(
                    start = if (chatMsg.imageUrl.isNotEmpty()) 0.dp else 15.dp,
                    end = if (chatMsg.imageUrl.isNotEmpty()) 0.dp else 15.dp,
                    top = if (chatMsg.imageUrl.isNotEmpty()) 0.dp else 15.dp,
                    bottom = 15.dp
                ),
            horizontalAlignment = Alignment.End
        ) {
            if (!chatMsg.imageUrl.isNullOrEmpty()) {
                if (painter.state is AsyncImagePainter.State.Loading) {
                    SpinningProgressBar()
                } else {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(height = 120.dp, width = 150.dp)
                            .clip(shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp))
                    )
                }
            } else {
                Text(text = chatMsg.last_msg, textAlign = TextAlign.Start, color = whiteColor)
            }
            SpacerVertical(10.dp)
            NormalText(
                label = getTimeFromTimestamp(chatMsg.last_time),
                style = fontRegular.copy(fontSize = 12.sp, color = whiteColor),
                textAlign = TextAlign.End,
                modifier = Modifier.offset(x = if (chatMsg.imageUrl.isEmpty()) 0.dp else (-10).dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RightChatItem(chatMsg: AddChatMessageModel) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Uri.parse(chatMsg.imageUrl))
            .size(coil.size.Size.ORIGINAL)
            .build()
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(start = 35.dp, end = 15.dp, top = 8.dp, bottom = 8.dp)
                .clip(
                    shape = RoundedCornerShape(
                        25.dp
                    )
                )
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(
                        25.dp
                    ),
                    color = whiteColor.copy(alpha = 0.3f)
                )
                .background(color = if (chatMsg.imageUrl.isNotEmpty()) Color.Transparent else darkBlueSenderBgColor)
                .padding(
                    start = if (chatMsg.imageUrl.isNotEmpty()) 0.dp else 15.dp,
                    end = if (chatMsg.imageUrl.isNotEmpty()) 0.dp else 15.dp,
                    top = if (chatMsg.imageUrl.isNotEmpty()) 0.dp else 15.dp,
                    bottom = 15.dp
                ),
            horizontalAlignment = Alignment.End
        ) {
            if (chatMsg.imageUrl.isNotEmpty()) {
                if (painter.state is AsyncImagePainter.State.Loading) {
                    SpinningProgressBar()
                } else {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(height = 120.dp, width = 150.dp)
                            .clip(shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp))
                    )
                }
            } else {
                Text(text = chatMsg.last_msg, textAlign = TextAlign.Start, color = whiteColor)
            }
            SpacerVertical(5.dp)
            NormalText(
                label = getTimeFromTimestamp(chatMsg.last_time),
                style = fontRegular.copy(fontSize = 12.sp, color = whiteColor),
                textAlign = TextAlign.End,
                modifier = Modifier.offset(
                    x = if (chatMsg.imageUrl.isEmpty()) 0.dp else (-10).dp,
                    y = if (chatMsg.imageUrl.isEmpty()) 0.dp else 4.dp
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraGalleryBottomSheet(
    context: Context,
    showSheet: MutableState<Boolean>,
    sendImageUri: (Uri) -> Unit,
) {
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), "com.example.firebasechatdemo" + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }


    val modalBottomSheetState = rememberModalBottomSheetState()

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    // ActivityResultLauncher for gallery
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                capturedImageUri = uri
            }
        }
    if (capturedImageUri.path?.isNotEmpty() == true) {
        sendImageUri(capturedImageUri)
        capturedImageUri = Uri.EMPTY
    }

    ModalBottomSheet(
        onDismissRequest = {
            showSheet.value = false
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 80.dp, start = 20.dp, end = 20.dp)
                .clip(shape = RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                BottomSheetIconButton(
                    painter = painterResource(id = R.drawable.camera),
                ) {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        // Request a permission
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }

                BottomSheetIconButton(
                    painter = painterResource(id = R.drawable.gallery),
                ) {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun BottomSheetIconButton(
    painter: Painter,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(blueBgColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            contentScale = ContentScale.Fit
        )
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

@Composable
fun SpinningProgressBar(
    modifier: Modifier = Modifier,
    imageSize: Dp = 30.dp,
    boxSize: Dp = 120.dp,
) {
    val count = 12
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = count.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(count * 80, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(boxSize)
    ) {
        Canvas(modifier = modifier.size(imageSize)) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val width = size.width * .3f
            val height = size.height / 8

            val cornerRadius = width.coerceAtMost(height) / 2

            for (i in 0..360 step 360 / count) {
                rotate(i.toFloat()) {
                    drawRoundRect(
                        color = Color.LightGray.copy(alpha = .7f),
                        topLeft = Offset(canvasWidth - width, (canvasHeight - height) / 2),
                        size = Size(width, height),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                }
            }

            val coefficient = 360f / count

            for (i in 1..4) {
                rotate((angle.toInt() + i) * coefficient) {
                    drawRoundRect(
                        color = Color.Gray.copy(alpha = (0.2f + 0.2f * i).coerceIn(0f, 1f)),
                        topLeft = Offset(canvasWidth - width, (canvasHeight - height) / 2),
                        size = Size(width, height),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                }
            }
        }
    }
}