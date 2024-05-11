package org.codeforegypt.quickassestant.data.model

data class Emergency(
    val date: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val message: String,
    val phoneNumber: String,
    val username: String
)