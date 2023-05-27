package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.*
import app.retvens.rown.DataCollections.onboarding.ContactResponse
import app.retvens.rown.DataCollections.onboarding.ContactsData
import app.retvens.rown.DataCollections.onboarding.GetInterests
import app.retvens.rown.DataCollections.onboarding.SearchUser
import app.retvens.rown.authentication.UpdateInterestClass
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RownUrl {

    @Multipart
    @PATCH("update/{user_id}")
    fun uploadUserProfile(
        @Path("user_id") user_id : String,
        @Part("Full_name") Name: RequestBody,
        @Part("Email") Email: RequestBody,
        @Part("Phone") Phone: Long,
        @Part Profile_pic: MultipartBody.Part,
        @Part("Mesibo_account[uid]") uid:Int,
        @Part("Mesibo_account[address]") address:RequestBody,
        @Part("Mesibo_account[token]") token:RequestBody,
        @Part("Interest[id]") id:RequestBody
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("update/{user_id}")
    fun uploadUserProfileWithoutImg(
        @Path("user_id") user_id : String,
        @Part("Full_name") Name: RequestBody,
        @Part("Email") Email: RequestBody,
        @Part("Phone") Phone: Long,
        @Part("Mesibo_account[uid]") uid:Int,
        @Part("Mesibo_account[address]") address:RequestBody,
        @Part("Mesibo_account[token]") token:RequestBody,
        @Part("Interest[id]") id:RequestBody
    ) : Call<UserProfileResponse>

    @POST("profile")
    fun searchUser(
        @Body searchUser: SearchUser
    ) : Call<UserProfileResponse>

    @GET("profile/{user_id}")
    fun fetchUser(
        @Path("user_id") user_id: String
    ) : Call<UserProfileRequestItem>

    @GET("profile")
    fun getProfile() : Call<List<UserProfileRequestItem>>

    @POST("usercreate")
    fun createMesiboUser(@Body create:MesiboDataClass):Call<MesiboResponseClass>

    @GET("users")
    fun getMesiboUsers(): Call<UsersList>

    @POST("creategroup")
    fun createGroup(@Body create:GroupCreate):Call<ResponseGroup>



    @POST("addmember")
    fun addMember(@Body addMember:AddMemberData):Call<ResponseGroup>


    @POST("contacts")
    fun uploadContacts(
        @Body contacts : ContactsData
    ) : Call<ContactResponse>

    @GET("get_interest")
    fun getInterests() : Call<List<GetInterests>>

    @PATCH("interest_push/{User_id}")
    fun updateInterest(
        @Path("User_id") User_id : String,
        @Body updateInterestClass: UpdateInterestClass
    ) : Call<ContactResponse>
}

