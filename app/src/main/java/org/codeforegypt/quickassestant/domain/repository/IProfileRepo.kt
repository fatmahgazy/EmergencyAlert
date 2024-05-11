package org.codeforegypt.quickassestant.domain.repository

import org.codeforegypt.quickassestant.data.model.User

interface IProfileRepo {

    suspend fun userProfile(): User

    suspend fun editProfile(
        newEmail: String,
        newAge: String,
        newUsername: String,
        newBloodType: String,
        newPhone: String
    ): Boolean

    suspend fun editProfilePicture(
        filePath: String
    ): Boolean
}