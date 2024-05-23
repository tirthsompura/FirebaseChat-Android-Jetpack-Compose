package com.example.firebasechatdemo.response


data class UserResponse(
    var email: String = "",
    var name: String = "",
    var profile_pic: String = "",
    var userId: String = "",
    var online: String = "",
    var lastSeenAt: Long = 0,
    var unreadCount: Int = 0
)

data class  AddChatMessageModel(
    var imageUrl: String = "",
    var from_name: String = "",
    var from_uid: String = "",
    var to_uid: String = "",
    var to_name: String = "",
    var last_time: Long = 0,
    var last_msg: String = "",
)