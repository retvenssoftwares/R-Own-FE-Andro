package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.ConnectionCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ConnectionApis {

    @PATCH("acceptRequest/{userId}")
    fun acceptRequest(
        @Path("userId")userId:String,
        @Body user_id: ConnectionDataClass
    ):Call<UpdateResponse>

    @PATCH("sendRequest/{userId}")
    fun sendRequest(
        @Path("userId")userId:String,
        @Body user_id: ConnectionDataClass
    ):Call<UpdateResponse>


    @PATCH("deleteRequest/{user_id}")
    fun removeRequest(
        @Path("user_id")userId:String,
        @Body user_id: ConnectionDataClass
    ):Call<UpdateResponse>

    @PATCH("deleteConn/{user_id}")
    fun removeConnection(
        @Path("user_id")userId:String,
        @Body user_id: ConnectionDataClass
    ):Call<UpdateResponse>

    @GET("allRequests/{userId}")
    fun getRequestList(
        @Path("userId")userId:String
    ):Call<GetAllRequestDataClass>

    @GET("connectionList/{userId}")
    fun getConnectionList(
        @Path("userId")userId:String
    ):Call<List<ConnectionListDataClass>>

    @GET("normalProfile/{userId}/{connUserId}")
    fun getconnProfile(
        @Path("userId")userId:String,
        @Path("connUserId")connUserId:String
    ):Call<NormalUserDataClass>

    @GET("hotelInfo/{userId}/{connUserId}")
    fun getconnOwnerProfile(
        @Path("userId")userId:String,
        @Path("connUserId")connUserId:String
    ):Call<OwnerProfileDataClass>


    @GET("vendorInfo/{userId}/{connUserId}")
    fun getVendorProfile(
        @Path("userId")userId:String,
        @Path("connUserId")connUserId:String
    ):Call<VendorProfileDataClass>

    @POST("call/{receiveruserId}")
    fun calling(
        @Path("receiveruserId")receiveruserId:String,
        @Body senderUserId:String
    ):Call<UpdateResponse>


}