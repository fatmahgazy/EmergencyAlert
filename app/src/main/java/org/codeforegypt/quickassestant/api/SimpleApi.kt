package org.codeforegypt.quickassestant.api

import kotlinx.coroutines.Deferred
import org.codeforegypt.quickassestant.data.model.AuthModel
import org.codeforegypt.quickassestant.data.model.ForgotPasswordMessage
import org.codeforegypt.quickassestant.data.model.UserSignUpModel
import org.codeforegypt.quickassestant.data.model.UserLoginModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SimpleApi {

    @FormUrlEncoded
    @POST("auth/signup")
    fun signUp(
        @Field("email") email: String,
        @Field("age") age: String,
        @Field("blood_type") bloodType: String,
        @Field("full_name") fullName: String,
        @Field("gender") gender: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
    ): Deferred<Response<AuthModel>>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(@Field("email") email: String, @Field("password") password: String): Deferred<Response<AuthModel>>

    @FormUrlEncoded
    @POST("user/code")
    fun forgotPasswordCode(@Field("email") email: String): Deferred<Response<ForgotPasswordMessage>>

    @FormUrlEncoded
    @POST("user/code")
    fun forgotPasswordNewPassword(
        @Field("email") email: String,
        @Field("code") code: String,
        @Field("new") newPassword: String
    ): Deferred<Response<ForgotPasswordMessage>>

}