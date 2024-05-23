package com.example.firebasechatdemo.utils


object ValidationConstants {
    const val NameBlank = "Please enter your name."
    const val EmailBlank = "Please enter your email address."
    const val EmailValid = "Please enter valid email address."
    const val PasswordBlank = "Please enter your password."
    const val PasswordValid = "Password length must be at least 8 characters and must not contain spaces."
    const val UserDataString = "UserData"

}

object FirebaseKeyConstants {
    const val Name = "name"
    const val LastSeenAt = "lastSeenAt"
    const val Online = "online"
    const val Email = "email"
    const val USERS_KEY = "users"
    const val USER_ID = "userId"
    const val MESSAGES_KEY = "messages"
    const val ONLINE = "online"
    const val OFFLINE = "offline"
    const val UNREAD_COUNT = "unreadCount"
}