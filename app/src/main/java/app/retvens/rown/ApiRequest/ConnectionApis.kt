package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.ConnectionCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ConnectionApis {

    @PATCH("sendrequest/{userId}")
    fun sendRequest(
        @Path("userId")userId:String,
        @Body user_id: ConnectionDataClass
    ):Call<UpdateResponse>

    @PATCH("deleterequest/{user_id}")
    fun removeRequest(
        @Path("user_id")userId:String,
        @Body user_id: ConnectionDataClass
    ):Call<UpdateResponse>

    @PATCH("deleteconn/{user_id}")
    fun removeConnection(
        @Path("user_id")userId:String,
        @Body user_id: ConnectionDataClass
    ):Call<UpdateResponse>

    @GET("allrequests/{userId}")
    fun getRequestList(
        @Path("userId")userId:String
    ):Call<GetAllRequestDataClass>

    @GET("connectionlist/{userId}")
    fun getConnectionList(
        @Path("userId")userId:String
    ):Call<List<ConnectionListDataClass>>

    @GET("normalprofile/{userId}/{connUserId}")
    fun getconnProfile(
        @Path("userId")userId:String,
        @Path("connUserId")connUserId:String
    ):Call<NormalUserDataClass>

    @GET("hotelinfo/{userId}/{connUserId}")
    fun getconnOwnerProfile(
        @Path("userId")userId:String,
        @Path("connUserId")connUserId:String
    ):Call<OwnerProfileDataClass>


    @GET("vendorinfo/{userId}/{connUserId}")
    fun getVendorProfile(
        @Path("userId")userId:String,
        @Path("connUserId")connUserId:String
    ):Call<VendorProfileDataClass>

}