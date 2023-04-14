package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RownUrl {

    @Multipart
    @POST("profile")
    fun uploadUserProfile(
        @Part Profile_pic: MultipartBody.Part,
        @Part("User_name") Name: RequestBody,
        @Part("Email") Email: RequestBody,
        @Part("Phone") Phone: Long,

    ) : Call<UserProfileResponse>

    @POST("usercreate")
    fun createMesiboUser(@Body create:MesiboDataClass):Call<MesiboResponseClass>

    @POST("creategroup")
    fun createGroup(@Body create:GroupCreate):Call<ResponseGroup>

    @GET("users")
    fun getMesiboUsers(): Call<UsersList>

    @POST("addmember")
    fun addMember(@Body addMember:AddMemberData):Call<ResponseGroup>

    @POST("contacts")
    fun uploadContacts(
        @Body contacts : ContactsData
    ) : Call<ContactResponse>

//    @GET("profile")
//    fun getUsers() : Call<>
}

