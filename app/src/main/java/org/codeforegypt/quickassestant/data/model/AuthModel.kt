package org.codeforegypt.quickassestant.data.model

import com.google.gson.annotations.SerializedName

class AuthModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User
)