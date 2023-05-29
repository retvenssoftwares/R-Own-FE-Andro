package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore.ExploreJobData

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExploreApis {

    @GET("getExplorePosts/{user_id}")
    fun getExplorePost(
        @Path("user_id")user_id:String,
        @Query("page")page:String
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

    @GET("getSearchJob/{text}")
    fun searchJob(
        @Path("text")text:String,
        @Query("page")page:String
    ):Call<List<ExploreJobData>>

}