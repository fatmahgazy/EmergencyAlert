package org.codeforegypt.quickassestant.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class User(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("blood_type")
    val bloodType: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("imgurl")
    val imgUrl: String
)