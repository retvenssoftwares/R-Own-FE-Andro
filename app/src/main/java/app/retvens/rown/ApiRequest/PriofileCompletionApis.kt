package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.ProfileCompletion.*
import app.retvens.rown.DataCollections.location.CityData
import app.retvens.rown.DataCollections.location.CountryData
import app.retvens.rown.DataCollections.location.StateData
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
    fun verifyUsername(
        @Path("user_id") user_id : String,
        @Body verifyUsername: VerifyUsername
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

    @PATCH("/hotelowner/{user_id}")
    fun setHotelInfo(
        @Path("user_id") user_id : String,
        @Body details: HotelOwnerInfoData
    ):Call<UpdateResponse>

    @Multipart
    @POST("hotelpost")
    fun uploadHotelData(
        @Part("hotelName") Name: RequestBody,
        @Part("hotelAddress") address: RequestBody,
        @Part("hotelRating") hotel_rating: RequestBody,
        @Part hotelLogo: MultipartBody.Part,
        @Part hotelProfilepic: MultipartBody.Part,
        @Part hotelCoverpic: MultipartBody.Part,
        @Part("hotelOwnerId") id: RequestBody,
    ): Call<UpdateResponse>

    @Multipart
    @POST("hotelpost")
    fun uploadHotelChainData(
        @Part("hotelName") Name: RequestBody,
        @Part("hotelAddress") address: RequestBody,
        @Part("hotelRating") hotel_rating: RequestBody,
        @Part hotelProfilepic: MultipartBody.Part,
        @Part("hotelOwnerId") id: RequestBody,
    ): Call<UpdateResponse>

    @GET("countries")
    fun getLocation():Call<List<LocationDataClass>>
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
    ):Call<CityData>

    @GET("getjobtitle")
    fun getJobTitle():Call<List<GetJobDataClass>>

    @GET("getcompany")
    fun getCompany():Call<List<CompanyDatacClass>>

    @Multipart
    @PATCH("/vendor/{user_id}")
    fun uploadVendorData(
        @Path("user_id") user_id : String,
        @Part("vendorName") Name: RequestBody,
        @Part("vendorDescription") address: RequestBody,
        @Part("portfolioLink") portfolio: RequestBody,
        @Part vendorImage: MultipartBody.Part,
        @Part("vendor_id") id: RequestBody,
        @Part("websiteLink") website: RequestBody
    ): Call<UpdateResponse>

    @GET("getservicename")
    fun getServices():Call<List<VendorServicesData>>

    @PATCH("vendorservice/{user_id}")
    fun setServices(
        @Path("user_id") user_id : String,
        @Body details: PostVendorSerivces
    ):Call<UpdateResponse>

    @POST("vendordata")
    fun createVendor(
        @Body vendor:CreateVendorDataClass
    ):Call<UpdateResponse>
}
