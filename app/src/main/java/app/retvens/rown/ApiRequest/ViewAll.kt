package app.retvens.rown.ApiRequest


import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SaveBlog
import app.retvens.rown.viewAll.viewAllBlogs.LikeBlog
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ViewAll {

    @GET("getblogpost//{user_id}")
    fun getAllBlogs(
        @Path("user_id") user_id : String,
    ) : Call<List<AllBlogsData>>

    @GET("getblog/{User_id}")
    fun getBlogsByUserId(
        @Path("category_id") User_id : String
    ) : Call<List<AllBlogsData>>

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

    @GET("getblogcategory/{category_id}")
    fun getBlogsByCategory(
        @Path("category_id") category_id : String
    ) : Call<List<AllBlogsData>>

}