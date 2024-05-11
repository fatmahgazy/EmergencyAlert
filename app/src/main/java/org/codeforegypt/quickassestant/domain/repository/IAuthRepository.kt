package org.codeforegypt.quickassestant.domain.repository

import org.codeforegypt.quickassestant.data.model.UserSignUpModel
import org.codeforegypt.quickassestant.data.model.UserLoginModel

interface IAuthRepository {

    suspend fun signUp(user: UserSignUpModel): Boolean

    suspend fun login(userLoginModel: UserLoginModel): Boolean

    suspend fun userExists(): Boolean

    suspend fun forgotPasswordCode(email: String): Boolean

    suspend fun forgotPasswordNewPassword(email: String, code: String, newPassword: String): String?

}