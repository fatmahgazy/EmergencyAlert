package org.codeforegypt.quickassestant.data.model

import com.google.gson.annotations.SerializedName

data class UserSignUpModel(
    val age: String,
    @SerializedName("blood_type")
    val bloodType: String,
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    val gender: String,
    val phone: String,
    val password: String
)
