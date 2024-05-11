package org.codeforegypt.quickassestant.data.model

data class DeleteContactResponse(
    val message: String,
    val phone: List<Phone>
)