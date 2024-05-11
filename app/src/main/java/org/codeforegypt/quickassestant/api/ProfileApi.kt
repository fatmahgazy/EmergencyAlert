package org.codeforegypt.quickassestant.api

import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.codeforegypt.quickassestant.data.model.profile.UpdateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ProfileApi {

    @FormUrlEncoded
    @POST("user/Edit")
    fun editProfile(
        @Field("email") email: String,
        @Field("new_email") newEmail: String,
        @Field("new_age") age: String,
        @Field("new_blood") bloodType: String,
        @Field("new_name") fullName: String,
        @Field("new_phone") phone: String
    ): Deferred<Response<UpdateUserResponse>>

    @Multipart
    @POST("updateimg")
    fun editProfilePicture(
        @Part("email") requestBody: RequestBody,
        @Part part: MultipartBody.Part
    ): Deferred<Response<UpdateUserResponse>>

}