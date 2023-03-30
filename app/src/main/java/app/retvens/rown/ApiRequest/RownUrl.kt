package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.MesiboDataClass
import app.retvens.rown.DataCollections.MesiboResponseClass
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RownUrl {

    @POST("createUser")
    fun createMesiboUser(@Body create:MesiboDataClass):Call<MesiboResponseClass>

}

