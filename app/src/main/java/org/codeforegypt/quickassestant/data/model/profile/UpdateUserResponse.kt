package org.codeforegypt.quickassestant.data.model.profile

import org.codeforegypt.quickassestant.data.model.User

data class UpdateUserResponse(
    val message: String,
    val user: User
)