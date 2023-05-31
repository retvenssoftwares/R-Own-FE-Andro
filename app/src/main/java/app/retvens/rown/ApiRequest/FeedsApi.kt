package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.FeedCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.LocationClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.location.UpcomingEventDataclass
import app.retvens.rown.NavigationFragments.home.DataItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface FeedsApi {



    @PATCH("like/{postId}")
    fun postLike(
        @Path("postId") postId : String,
        @Body likes:LikesCollection
    ):Call<UpdateResponse>

    @GET("comment/{postId}")
    fun getComment(
        @Path("postId")postId : String
    ):Call<GetComments>

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

    @Multipart
    @POST("post/{user_id}")
    fun createPost(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("Can_See")Can_See:RequestBody,
        @Part("Can_comment")Can_comment:RequestBody,
        @Part("caption")caption:RequestBody,
        @Part media: MultipartBody.Part
    ):Call<UpdateResponse>

    @Multipart
    @POST("post/{user_id}")
    fun createStatus(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("Can_See")Can_See:RequestBody,
        @Part("Can_comment")Can_comment:RequestBody,
        @Part("caption")caption:RequestBody,
    ):Call<UpdateResponse>

    @Multipart
    @POST("post/{user_id}")
    fun createPostEvent(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("Can_See")Can_See:RequestBody,
        @Part("Can_comment")Can_comment:RequestBody,
        @Part("caption")caption:RequestBody,
        @Part("event_thumbnail")event_thumbnail:RequestBody,
        @Part("Event_name")Event_name:RequestBody,
        @Part("Event_location")Event_location:RequestBody
    ):Call<UpdateResponse>

    @Multipart
    @POST("post/{user_id}")
    fun createPoll(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("pollQuestion[0][Question]")question:RequestBody,
        @Part("pollQuestion[0][Options][0][Option]")Option1:RequestBody,
        @Part("pollQuestion[0][Options][1][Option]")Option2:RequestBody,
    ):Call<UpdateResponse>

    @GET("getconnpost/{user_Id}")
    fun getPost(
        @Path("user_Id")user_Id:String
    ):Call<List<PostsDataClass>>

    @PATCH("postcomment/{post_Id}")
    fun postComment(
        @Path("post_Id") post_Id:String,
        @Body postComment:PostCommentClass
    ):Call<UpdateResponse>

    @PATCH("commentReply/{post_Id}")
    fun replyComment(
        @Path("post_Id") post_Id:String,
        @Body postComment:PostCommentReplyClass
    ):Call<UpdateResponse>

    @GET("gethotelinpost")
    fun fetchbyLocation(
        location:String
    ):Call<List<GetHotelDataClass>>

    @GET("geteventposting/{location}")
    fun getEvent(
        @Path("location")location: String
    ):Call<List<UpcomingEventDataclass>>

    @GET("getpostmedia/{user_id}/{User_id}")
    fun getUserProfileMedia(
        @Path("user_id")user_id:String,
        @Path("User_id")User_id:String,
        @Query("page")page:String
    ):Call<List<PostsDataClass>>

    @GET("getPolls/{user_id}")
    fun getNormalUserPoll(
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<PostsDataClass>>

    @GET("getStatus/{user_id}")
    fun getNormalUserStatus(
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<PostsDataClass>>

    @PATCH("polls/{postId}/{optionId}")
    fun votePost(
        @Path("postId")postId:String,
        @Path("optionId")optionId:String,
        @Body user_id: LikesCollection
    ):Call<UpdateResponse>
}