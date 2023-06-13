package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.hotels.HotelDetailsProfileActivity
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsName
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.NavigationFragments.profile.services.UpdatePrice
import app.retvens.rown.sideNavigation.faqData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApis {

    @GET("getevent/{user_id}")
    fun getProfileEvents(
        @Path("user_id") user_id : String
    ) : Call<List<OnGoingEventsData>>

    @GET("gethotelname/{user_id}")
    fun getProfileHotels(
        @Path("user_id") user_id : String
    ) : Call<List<HotelsName>>

    @GET("getservice/{user_id}")
    fun getProfileService(
        @Path("user_id") user_id : String
    ) : Call<List<ProfileServicesDataItem>>

    @GET("getservicename")
    fun getProfileServiceName(
    ) : Call<List<ProfileServicesDataItem>>

    @GET("getHotelbyHotelid/{hotel_id}")
    fun getHotelInfo(
        @Path("hotel_id") hotel_id : String
    ) : Call<HotelData>

    @Multipart
    @PATCH("updatehoteldata/{hotel_id}")
    fun updateHotel(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
        @Part gallery : List<MultipartBody.Part>
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("updatehoteldata/{hotel_id}")
    fun updateHotelWithoutImg(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("updatehoteldata/{hotel_id}")
    fun removeHotel(
        @Path("hotel_id") hotel_id:String,
        @Part("display_status") display_status: RequestBody
    ):Call<UpdateResponse>

    @GET("getfaq")
    fun getFAQ() : Call<List<faqData>>

    @DELETE("deleteservice/{vendorServiceId}")
    fun deleteService(
        @Path("vendorServiceId") vendorServiceId : String
    ) : Call<UpdateResponse>

    @PATCH("updateprice/{vendorServiceId}")
    fun updatePrice(
        @Path("vendorServiceId") vendorServiceId : String,
        @Body updatePrice: UpdatePrice
    ) : Call<UpdateResponse>

    @Multipart
    @POST("postbug")
    fun postBug(
        @Part bugimg : List<MultipartBody.Part>,
        @Part("description_bug") description_bug: RequestBody
    ) : Call<UpdateResponse>

    @Multipart
    @PATCH("update/{user_id}")
    fun updateUserProfileWithoutPDF(
        @Path("user_id") user_id : String,
        @Part("Full_name") Name: RequestBody,
        @Part("userBio") userBio: RequestBody,
        @Part("Gender") Gender: RequestBody,
        @Part Profile_pic: MultipartBody.Part,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("update/{user_id}")
    fun updateUserProfileWithPDFImg(
        @Path("user_id") user_id : String,
        @Part("Full_name") Name: RequestBody,
        @Part("userBio") userBio: RequestBody,
        @Part("Gender") Gender: RequestBody,
        @Part Profile_pic: MultipartBody.Part,
        @Part resume: MultipartBody.Part,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("update/{user_id}")
    fun updateUserProfileWithPDF(
        @Path("user_id") user_id : String,
        @Part("Full_name") Name: RequestBody,
        @Part("userBio") userBio: RequestBody,
        @Part("Gender") Gender: RequestBody,
        @Part resume: MultipartBody.Part,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("update/{user_id}")
    fun updateUserProfileWithoutImgPDF(
        @Path("user_id") user_id : String,
        @Part("Full_name") Name: RequestBody,
        @Part("userBio") userBio: RequestBody,
        @Part("Gender") Gender: RequestBody,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("update/{user_id}")
    fun updateVendorProfile(
        @Path("user_id") user_id : String,
        @Part("Full_name") Name: RequestBody,
        @Part("userBio") userBio: RequestBody,
        @Part Profile_pic: MultipartBody.Part,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("update/{user_id}")
    fun updateVendorProfileWithoutImg(
        @Path("user_id") user_id : String,
        @Part("Full_name") Name: RequestBody,
        @Part("userBio") userBio: RequestBody,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("hotelowner/{user_id}")
    fun updateHotel(
        @Path("user_id") user_id : String,
        @Part("hotelownerName") hotelownerName: RequestBody,
        @Part("hotelDescription") hotelDescription: RequestBody,
        @Part("hotelType") hotelType: RequestBody,
        @Part("websiteLink") websiteLink: RequestBody,
        @Part("bookingEngineLink") bookingEngineLink: RequestBody,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("updatehoteldata/{hotel_id}")
    fun updateHotelLogo(
        @Path("hotel_id") hotel_id:String,
        @Part hotelLogo: MultipartBody.Part
    ):Call<UpdateResponse>

}