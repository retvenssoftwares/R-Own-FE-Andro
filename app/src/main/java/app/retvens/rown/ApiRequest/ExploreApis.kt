package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogData
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventData
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.ExploreHotelData
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.SaveHotel
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobData
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServiceData
import app.retvens.rown.NavigationFragments.exploreForUsers.people.ExplorePeopleDataClass
import app.retvens.rown.NavigationFragments.profile.vendorsReview.AllReviewsData
import app.retvens.rown.NavigationFragments.profile.vendorsReview.GetQuickReviewsData
import app.retvens.rown.NavigationFragments.profile.vendorsReview.VendorReviewsData
import app.retvens.rown.viewAll.vendorsDetails.ReviewData

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ExploreApis {

    @GET("getExplorePosts/{user_id}")
    fun getExplorePost(
        @Path("user_id")user_id:String,
        @Query("page") page: String
    ):Call<List<PostsDataClass>>

    @GET("getalljob")
    fun getExploreJob(
        @Query("page")page:String
    ):Call<List<ExploreJobData>>

    @GET("getallpostbycaption/{text}/{user_id}")
    fun searchPost(
        @Path("text")text:String,
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<PostsDataClass>>

    @GET("searchpeople/{text}/{user_id}")
    fun searchPeople(
        @Path("text")text:String,
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<ExplorePeopleDataClass>>

    @GET("getSearchServices/{text}")
    fun searchServices(
        @Path("text")text:String,
        @Query("page")page:String
    ):Call<List<ExploreServiceData>>

    @GET("searchblog/{text}/{user_id}")
    fun searchBlog(
        @Path("text")text:String,
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<ExploreBlogData>>

    @GET("searchotel/{text}/{user_id}")
    fun searchHotel(
        @Path("text")text:String,
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<ExploreHotelData>>

    @GET("getSearchJob/{text}")
    fun searchJob(
        @Path("text")text:String,
        @Query("page")page:String
    ):Call<List<ExploreJobData>>

    @GET("getpeople/{user_id}")
    fun getPeople(
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<ExplorePeopleDataClass>>

    @GET("getBlog/{user_id}")
    fun getExploreBlog(
        @Path("user_id") user_id : String,
        @Query("page")page:String
    ):Call<List<ExploreBlogData>>

    @GET("getexplorehotel/{user_id}")
    fun getExploreHotels(
        @Path("user_id") user_id : String,
        @Query("page")page:String
    ):Call<List<ExploreHotelData>>

    @GET("Event/{user_id}")
    fun getExploreEvent(
        @Path("user_id") user_id : String,
        @Query("page")page:String
    ):Call<List<ExploreEventData>>

    @GET("getService")
    fun getExploreService(
        @Query("page")page:String
    ):Call<List<ExploreServiceData>>

    @PATCH("saveid/{User_id}")
    fun saveHotel(
        @Path("User_id") User_id : String,
        @Body saveHotel: SaveHotel
    ) : Call<UpdateResponse>

    @PATCH("addreviewshotel/{hotel_id}")
    fun addHotelReview(
        @Path("hotel_id") hotel_id : String,
        @Body reviewData : ReviewData
    ) : Call<UpdateResponse>

    @GET("tophotelreviews/{hotel_id}")
    fun topHotelReviews(
        @Path("hotel_id") hotel_id : String
    ) : Call<List<VendorReviewsData>>

    @GET("gethotelreview/{hotel_id}")
    fun allHotelReviews(
        @Path("hotel_id") hotel_id : String
    ) : Call<List<AllReviewsData>>

    @GET("getquickreview")
    fun getQuickReviews() : Call<List<GetQuickReviewsData>>


}