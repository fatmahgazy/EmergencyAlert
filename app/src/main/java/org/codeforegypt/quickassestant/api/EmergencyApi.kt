package org.codeforegypt.quickassestant.api

import kotlinx.coroutines.Deferred
import org.codeforegypt.quickassestant.data.model.Emergency
import org.codeforegypt.quickassestant.data.model.EmergencyReport
import org.codeforegypt.quickassestant.data.model.Phone
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EmergencyApi {

    @GET("report/repo")
    fun getEmergencies(
        @Query("phone") phone: String
    ): Deferred<Response<EmergencyReport>>

    @GET("report/not")
    fun sendNotification(
        @Query("email") email: String
    ): Deferred<Response<Void>>


    @GET("report/getmyrepo")
    fun getMyEmergency(
        @Query("email") email: String
    ): Deferred<Response<EmergencyReport>>

    @FormUrlEncoded
    @POST("report/Add")
    fun addEmergency(
        @Field("email") email: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
        @Field("message") message: String
    ): Deferred<Response<Void>>

}