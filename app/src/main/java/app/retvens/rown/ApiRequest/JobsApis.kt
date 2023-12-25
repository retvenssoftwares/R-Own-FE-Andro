package app.retvens.rown.ApiRequest

import UserJobAppliedData
import app.retvens.rown.DataCollections.JobsCollection.*
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.saveId.SaveEvent
import app.retvens.rown.NavigationFragments.home.DataItem
import app.retvens.rown.NavigationFragments.job.GetJobData
import app.retvens.rown.NavigationFragments.job.JobPostData
import app.retvens.rown.NavigationFragments.job.JobPostResponse
import app.retvens.rown.NavigationFragments.job.savedJobs.Job
import app.retvens.rown.NavigationFragments.job.savedJobs.SaveJob
import app.retvens.rown.NavigationFragments.job.savedJobs.SavedJobsData
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
import retrofit2.http.Query

interface JobsApis {

    @GET("getjob/{userId}")
    fun getJobs(
        @Path("userId") userId : String
    ): Call<List<GetAllJobsData>>

    @GET("getJobs/{jobId}")
    fun getJobDetail(
        @Path("jobId") jobId: String,
        @Query("userId") userId: String
    ): Call<JobDetailsDataClass>

    @GET("job/{userId}")
    fun getIndividualJobs(
        @Path("userId") userId : String
    ): Call<GetJobData>

    @GET("appliedjob/{userId}")
    fun appliedJobs(
        @Path("userId") userId : String
    ): Call<List<AppliedJobData>>

//    @GET("reqjobofuser/{userId}")
//    fun userAppliedJobs(
//        @Path("userId") userId : String
//    ): Call<List<AppliedJobData>>

    @POST("requestJob")
    fun requestJob(@Body post:RequestJobDataClass):Call<JobResponseDataClass>

    @GET("getrequestedjob")
    fun getRequestJob():Call<List<GetRequestedJobDara>>

    @POST("jobpost/{userId}")
    fun postJob(
        @Path("userId") userId : String,
        @Body post: JobPostData):Call<JobPostResponse>

    @Multipart
    @POST("applyjob")
    fun applyJob(
        @Part("Full_name")Full_name:RequestBody,
        @Part("Experience")Experience:RequestBody,
        @Part("self_introduction")self_introduction:RequestBody,
        @Part("user_id")user_id:RequestBody,
        @Part("jobId")jobId:RequestBody,
        @Part resume: MultipartBody.Part
    ):Call<ApplyJobsResponse>

    @PATCH("pushid/{user_id}")
    fun pushId(
        @Path("user_id") user_id:String,
        @Body push:PushApplicantIdData
    ):Call<UpdateResponse>


    @GET("getspecificjob/{jId}")
    fun getApplicant(
        @Path("jId") jId:String
    ):Call<JobDetailsData>

    @GET("getapplicant")
    fun userAppliedJobs(
        @Query("page") page: Int,
        @Query("userId") userId:String
    ):Call<List<UserJobAppliedData>>

    @GET("getcandidate/{appId}")
    fun getCandidate(
        @Path("appId") appId:String
    ):Call<List<CandidateDataClass>>

    @PATCH("updatejob/{appId}")
    fun updateStatus(
        @Path("appId") appId:String,
        @Body status:StatusDataClass
    ):Call<UpdateResponse>

//    @GET("getbookmark/{userId}")
//    fun getSavedJobs(
//        @Path("userId") userId : String
//    ): Call<SavedJobsData>

    @GET("getSaveJob/{user_id}")
    fun getSavedJobs(
        @Path("user_id") userId : String,
        @Query("page") page: Int
    ): Call<List<SavedJobsData>>

//    @PATCH("saveid/{User_id}")
//    fun saveJobs(
//        @Path("User_id") User_id : String,
//        @Body saveJob: SaveJob
//    ) : Call<UpdateResponse>

    @PATCH("saveId/{user_id}")
    fun saveJobs(
        @Path("user_id") userId : String,
        @Body saveJob: SaveJob
    ) : Call<UpdateResponse>

    @GET("getCompaniesList")
    fun getHotelList(
        @Query("userId")userId:String
    ):Call<List<HotelsList>>
}