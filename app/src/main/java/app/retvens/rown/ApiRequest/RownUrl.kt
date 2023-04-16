package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.*
import app.retvens.rown.DataCollections.onboarding.ContactResponse
import app.retvens.rown.DataCollections.onboarding.ContactsData
import app.retvens.rown.DataCollections.onboarding.GetInterests
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
        @Part("Full_name") Name: RequestBody,
        @Part("Email") Email: RequestBody,
        @Part("Phone") Phone: Long,
        @Part Profile_pic: MultipartBody.Part,
        @Part("Mesibo_account[uid]") uid:Int,
        @Part("Mesibo_account[address]") address:RequestBody,
        @Part("Mesibo_account[token]") token:RequestBody,
        @Part("Interest[id]") id:RequestBody,
        @Part("Post_count") Post_count: Int,
        @Part("connection_count") connection_count: Int,
    ) : Call<UserProfileResponse>

    @GET("profile")
    fun getProfile() : Call<List<UserProfileRequestItem>>

    @POST("usercreate")
    fun createMesiboUser(@Body create:MesiboDataClass):Call<MesiboResponseClass>

    @GET("users")
    fun getMesiboUsers(): Call<UsersList>

    @POST("contacts")
    fun uploadContacts(
        @Body contacts : ContactsData
    ) : Call<ContactResponse>

    @GET("interests")
    fun getInterests() : Call<GetInterests>
}

