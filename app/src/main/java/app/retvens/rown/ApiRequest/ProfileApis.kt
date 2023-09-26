package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.BlockAccount
import app.retvens.rown.DataCollections.ConnectionCollection.BlockUserDataClass
import app.retvens.rown.DataCollections.DeleteAccount
import app.retvens.rown.DataCollections.FeedCollection.GetCommunitiesData
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ReportDataClass
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.ExploreBlogData
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventData
import app.retvens.rown.NavigationFragments.exploreForUsers.services.ExploreServiceData
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsName
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.NavigationFragments.profile.services.UpdatePrice
import app.retvens.rown.NavigationFragments.profile.setting.saved.hotels.SavedHotelsDataItem
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
import retrofit2.http.Query

interface ProfileApis {

    @GET("fetchGroup/{user_id}")
    fun getCommunities(
        @Path("user_id")user_id:String
    ):Call<List<GetCommunitiesData>>

    @GET("getEvent/{user_id}")
    fun getProfileEvents(
        @Path("user_id") user_id : String
    ) : Call<List<OnGoingEventsData>>

    @GET("getHotel/{user_id}/{User_Id}")
    fun getProfileHotels(
        @Path("user_id") user_id : String,
        @Path("User_Id") User_id : String
    ) : Call<List<HotelsName>>

    @GET("getService/{user_id}")
    fun getProfileService(
        @Path("user_id") user_id : String
    ) : Call<List<ProfileServicesDataItem>>

    @GET("getServiceName")
    fun getProfileServiceName(
    ) : Call<List<ProfileServicesDataItem>>

    @GET("getHotelByHotelId/{hotel_id}")
    fun getHotelInfo(
        @Path("hotel_id") hotel_id : String
    ) : Call<HotelData>

    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotels1(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
        @Part galleryImages1 : MultipartBody.Part,
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotels2(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
        @Part galleryImages2 : MultipartBody.Part,
    ):Call<UpdateResponse>
    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotels3(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
        @Part galleryImages3 : MultipartBody.Part
    ):Call<UpdateResponse>
    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotels12(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
        @Part galleryImages1 : MultipartBody.Part,
        @Part galleryImages2 : MultipartBody.Part
    ):Call<UpdateResponse>
    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotels13(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
        @Part galleryImages1 : MultipartBody.Part,
        @Part galleryImages3 : MultipartBody.Part
    ):Call<UpdateResponse>
    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotels23(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
        @Part galleryImages2 : MultipartBody.Part,
        @Part galleryImages3 : MultipartBody.Part
    ):Call<UpdateResponse>
    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotels123(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
        @Part galleryImages1 : MultipartBody.Part,
        @Part galleryImages2 : MultipartBody.Part,
        @Part galleryImages3 : MultipartBody.Part
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotels(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody,
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotelWithoutImg(
        @Path("hotel_id") hotel_id:String,
        @Part("Hoteldescription") Hoteldescription: RequestBody,
        @Part("hotelName") hotelName: RequestBody,
        @Part("hotelAddress") hotelAddress: RequestBody
    ):Call<UpdateResponse>

    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun removeHotel(
        @Path("hotel_id") hotel_id:String,
        @Part("display_status") display_status: RequestBody
    ):Call<UpdateResponse>

    @GET("getFaq")
    fun getFAQ() : Call<List<faqData>>

    @DELETE("deleteService/{vendorServiceId}")
    fun deleteService(
        @Path("vendorServiceId") vendorServiceId : String
    ) : Call<UpdateResponse>

    @PATCH("updatePrice/{vendorServiceId}")
    fun updatePrice(
        @Path("vendorServiceId") vendorServiceId : String,
        @Body updatePrice: UpdatePrice
    ) : Call<UpdateResponse>

    @Multipart
    @POST("postBug")
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
        @Part("normalUserInfo[jobTitle]") designation : RequestBody,
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
        @Part("normalUserInfo[jobTitle]") designation : RequestBody,
        @Part("location")location:RequestBody
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
    @PATCH("hotelOwner/{user_id}")
    fun updateHotel(
        @Path("user_id") user_id : String,
        @Part("hotelownerName") hotelownerName: RequestBody,
        @Part("hotelDescription") hotelDescription: RequestBody,
        @Part("hotelType") hotelType: RequestBody,
        @Part("websiteLink") websiteLink: RequestBody,
        @Part("bookingEngineLink") bookingEngineLink: RequestBody,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("updateHotelData/{hotel_id}")
    fun updateHotelLogo(
        @Path("hotel_id") hotel_id:String,
        @Part hotelLogo: MultipartBody.Part
    ):Call<UpdateResponse>

    @GET("getSaveHotel/{user_id}")
    fun getSaveHotel(
        @Path("user_id") user_id : String,
        @Query("page")page:String
    ):Call<List<SavedHotelsDataItem>>

    @GET("getSavedBlogs/{user_id}")
    fun getSavedBlog(
        @Path("user_id") user_id : String,
        @Query("page")page:String
    ):Call<List<ExploreBlogData>>

    @GET("getSaveEvent/{user_id}")
    fun getSavedEvent(
        @Path("user_id") user_id : String,
        @Query("page")page:String
    ):Call<List<ExploreEventData>>

    @GET("savePost/{user_id}")
    fun getSavedPost(
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<PostsDataClass>>

    @GET("getSaveService")
    fun getSaveService(
        @Path("user_id")user_id:String,
        @Query("page")page:String
    ):Call<List<ExploreServiceData>>

    @POST("report")
    fun addreport(
        @Body report:ReportDataClass
    ):Call<UpdateResponse>

    @PATCH("deleteAcc")
    fun deleteAccount(
        @Body delete:DeleteAccount
    ):Call<UpdateResponse>

    @PATCH("block/{user_id}")
    fun blockAccount(
        @Path("user_id")user_id:String,
        @Body block:BlockAccount
    ):Call<UpdateResponse>

    @GET("userList/{user_id}")
    fun getBlockList(
        @Path("user_id")user_id:String
    ):Call<List<BlockUserDataClass>>

    @PATCH("unblock/{user_id}")
    fun unblockAccount(
        @Path("user_id")user_id:String,
        @Body block:BlockAccount
    ):Call<UpdateResponse>
}