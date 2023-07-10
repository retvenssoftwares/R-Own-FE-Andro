package app.retvens.rown.ApiRequest

import androidx.room.SkipQueryVerification
import app.retvens.rown.Dashboard.profileCompletion.ProfileCompletionStatus
import app.retvens.rown.DataCollections.ProfileCompletion.*
import app.retvens.rown.DataCollections.location.CityData
import app.retvens.rown.DataCollections.location.CountryData
import app.retvens.rown.DataCollections.location.StateData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface PriofileCompletionApis{

    @Multipart
    @PATCH("/update/{user_id}")
    fun setUsername(
        @Path("user_id") user_id : String,
        @Part("Full_name")Full_name:RequestBody,
        @Part("DOB")DOB:RequestBody,
        @Part("User_name")User_id:RequestBody
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("/update/{user_id}")
    fun setBio(
        @Path("user_id") user_id : String,
        @Part("userBio") userBio: RequestBody,
        @Part("Gender") Gender: RequestBody
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("/update/{user_id}")
    fun setLocation(
        @Path("user_id") user_id : String,
        @Part("location")location:String
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("/update/{user_id}")
    fun verifyUsername(
        @Path("user_id")user_id:String,
        @Part("User_name")User_name:RequestBody
    ):Call<UpdateResponse>

//    @PATCH("/update/{user_id}")
//    fun verifyUsername(
//        @Path("user_id") user_id : String,
//        @Body verifyUsername: VerifyUsername
//    ):Call<UpdateResponse>

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
    fun profileCompletionStatus(
        @Path("user_id") user_id : String,
        @Body profileCompletionStatus : ProfileCompletionStatus
    ):Call<UpdateResponse>

    @PATCH("/update/{user_id}")
    fun setHospitalityExpertDetails(
        @Path("user_id") user_id : String,
        @Body details: HospitalityexpertData
    ):Call<UpdateResponse>

    @PATCH("/hotelowner/{user_id}")
    fun setHotelInfo(
        @Path("user_id") user_id : String,
        @Body details: HotelOwnerInfoData
    ):Call<UpdateResponse>

    @Multipart
    @POST("hotelpost")
    fun uploadHotelData(
        @Part("user_id")user_id:RequestBody,
        @Part("hotelName") Name: RequestBody,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelAddress") address: RequestBody,
        @Part("hotelRating") hotel_rating: RequestBody,
        @Part hotelLogo: MultipartBody.Part,
        @Part hotelCoverpic: MultipartBody.Part
    ): Call<UpdateResponse>

    @Multipart
    @POST("hotelpost")
    fun uploadHotelChainData(
        @Part("hotelName") Name: RequestBody,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelAddress") address: RequestBody,
        @Part("hotelRating") hotel_rating: RequestBody,
        @Part hotelCoverpic: MultipartBody.Part,
        @Part("user_id") id: RequestBody,
    ): Call<UpdateResponse>

    @Multipart
    @POST("hotelpost")
    fun addHotelProfile(
        @Part("hotelName") Name: RequestBody,
        @Part("hotelAddress") address: RequestBody,
        @Part("hotelRating") hotel_rating: RequestBody,
        @Part hotelCoverpic: MultipartBody.Part,
        @Part("user_id") id: RequestBody,
    ): Call<UpdateResponse>


    @GET("countries")
    fun getCountries():Call<List<CountryData>>
    @GET("countries/{numeric_code}/states")
    fun getStates(
        @Path("numeric_code") numeric_code : String
    ):Call<List<StateData>>

    @GET("countries/{numeric_code}/states/{state_code}/cities")
    fun getCities(
        @Path("numeric_code") numeric_code : String,
        @Path("state_code") state_code : String
    ):Call<List<CityData>>

    @GET("getjobtitle")
    fun getJobTitle():Call<List<GetJobDataClass>>

    @GET("getcompany")
    fun getCompany():Call<List<CompanyDatacClass>>

    @Multipart
    @PATCH("/vendor/{user_id}")
    fun uploadVendorData(
        @Path("user_id") user_id : String,
        @Part Vendorimg: MultipartBody.Part,
        @Part("vendorName") vendorName: RequestBody,
        @Part("vendorDescription") vendorDescription: RequestBody,
        @Part portfolioImages1:MultipartBody.Part,
        @Part portfolioImages2:MultipartBody.Part,
        @Part portfolioImages3:MultipartBody.Part,
        @Part("websiteLink") websiteLink: RequestBody
    ): Call<UpdateResponse>

    @Multipart
    @PATCH("/vendor/{user_id}")
    fun updateVendorData(
        @Path("user_id") user_id : String,
        @Part Vendorimg: MultipartBody.Part,
        @Part("vendorDescription") vendorDescription: RequestBody,
        @Part("websiteLink") websiteLink: RequestBody,
        @Part portfolioLinkdata: List<MultipartBody.Part>
    ): Call<UpdateResponse>

    @GET("getservicename")
    fun getServices():Call<List<VendorServicesData>>

    @PATCH("postservice/{user_id}")
    fun setServices(
        @Path("user_id") user_id : String,
        @Body details: PostVendorSerivces
    ):Call<UpdateResponse>

    @POST("postservice")
    fun postServices(
        @Body details: PostVendorSerivces
    ):Call<UpdateResponse>

    @Multipart
    @POST("/document")
    fun uploadDocs(
        @Part("user_id") user_id : RequestBody,
//        @Part("CountryorRegion") CountryorRegion: RequestBody,
        @Part("Category") Category: RequestBody,
        @Part("User_name") User_name: RequestBody,
        @Part("Full_name") Full_name: RequestBody,
        @Part Documents: MultipartBody.Part,
    ): Call<UpdateResponse>

    @GET("checkVerification/{user_id}")
    fun appliedFor(
        @Path("user_id") user_id : String
    ):Call<UpdateResponse>

    @PATCH("update/{userId}")
    fun updateExperience(
        @Path("userId") user_id : String,
        @Body updateData:AddExperienceDataClass
    ):Call<UpdateResponse>

    @PATCH("addmultiple/{userId}")
    fun addExperience(
        @Path("userId") user_id : String,
        @Body updateData:AddExperienceDataClass
    ):Call<UpdateResponse>
}
