package com.example.firebasechatdemo.screens.authentication.signup

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.firebasechatdemo.R
import com.example.firebasechatdemo.navigations.AuthenticationScreens
import com.example.firebasechatdemo.response.UserResponse
import com.example.firebasechatdemo.screens.authentication.login.BikePosition
import com.example.firebasechatdemo.screens.common.ChangeStatusBarColor
import com.example.firebasechatdemo.screens.common.GradientButton
import com.example.firebasechatdemo.screens.common.OutlinedSimpleTextFiled
import com.example.firebasechatdemo.screens.common.SpacerVertical
import com.example.firebasechatdemo.screens.common.drawline.BezierCurve
import com.example.firebasechatdemo.screens.common.drawline.BezierCurveStyle
import com.example.firebasechatdemo.ui.theme.blueBgColor
import com.example.firebasechatdemo.ui.theme.blueBgColorLight
import com.example.firebasechatdemo.ui.theme.fontRegular
import com.example.firebasechatdemo.ui.theme.whiteColor
import com.example.firebasechatdemo.utils.AuthFirebase
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.Name
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.ONLINE
import com.example.firebasechatdemo.utils.FirebaseKeyConstants.USERS_KEY
import com.example.firebasechatdemo.utils.ValidationConstants
import com.example.firebasechatdemo.utils.isEmpty
import com.example.firebasechatdemo.utils.isValidEmail
import com.example.firebasechatdemo.utils.isValidPassword
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    userData: MutableState<FirebaseUser?>,
    navController: NavController,
    signViewModel: SignUpViewModel = hiltViewModel(),
) {
    ChangeStatusBarColor()
    val auth = AuthFirebase.auth
    val userLoginData = remember { mutableStateOf<UserResponse?>(null) }

    LaunchedEffect(userData.value?.uid) {
        val fireStore = FirebaseFirestore.getInstance()
        val userDocRef = fireStore.collection(USERS_KEY).document("${userData.value?.uid}")
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userName = document.getString(Name)
                    userLoginData.value = UserResponse(
                        name = userName ?: "",
                        email = userData.value?.email ?: "",
                        userId = userData.value?.uid ?: "",
                        profile_pic =  "",
                        online = ONLINE,
                    )
                } else {
                    // Handle the case where the document doesn't exist
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    val context = LocalContext.current
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(blueBgColorLight)
                    .padding(bottom = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.createAccount),
                    fontWeight = FontWeight.W700,
                    fontSize = 25.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.urbanist_semibold)),
                )
            }
        }

        BezierCurve(
            modifier = Modifier
                .padding(top = 200.dp)
                .rotate(240f)
                .width(220.dp)
                .padding(end = 30.dp, top = 50.dp)
                .height(60.dp),
            points = listOf(150F, 30F, 80F, 10F, 10F),
            minPoint = 0F,
            maxPoint = 100F,
            style = BezierCurveStyle.CurveStroke(
                brush = Brush.horizontalGradient(listOf(Color(0xFFFFFFFF), Color(0xFF404961))),
                stroke = Stroke(width = with(LocalDensity.current) { 10.dp.toPx() })
            ),
        )
        Box(
            modifier = Modifier
                .padding(top = 149.dp)
                .offset(y = (-2).dp)
                .size(20.dp)
                .clip(shape = CircleShape)
                .background(whiteColor)
        )

        val bikeState = remember { mutableStateOf(BikePosition.Start) }
        val offsetAnimation: Dp by animateDpAsState(
            if (bikeState.value == BikePosition.Start) -10.dp else 10.dp,
            spring(dampingRatio = Spring.DampingRatioHighBouncy)
        )

        LaunchedEffect(Unit) {
            bikeState.value = BikePosition.Finish
        }
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(blueBgColor),
            modifier = Modifier
                .padding(top = 260.dp, start = 0.dp, end = 20.dp)
                .fillMaxWidth()
                .absoluteOffset(x = offsetAnimation)
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                SpacerVertical(10.dp)

                OutlinedSimpleTextFiled(
                    name = signViewModel.nameMsg,
                    placeHolder = stringResource(R.string.your_name),
                    isPasswordField = false,
                    horizontal = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    errorMsg = signViewModel.nameErrMsg.value
                ) {

                }
                SpacerVertical(10.dp)
                OutlinedSimpleTextFiled(
                    name = signViewModel.emailAddress,
                    placeHolder = stringResource(R.string.email),
                    isPasswordField = false,
                    horizontal = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    errorMsg = signViewModel.emailErrMsg.value
                ) {

                }
                SpacerVertical(10.dp)
                OutlinedSimpleTextFiled(
                    name = signViewModel.password,
                    placeHolder = stringResource(R.string.password),
                    isPasswordField = true,
                    horizontal = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    errorMsg = signViewModel.passwordErrMsg.value
                ) {

                }

                SpacerVertical(50.dp)

                GradientButton(
                    style = fontRegular.copy(
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        color = Color.White
                    ),
                    label = stringResource(R.string.sign_up),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    validationForSignUp(signViewModel) {
                        signUpAuth(context, auth, signViewModel) { signedInUser ->
                            navController.navigate(AuthenticationScreens.LoginScreen.route)
                            Toast.makeText(context, "SignUp Success", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.already_have_an_account),
                        fontWeight = FontWeight.W400,
                        fontSize = 15.sp,
                        color = whiteColor.copy(alpha = 0.5f),
                        fontFamily = FontFamily(Font(R.font.urbanist_regular)),
                    )
                    Text(
                        text = stringResource(R.string.login),
                        fontWeight = FontWeight.W700,
                        fontSize = 15.sp,
                        color = whiteColor,
                        fontFamily = FontFamily(Font(R.font.urbanist_semibold)),
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                navController.navigate(AuthenticationScreens.LoginScreen.route)
                            }
                        )
                    )
                }
                SpacerVertical(10.dp)
            }
        }
    }
}

fun validationForSignUp(signViewModel: SignUpViewModel, onSuccess: () -> Unit) {
    if (isEmpty(signViewModel.nameMsg.value)) {
        signViewModel.nameErrMsg.value = ValidationConstants.NameBlank
    }else if (isEmpty(signViewModel.emailAddress.value)) {
        signViewModel.emailErrMsg.value = ValidationConstants.EmailBlank
    } else if (!isValidEmail(signViewModel.emailAddress.value)) {
        signViewModel.emailErrMsg.value = ValidationConstants.EmailValid
    } else if (isEmpty(signViewModel.password.value)) {
        signViewModel.emailErrMsg.value = ""
        signViewModel.passwordErrMsg.value = ValidationConstants.PasswordBlank
    } else if (!isValidPassword(signViewModel.password.value)) {
        signViewModel.emailErrMsg.value = ""
        signViewModel.passwordErrMsg.value = ValidationConstants.PasswordValid
    } else {
        signViewModel.passwordErrMsg.value = ""
        onSuccess()
    }
}
