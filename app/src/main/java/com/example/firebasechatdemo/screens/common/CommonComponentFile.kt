package com.example.firebasechatdemo.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firebasechatdemo.R
import com.example.firebasechatdemo.ui.theme.blueBgColorLight
import com.example.firebasechatdemo.ui.theme.fontRegular
import com.example.firebasechatdemo.ui.theme.whiteColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun NormalText(
    label: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    maxLine: Int = 1,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        text = label,
        modifier = modifier,
        style = style,
        maxLines = maxLine,
        overflow = overflow,
        textAlign = textAlign,
    )
}

@Composable
fun SpacerVertical(height: Dp = 10.dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun SpacerHorizontal(width: Dp = 18.dp) {
    Spacer(modifier = Modifier.width(width))
}


@ExperimentalComposeUiApi
@Composable
fun OutlinedSimpleTextFiled(
    name: MutableState<String>,
    placeHolder: String,
    horizontal: Dp = 0.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email
    ),
    isPasswordField: Boolean = false,
    trailingIcon: Int = 0,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    errorMsg: String = "",
    isDisabled: Boolean = false,
    isNumbersOnly: Boolean = false,
    showSmallLabel: Boolean = false,
    isMobileNumberField: Boolean = false,
    isZipCodeField: Boolean = false,
    isUserName: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    doneButtonCallback: () -> Unit = {},
    callBack: () -> Unit,

    ) {
    val pattern = remember { Regex("^\\d+\$") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusChanged = remember {
        mutableStateOf(false)
    }
    val labelTextSize = remember {
        mutableStateOf(if (showSmallLabel) 12.sp else 16.sp)
    }
    val labelLineHeight = remember {
        mutableStateOf(if (showSmallLabel) 18.sp else 20.sp)
    }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = horizontal)) {
        OutlinedTextField(
            modifier = modifier
                .onFocusChanged {
                    //Change label size
                    focusChanged.value = it.isFocused
                    if (!isDisabled && !readOnly) {
                        if (it.isFocused || it.hasFocus || name.value.isNotEmpty()) {
                            labelTextSize.value = 12.sp
                            labelLineHeight.value = 20.sp
                        } else if (!it.isFocused) {
                            labelTextSize.value = 18.sp
                            labelLineHeight.value = 20.sp
                        }
                    }
                }
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        callBack()
                    }
                )
                .fillMaxWidth()
                .height(60.dp),
            value = name.value,

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = whiteColor,
                unfocusedBorderColor = whiteColor.copy(alpha = 0.5f),
                cursorColor = whiteColor,
                disabledBorderColor = whiteColor,
            ),
            isError = errorMsg.isNotEmpty(),
            readOnly = readOnly,
            label = {
                Text(
                    text = placeHolder,
                    style = fontRegular.copy(
                        color = if (isDisabled) whiteColor else whiteColor.copy(alpha = 0.8f),
                        fontSize = labelTextSize.value,
                        lineHeight = labelLineHeight.value
                    )
                )
            },
            enabled = !readOnly,
            onValueChange = {
                if (isNumbersOnly) {
                    if (it.isEmpty() || it.matches(pattern)) {
                        if (isMobileNumberField) {
                            if (it.length <= 14) {
                                name.value = it
                            }
                        } else if (isZipCodeField) {
                            if (it.length <= 10) {
                                name.value = it
                            }
                        } else {
                            name.value = it
                        }

                    }
                } else if (isPasswordField) {
                    if (!it.contains(" ")) {
                        name.value = it
                    }
                } else if (isUserName) {
                    name.value = it.trim()
                } else {
                    name.value = it
                }
                //Change label size
                if (name.value.isNotEmpty()) {
                    labelTextSize.value = 18.sp
                    labelLineHeight.value = 18.sp
                }
            },
            textStyle = fontRegular.copy(
                color = if (isDisabled) whiteColor else whiteColor,
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Start, fontSize = 16.sp, lineHeight = 20.sp
            ),
            shape = RoundedCornerShape(size = 12.dp),
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = if (isPasswordField) if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPasswordField) {
                {
                    val painter = if (passwordVisible)
                        painterResource(id = R.drawable.ic_eye)
                    else painterResource(id = R.drawable.ic_eye_off)

                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ),
                    )
                }
            } else if (trailingIcon != 0) {
                {
                    if (isDisabled) {
                        Image(
                            painter = painterResource(id = trailingIcon),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 15.dp),
                            colorFilter = ColorFilter.tint(color = whiteColor.copy(alpha = 0.5f))
                        )
                    } else
                        Image(
                            painter = painterResource(id = trailingIcon),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 15.dp)
                        )
                }
            } else null,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    doneButtonCallback()
                    keyboardController?.hide()
                }
            )
        )

        //Manage visibility for error messages
        if (errorMsg.isNotEmpty()) Text(
            text = errorMsg,
            modifier = Modifier.padding(top = 6.dp),
            style = fontRegular.copy(
                color = Color.Red,
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Start, fontSize = 12.sp, lineHeight = 16.sp
            ),
        )
    }
}


@Composable
fun GradientButton(
    backgroundColor: Color = blueBgColorLight,
    style: TextStyle,
    label: String,
    buttonHeight: Dp = 50.dp,
    borderRadious: Dp = 12.dp,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .clip(shape = RoundedCornerShape(borderRadious))

            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onClick()

                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = style,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GradientButtonWithIcon1(
    icon: Painter?,
    backgroundColor: Color = blueBgColorLight,
    style: TextStyle,
    label: String,
    height: Dp = 40.dp,
    modifier: Modifier,
    iconTint: Color,
    borderRadious: Dp = 6.dp,
    onClick: () -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape = RoundedCornerShape(size = borderRadious))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onClick()
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Image(
                painter = icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconTint),
                modifier = Modifier.size(16.dp)
            )
            SpacerHorizontal(width = 8.dp)
        }
        Text(
            text = label,
            style = style,
        )
    }
}


@Composable
fun DeleteButtonWithIconTile(
    modifier: Modifier = Modifier,
    height: Dp = 40.dp,
    style: TextStyle = fontRegular.copy(
        color = Color.Black
    ),
    borderRadious: Dp = 6.dp,
    icon: Painter?,
    label: String, deleteItem: () -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape = RoundedCornerShape(borderRadious))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { deleteItem() }
            )
            .background(whiteColor),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(16.dp)
                )
                SpacerHorizontal(width = 6.dp)
            }
            Text(
                text = label,
                style = style,
            )
        }
    }
}

@Composable
fun ChangeStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = if (darkTheme) blueBgColorLight else blueBgColorLight, darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = if (darkTheme) blueBgColorLight else blueBgColorLight, darkIcons = true
        )
    }
}