package org.codeforegypt.quickassestant.api

import kotlinx.coroutines.Deferred
import org.codeforegypt.quickassestant.data.model.ModifyContact
import org.codeforegypt.quickassestant.data.model.Contact
import org.codeforegypt.quickassestant.data.model.Contacts
import org.codeforegypt.quickassestant.data.model.DeleteContactResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ContactsApi {

    @FormUrlEncoded
    @POST("phone/Add")
    fun addContact(
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String
    ): Deferred<Response<Contact>>

    @GET("phone/get")
    fun getContacts(@Query("email") email: String): Deferred<Response<Contacts>>

    @FormUrlEncoded
    @POST("phone/Delete")
    fun deleteContact(@Field("name") name: String, @Field("phone") phone: String, @Field("email") email: String): Deferred<Response<DeleteContactResponse>>
}