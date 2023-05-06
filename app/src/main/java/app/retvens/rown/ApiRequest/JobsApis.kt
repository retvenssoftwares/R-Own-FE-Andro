package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.JobsCollection.AppliedJobData
import app.retvens.rown.DataCollections.JobsCollection.JobResponseDataClass
import app.retvens.rown.DataCollections.JobsCollection.JobsData
import app.retvens.rown.DataCollections.JobsCollection.RequestJobDataClass
import app.retvens.rown.NavigationFragments.home.DataItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JobsApis {

    @GET("getjob")
    fun getJobs(): Call<List<JobsData>>

    @GET("requestJob/{userId}")
    fun appliedJobs(
        @Path("userId") userId : String
    ): Call<AppliedJobData>

    @POST("requestJob")
    fun requestJob(@Body post:RequestJobDataClass):Call<JobResponseDataClass>

}