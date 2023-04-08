package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.MesiboDataClass
import app.retvens.rown.DataCollections.MesiboResponseClass
import app.retvens.rown.DataCollections.MesiboUsersData
import app.retvens.rown.DataCollections.UsersList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RownUrl {

    @POST("usercreate")
    fun createMesiboUser(@Body create:MesiboDataClass):Call<MesiboResponseClass>

    @GET("users")
    fun getMesiboUsers(): Call<UsersList>

}

