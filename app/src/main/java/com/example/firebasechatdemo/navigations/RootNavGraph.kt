package com.example.firebasechatdemo.navigations

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebasechatdemo.screens.common.ChangeStatusBarColor
import com.example.firebasechatdemo.utils.AuthFirebase
import com.example.firebasechatdemo.utils.RootGraph
import com.example.firebasechatdemo.utils.SessionManagerClass
import com.example.firebasechatdemo.App
import com.example.firebasechatdemo.R
import com.example.firebasechatdemo.screens.home.HomeScreen
import com.example.firebasechatdemo.ui.theme.blueBgColorLight
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavGraph(sessionManagerClass: SessionManagerClass) {

    var showSplashScreen by remember { mutableStateOf(true) }

    val navController = rememberNavController()
    App.navHostController = navController
    App.context = LocalContext.current

    LaunchedEffect(showSplashScreen) {
        delay(2000)
        showSplashScreen = false
    }
    val userData = remember { mutableStateOf(AuthFirebase.auth.currentUser) }

    Crossfade(targetState = showSplashScreen, label = "") { isSplashScreenVisible ->
        if (isSplashScreenVisible) {
            SplashScreen {
                showSplashScreen = false
            }
        } else {
            NavHost(
                navController = navController,
                route = RootGraph.ROOT,
                startDestination = if (userData.value == null) RootGraph.AUTHENTICATION else RootGraph.DASHBOARD,
            ) {
                authenticationNavigationGraph(navController = navController,userData)
                composable(route = RootGraph.DASHBOARD) {
                    HomeScreen(sessionManagerClass = sessionManagerClass,userData = userData,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navigateToAuthOrMainScreen: () -> Unit) {
    ChangeStatusBarColor()

    var rotationState by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(true) {
        delay(2000)
        navigateToAuthOrMainScreen()
    }

    LaunchedEffect(rotationState) {
        while (true) {
            delay(16) // Adjust the delay to control the rotation speed
            rotationState += 1f
        }
    }

    // Splash screen UI with transitions
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = TweenSpec(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(blueBgColorLight),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_book),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .scale(scale)
                .rotate(rotationState) // Apply the rotation effect
        )
    }
}