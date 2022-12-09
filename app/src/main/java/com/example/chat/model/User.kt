package com.example.chat.model

import java.io.Serializable

data class User(
    var userName: String? = null,
    var phone: String? = null,
    var uid: String? = null,
) : Serializable
