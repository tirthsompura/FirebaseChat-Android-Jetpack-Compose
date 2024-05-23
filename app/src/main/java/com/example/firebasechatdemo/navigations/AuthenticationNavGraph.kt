package com.example.firebasechatdemo.navigations

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.firebasechatdemo.screens.authentication.signup.SignUpScreen
import com.example.firebasechatdemo.screens.authentication.login.LoginScreen
import com.example.firebasechatdemo.screens.chatscreen.ChatScreen
import com.example.firebasechatdemo.utils.RootGraph
import com.google.firebase.auth.FirebaseUser

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authenticationNavigationGraph(
    navController: NavHostController,
    userData: MutableState<FirebaseUser?>,
) {
    navigation(
        route = RootGraph.AUTHENTICATION, startDestination = AuthenticationScreens.LoginScreen.route
    ) {
        composable(
            route = AuthenticationScreens.LoginScreen.route,
        ) {
            LoginScreen(navController = navController)
        }

        composable(route = AuthenticationScreens.RegisterScreen.route) {
            SignUpScreen(
                navController = navController, userData = userData
            )
        }

        composable(
            route = AuthenticationScreens.ChatScreen.route,
        ) {
            ChatScreen(navController)
        }
    }
}