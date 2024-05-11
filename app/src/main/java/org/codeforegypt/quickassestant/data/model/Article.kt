package org.codeforegypt.quickassestant.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Entity
@Parcelize
data class Article(
    @SerializedName("fcontent")
    val content: String,
    @PrimaryKey
    val id: String,
    @SerializedName("imgurl")
    val imgUrl: String,
    val title: String,
    val type: String
): Parcelable