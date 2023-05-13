package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.JobsCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.home.DataItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface JobsApis {

    @GET("getjob")
    fun getJobs(): Call<List<JobsData>>

    @GET("getJob/{userId}")
    fun getIndividualJobs(
        @Path("userId") userId : String
    ): Call<List<JobsData>>

    @GET("requestJob/{userId}")
    fun appliedJobs(
        @Path("userId") userId : String
    ): Call<AppliedJobData>

    @POST("requestJob")
    fun requestJob(@Body post:RequestJobDataClass):Call<JobResponseDataClass>

    @POST("jobpost/{userId}")
    fun postJob(
        @Path("userId") userId:String,
        @Body post: PostJobDataCLass
    ):Call<JobResponseDataClass>

    @Multipart
    @POST("applyjob")
    fun applyJob(
        @Part("user_id")user_id:RequestBody,
        @Part("jobId")jobId:RequestBody,
        @Part resume: MultipartBody.Part
    ):Call<UpdateResponse>
}