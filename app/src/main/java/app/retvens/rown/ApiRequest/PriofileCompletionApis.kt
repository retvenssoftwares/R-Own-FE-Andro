package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.ProfileCompletion.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PriofileCompletionApis{

    @PATCH("/update/{user_id}")
    fun setUsername(
        @Path("user_id") user_id : String,
        @Body userName: UpdateUserName
    ):Call<UpdateResponse>

    @PATCH("/update/{user_id}")
    fun setLocation(
        @Path("user_id") user_id : String,
        @Body location: LocationClass
    ):Call<UpdateResponse>

    @PATCH("/update/{user_id}")
    fun setJobRole(
        @Path("user_id") user_id : String,
        @Body role: BasicInfoClass
    ):Call<UpdateResponse>

    @PATCH("/update/{user_id}")
    fun setJobDetail(
        @Path("user_id") user_id : String,
        @Body details: JobData
    ):Call<UpdateResponse>

    @PATCH("/update/{user_id}")
    fun setHospitalityExpertDetails(
        @Path("user_id") user_id : String,
        @Body details: HospitalityexpertData
    ):Call<UpdateResponse>

    @Multipart
    @POST("hotelpost")
    fun uploadHotelData(
        @Part("hotelName") Name: RequestBody,
        @Part("hotelAddress") address: RequestBody,
        @Part("hotelRating") hotel_rating: RequestBody,
        @Part hotelData: MultipartBody.Part,
        @Part("hotelownerid") id: RequestBody,
    ): Call<UpdateResponse>

}
