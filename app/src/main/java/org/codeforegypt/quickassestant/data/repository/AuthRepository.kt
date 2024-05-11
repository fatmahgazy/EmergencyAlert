package org.codeforegypt.quickassestant.data.repository

import android.util.Log
import org.codeforegypt.quickassestant.api.SimpleApi
import org.codeforegypt.quickassestant.data.db.UserDao
import org.codeforegypt.quickassestant.data.model.UserSignUpModel
import org.codeforegypt.quickassestant.domain.repository.IAuthRepository
import org.codeforegypt.quickassestant.data.model.UserLoginModel
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val simpleApi: SimpleApi,
    private val userDao: UserDao
): IAuthRepository {
    override suspend fun signUp(user: UserSignUpModel): Boolean {
        val response = simpleApi.signUp(
            user.email,
            user.age,
            user.bloodType,
            user.fullName,
            user.gender,
            user.phone,
            user.password
        ).await()

        return if (response.isSuccessful && response.body() != null) {
            val registeredUser = response.body()!!.user
            userDao.insertUser(registeredUser)
            true
        } else
            false
    }

    override suspend fun login(user: UserLoginModel): Boolean {
        val responseOfLogin = simpleApi.login(user.email, user.password).await()

       return if (responseOfLogin.isSuccessful && responseOfLogin.body() != null) {
            val userOfLogin = responseOfLogin.body()!!.user
            userDao.insertUser(userOfLogin)
            true
        } else
            false
    }

    override suspend fun userExists(): Boolean {
        return userDao.getUsers().isNotEmpty()
    }

    override suspend fun forgotPasswordCode(email: String): Boolean {
        return try {
            val response = simpleApi.forgotPasswordCode(email).await()
            response.isSuccessful && response.body() != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun forgotPasswordNewPassword(email: String, code: String, newPassword: String): String? {
        return try {
            val response = simpleApi.forgotPasswordNewPassword(email, code, newPassword).await()
            if (response.isSuccessful && response.body() != null)
                null
            else
                response.message()
        } catch (e: Exception) {
            e.message ?: "Something went wrong!"
        }
    }
}