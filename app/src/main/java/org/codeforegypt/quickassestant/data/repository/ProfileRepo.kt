package org.codeforegypt.quickassestant.data.repository

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.codeforegypt.quickassestant.api.ProfileApi
import org.codeforegypt.quickassestant.data.db.UserDao
import org.codeforegypt.quickassestant.data.model.User
import org.codeforegypt.quickassestant.domain.repository.IProfileRepo
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ProfileRepo @Inject constructor(
    private val userDao: UserDao,
    private val profileApi: ProfileApi
): IProfileRepo{

    override suspend fun userProfile(): User {
        return userDao.getUser()
    }

    override suspend fun editProfile(
        newEmail: String,
        newAge: String,
        newUsername: String,
        newBloodType: String,
        newPhone: String
    ): Boolean {
        return try {
            val email = userProfile().email
            val response = profileApi.editProfile(
                email,
                newEmail,
                newAge,
                newBloodType,
                newUsername,
                newPhone
            ).await()

            if (response.isSuccessful && response.body() != null)
                userDao.insertUser(response.body()!!.user)

            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }

    override suspend fun editProfilePicture(filePath: String): Boolean {
        return try {
            val picPart = getPhotoPart(File(filePath)) ?: return false

            val email = userProfile().email
            val response = profileApi.editProfilePicture(getRequestBodyPart(email), picPart).await()

            if (response.isSuccessful && response.body() != null)
                userDao.insertUser(response.body()!!.user)
            response.isSuccessful
        } catch (e: Exception) {
            Log.d("EditProfile", "${e.message}")
            false
        }
    }

    fun getRequestBodyPart(email: String): RequestBody {
        return email.toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun getPhotoPart(file: File): MultipartBody.Part? {
        if (file.exists().not())
            return null
        val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}