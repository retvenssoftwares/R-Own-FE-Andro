package app.retvens.rown.ApiRequest


import app.retvens.rown.DataCollections.FeedCollection.PostCommentClass
import app.retvens.rown.DataCollections.FeedCollection.PostCommentReplyClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SaveBlog
import app.retvens.rown.NavigationFragments.profile.vendorsReview.AllReviewsData
import app.retvens.rown.NavigationFragments.profile.vendorsReview.VendorReviewsData
import app.retvens.rown.viewAll.vendorsDetails.ReviewData
import app.retvens.rown.viewAll.viewAllBlogs.LikeBlog
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData
import app.retvens.rown.viewAll.viewAllBlogs.BlogAllComments
import app.retvens.rown.viewAll.viewAllBlogs.GetBlogById
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ViewAll {

    @GET("getblogpost/{user_id}")
    fun getAllBlogs(
        @Path("user_id") user_id : String,
    ) : Call<List<AllBlogsData>>

    @GET("getcomment/{blog_id}")
    fun getBlogComment(
        @Path("blog_id") blog_id : String
    ):Call<BlogAllComments>
    @PATCH("comment/{blog_id}")
    fun blogComment(
        @Path("blog_id") blog_id:String,
        @Body postComment: PostCommentClass
    ):Call<UpdateResponse>
    @PATCH("replycomment/{blog_id}")
    fun replyBlogComment(
        @Path("blog_id") blog_id:String,
        @Body postComment: PostCommentReplyClass
    ):Call<UpdateResponse>

    @GET("getblog/{blog_id}/{User_id}")
    fun getBlogsByBlogId(
        @Path("blog_id") blog_id:String,
        @Path("User_id") User_id : String
    ) : Call<GetBlogById>

//        @PATCH("deleterequest/saveid/{user_id}")
    @PATCH("saveid/{user_id}")
    fun saveBlog(
        @Path("user_id") user_id : String,
        @Body saveBlog: SaveBlog
    ) : Call<UserProfileResponse>

    @PATCH("likeblog/{blog_id}")
    fun likeBlog(
        @Path("blog_id") blog_id : String,
        @Body likeBlog: LikeBlog
    ) : Call<UserProfileResponse>

    @GET("getcategory")
    fun getBlogsCategory() : Call<List<ViewAllCategoriesData>>

    @GET("getblogcategory/{user_id}/{category_id}")
    fun getBlogsByCategory(
        @Path("user_id") user_id : String,
        @Path("category_id") category_id : String
    ) : Call<List<AllBlogsData>>

    @PATCH("addreviews/{user_id}")
    fun addVendorReview(
        @Path("user_id") user_id : String,
        @Body reviewData : ReviewData
    ) : Call<UpdateResponse>

    @GET("topreviews/{user_id}")
    fun topReviews(
        @Path("user_id") user_id : String
    ) : Call<List<VendorReviewsData>>

    @GET("getreviews/{user_id}")
    fun allReviews(
        @Path("user_id") user_id : String
    ) : Call<List<AllReviewsData>>
}