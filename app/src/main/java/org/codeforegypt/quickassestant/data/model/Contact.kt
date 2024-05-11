package org.codeforegypt.quickassestant.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    val name: String,
    val phone: String,
    @PrimaryKey
    val id: Int
)
