package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.DeleteCommunityDataClass
import app.retvens.rown.DataCollections.FeedCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.LocationClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.location.UpcomingEventDataclass
import app.retvens.rown.DataCollections.saveId.SaveBlog
import app.retvens.rown.DataCollections.saveId.SavePost
import app.retvens.rown.NavigationFragments.home.DataItem
import app.retvens.rown.NavigationFragments.profile.polls.VotesDataClass
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

    @GET("post/{postId}")
    fun getPosts(
        @Path("postId")postId : String
    ):Call<PostItem>

    @GET("getPost/{User_id}/{postId}")
    fun getPostData(
        @Path("User_id")User_id : String,
        @Path("postId")postId : String
    ):Call<List<PostItem>>

    @Multipart
    @POST("userGroup")
    fun createCommunities(
        @Part("group_name") group_name : RequestBody,
        @Part("group_id") group_id: RequestBody,
        @Part("location") location: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("community_type") community_type: RequestBody,
        @Part("creatorID") creatorID: RequestBody,
        @Part("description") description: RequestBody,
        @Part Profile_pic: MultipartBody.Part
    ): Call<UpdateResponse>

    @PATCH("addUser/{gid}")
    fun addUser(
        @Path("gid") gid:String,
        @Body addMember:AddUserDataClass
    ):Call<UpdateResponse>

    @GET("fetchGroup/{user_id}")
    fun getCommunities(
        @Path("user_id")user_id:String
    ):Call<List<GetCommunitiesData>>

    @GET("fetchCommunity/{user_id}")
    fun getOpenCommunities(
        @Path("user_id")user_id:String
    ):Call<List<DataItem.CommunityRecyclerData>>
