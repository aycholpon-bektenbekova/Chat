package com.example.chat.model

data class Messages(
    var senderId: String? = null,
    var receiverId: String? = null,
    var message: String? = null,
    var time: Long=System.currentTimeMillis()
):java.io.Serializable
