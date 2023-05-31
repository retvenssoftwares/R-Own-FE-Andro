package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
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
}