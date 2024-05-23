package com.example.firebasechatdemo.navigations

import com.example.firebasechatdemo.utils.ConstantAppScreenName
import com.example.firebasechatdemo.utils.ValidationConstants.UserDataString

sealed class AuthenticationScreens(val route: String) {

    object LoginScreen : AuthenticationScreens(ConstantAppScreenName.LOGIN_SCREEN)

    object RegisterScreen : AuthenticationScreens(ConstantAppScreenName.REGISTER_SCREEN)

    object ChatScreen : AuthenticationScreens("${ConstantAppScreenName.CHAT_SCREEN}/{$UserDataString}"){
        fun getUserDataClassString(getUserDataClassString: String): String {
            return this.route.replace(
                oldValue = "{$UserDataString}",
                newValue = getUserDataClassString
            )
        }
    }
}