//    @GET("getgroup")
//    fun getCommunities():Call<List<GetCommunitiesData>>

    @Multipart
    @PATCH("updateGroup/{groupId}")
    fun updateGroup(
        @Path("groupId")groupId:String,
        @Part("group_name")group_name:RequestBody,
        @Part("description")description:RequestBody,
        @Part Profile_pic: MultipartBody.Part
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("updateGroup/{groupId}")
    fun updateGroupDetails(
        @Path("groupId")groupId:String,
        @Part("group_name")group_name:RequestBody,
        @Part("description")description:RequestBody,
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("updateGroup/{groupId}")
    fun updateGroupStatus(
        @Path("groupId")groupId:String,
        @Part("community_type")community_type:RequestBody
    ):Call<UpdateResponse>


    @Multipart
    @POST("post/{user_id}")
    fun createPost(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("Can_See")Can_See:RequestBody,
        @Part("Can_comment")Can_comment:RequestBody,
        @Part("caption")caption:RequestBody,
        @Part("location") location:RequestBody,
        @Part media: MultipartBody.Part
    ):Call<UpdateResponse>

    @Multipart
    @POST("post/{user_id}")
    fun createMultiPost(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("Can_See")Can_See:RequestBody,
        @Part("Can_comment")Can_comment:RequestBody,
        @Part("caption")caption:RequestBody,
        @Part("location") location:RequestBody,
        @Part media: List<MultipartBody.Part>
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
        @Part("location") location:RequestBody
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
    fun createCheckIn(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("hotel_id")hotel_id:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("Can_See")Can_See:RequestBody,
        @Part("Can_comment")Can_comment:RequestBody,
        @Part("caption")caption:RequestBody,
        @Part("checkinLocation")Event_location:RequestBody
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

    @Multipart
    @POST("post/{user_id}")
    fun create3Poll(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("pollQuestion[0][Question]")question:RequestBody,
        @Part("pollQuestion[0][Options][0][Option]")Option1:RequestBody,
        @Part("pollQuestion[0][Options][1][Option]")Option2:RequestBody,
        @Part("pollQuestion[0][Options][2][Option]")Option3:RequestBody,
    ):Call<UpdateResponse>

    @Multipart
    @POST("post/{user_id}")
    fun create4Poll(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("pollQuestion[0][Question]")question:RequestBody,
        @Part("pollQuestion[0][Options][0][Option]")Option1:RequestBody,
        @Part("pollQuestion[0][Options][1][Option]")Option2:RequestBody,
        @Part("pollQuestion[0][Options][2][Option]")Option3:RequestBody,
        @Part("pollQuestion[0][Options][3][Option]")Option4:RequestBody,
    ):Call<UpdateResponse>

    @Multipart
    @POST("post/{user_id}")
    fun create5Poll(
        @Path("user_id") user_id:String,
        @Part("user_id")userId:RequestBody,
        @Part("post_type")post_type:RequestBody,
        @Part("pollQuestion[0][Question]")question:RequestBody,
        @Part("pollQuestion[0][Options][0][Option]")Option1:RequestBody,
        @Part("pollQuestion[0][Options][1][Option]")Option2:RequestBody,
        @Part("pollQuestion[0][Options][2][Option]")Option3:RequestBody,
        @Part("pollQuestion[0][Options][3][Option]")Option4:RequestBody,
        @Part("pollQuestion[0][Options][4][Option]")Option5:RequestBody,
    ):Call<UpdateResponse>

    @GET("getConnPost/{user_Id}")
    fun getPost(
        @Path("user_Id")user_Id:String
    ):Call<List<PostsDataClass>>

    @GET("getFeed/{user_Id}")
    fun getFeedPost(
        @Path("user_Id")user_Id:String
    ):Call<List<PostsDataClass>>

    @PATCH("saveId/{user_id}")
    fun savePost(
        @Path("user_id") user_id : String,
        @Body savePost: SavePost
    ) : Call<UserProfileResponse>

    @PATCH("postComment/{post_Id}")
    fun postComment(
        @Path("post_Id") post_Id:String,
        @Body postComment:PostCommentClass
    ):Call<UpdateResponse>

    @PATCH("commentReply/{post_Id}")
    fun replyComment(
        @Path("post_Id") post_Id:String,
        @Body postComment:PostCommentReplyClass
    ):Call<UpdateResponse>

    @GET("getHotelInPost/{location}")
    fun fetchbyLocation(
        @Path("location") location:String,
    ):Call<List<GetHotelDataClass>>

    @GET("getEventPosting/{location}")
    fun getEvent(
        @Path("location")location: String
    ):Call<List<UpcomingEventDataclass>>

    @GET("getPostMedia/{user_id}/{User_id}")
    fun getUserProfileMedia(
        @Path("user_id")user_id:String,
        @Path("User_id")User_id:String,
        @Query("page")page:String
    ):Call<List<PostsDataClass>>

    @GET("getPolls/{user_id}/{User_id}")
    fun getNormalUserPoll(
        @Path("user_id")user_id:String,
        @Path("User_id")Otheruser_id:String,
        @Query("page")page:String
    ):Call<List<PostsDataClass>>

    @GET("getPollsVotes/{post_Id}")
    fun getVotes(
        @Path("post_Id")post_Id:String,
    ):Call<List<VotesDataClass>>

    @GET("getStatus/{user_id}/{User_id}")
    fun getNormalUserStatus(
        @Path("user_id")user_id:String,
        @Path("User_id")User_id:String,
        @Query("page")page:String
    ):Call<List<PostsDataClass>>

    @PATCH("polls/{postId}/{optionId}")
    fun votePost(
        @Path("postId")postId:String,
        @Path("optionId")optionId:String,
        @Body user_id: VoteCollection
    ):Call<UpdateResponse>

    @PATCH("editPost/{postId}")
    fun editPost(
        @Path("postId")postId:String,
        @Body editPostClass: EditPostClass
    ):Call<UpdateResponse>

    @PATCH("editPost/{postId}")
    fun deletePost(
        @Path("postId")postId:String,
        @Body deletePost: DeletePost
    ):Call<UpdateResponse>

    @GET("getGroup/{groupId}")
    fun getGroup(
        @Path("groupId")groupId:String
    ):Call<GetCommunitiesData>


    @GET("fetchCommunity/{userID}")
    fun fetchOpenCommunity(
        @Path("userID")userID:String
    ):Call<List<GetCommunitiesData>>

    @PATCH("deleteData")
    fun deleteCommunity(
        @Body groupId:DeleteCommunityDataClass
    ):Call<UpdateResponse>

    @GET("fetchLike/{postId}")
    fun getLike(
        @Path("postId")PostId:String
    ):Call<LikeDataClass>
}