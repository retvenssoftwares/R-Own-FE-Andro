package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.FeedCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.home.DataItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface FeedsApi {

    @GET("getpost")
    fun getPost():Call<List<DataItem.Banner>>

    @PATCH("like/{postId}")
    fun postLike(
        @Path("postId") postId : String,
        @Body likes:LikesCollection
    ):Call<UpdateResponse>

    @GET("comment/{postId}")
    fun getComment(
        @Path("postId")postId : String
    ):Call<List<GetComments>>

    @Multipart
    @POST("usergroup")
    fun createCommunities(
        @Part("group_name") group_name : RequestBody,
        @Part("group_id") group_id: RequestBody,
        @Part("location") location: RequestBody,
        @Part("community_type") community_type: RequestBody,
        @Part Profile_pic: MultipartBody.Part
    ): Call<UpdateResponse>

    @PATCH("adduser/{gid}")
    fun addUser(
        @Path("gid") gid:String,
        @Body addMember:AddUserDataClass
    ):Call<UpdateResponse>

    @GET("getgroup")
    fun getCommunities():Call<List<GetCommunitiesData>>
}