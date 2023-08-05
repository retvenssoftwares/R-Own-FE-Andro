package app.retvens.rown.ApiRequest


import app.retvens.rown.DataCollections.ProfileCompletion.LocationClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SaveEvent
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.NavigationFragments.eventsForHoteliers.BottomEventCategoriesDataItem
import app.retvens.rown.NavigationFragments.profile.hotels.HotelsName
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllCategoriesData
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

interface EventApis {

    @GET("allEvent/{user_id}")
    fun getAllEvents(
        @Path("user_id") user_id : String,
    ) : Call<List<OnGoingEventsData>>

    @GET("getEventCategory/{user_id}/{category_id}")
    fun getEventsByCategory(
        @Path("user_id") user_id : String,
        @Path("category_id") category_id : String
    ) : Call<List<OnGoingEventsData>>

    @GET("getEventCategory")
    fun getEventCategory() : Call<List<ViewAllCategoriesData>>

    @GET("ongoingEvent")
    fun getOnGoingEvents() : Call<List<OnGoingEventsData>>

    @GET("getEvent/{user_id}")
    fun getEventByUserId(
        @Path("user_id") user_id : String,
    ) : Call<List<OnGoingEventsData>>

    @GET("getHotelInPost/{location}")
    fun getBottomSheetVenueByLocation(
        @Path("location") location : String
    ) : Call<List<HotelsName>>

    @GET("getEventCategory")
    fun getBottomSheetEventCategories() : Call<List<BottomEventCategoriesDataItem>>

//    @PATCH("deleterequest/saveid/{user_id}")
    @PATCH("saveid/{User_id}")
    fun saveEvent(
        @Path("User_id") User_id : String,
        @Body saveEvent: SaveEvent
    ) : Call<UpdateResponse>

    @Multipart
    @POST("postEvent")
    fun uploadEvent(
//        @Path("user_id") user_id : String,
        @Part("User_id") User_id : RequestBody,
        @Part("location") location: RequestBody,
        @Part("venue") venue: RequestBody,
        @Part("category_id") category_id: RequestBody,
//        @Part("country") country: RequestBody,
//        @Part("state") state: RequestBody,
//        @Part("city") city: RequestBody,
        @Part("event_title") event_title: RequestBody,
        @Part("event_description") event_description: RequestBody,
        @Part("event_category") event_category: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("website_link") website_link: RequestBody,
        @Part("booking_link") booking_link: RequestBody,
        @Part("price") price: RequestBody,
        @Part event_thumbnail: MultipartBody.Part,
        @Part("event_start_date") event_start_date: RequestBody,
        @Part("event_start_time") event_start_time: RequestBody,
        @Part("event_end_date") event_end_date: RequestBody,
        @Part("event_end_time") event_end_time: RequestBody,
        @Part("registration_start_date") registration_start_date: RequestBody,
        @Part("registration_start_time") registration_start_time: RequestBody,
        @Part("registration_end_date") registration_end_date: RequestBody,
        @Part("registration_end_time") registration_end_time: RequestBody,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("updateEvent/{user_id}")
    fun updateEvent(
        @Path("user_id") User_id : String,
        @Part("user_id") user_id : RequestBody,
        @Part("location") location: RequestBody,
        @Part("venue") venue: RequestBody,
        @Part("category_id") category_id: RequestBody,
//        @Part("country") country: RequestBody,
//        @Part("state") state: RequestBody,
//        @Part("city") city: RequestBody,
        @Part("event_title") event_title: RequestBody,
        @Part("event_description") event_description: RequestBody,
        @Part("event_category") event_category: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("website_link") website_link: RequestBody,
        @Part("booking_link") booking_link: RequestBody,
        @Part("price") price: RequestBody,
        @Part event_thumbnail: MultipartBody.Part,
        @Part("event_start_date") event_start_date: RequestBody,
        @Part("event_start_time") event_start_time: RequestBody,
        @Part("event_end_date") event_end_date: RequestBody,
        @Part("event_end_time") event_end_time: RequestBody,
        @Part("registration_start_date") registration_start_date: RequestBody,
        @Part("registration_start_time") registration_start_time: RequestBody,
        @Part("registration_end_date") registration_end_date: RequestBody,
        @Part("registration_end_time") registration_end_time: RequestBody,
    ) : Call<UserProfileResponse>

    @Multipart
    @PATCH("updateEvent/{user_id}")
    fun updateEventWithoutImg(
        @Path("user_id") User_id : String,
        @Part("user_id") user_id : RequestBody,
        @Part("location") location: RequestBody,
        @Part("venue") venue: RequestBody,
        @Part("category_id") category_id: RequestBody,
//        @Part("country") country: RequestBody,
//        @Part("state") state: RequestBody,
//        @Part("city") city: RequestBody,
        @Part("event_title") event_title: RequestBody,
        @Part("event_description") event_description: RequestBody,
        @Part("event_category") event_category: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("website_link") website_link: RequestBody,
        @Part("booking_link") booking_link: RequestBody,
        @Part("price") price: RequestBody,
        @Part("event_start_date") event_start_date: RequestBody,
        @Part("event_start_time") event_start_time: RequestBody,
        @Part("event_end_date") event_end_date: RequestBody,
        @Part("event_end_time") event_end_time: RequestBody,
        @Part("registration_start_date") registration_start_date: RequestBody,
        @Part("registration_start_time") registration_start_time: RequestBody,
        @Part("registration_end_date") registration_end_date: RequestBody,
        @Part("registration_end_time") registration_end_time: RequestBody,
    ) : Call<UserProfileResponse>

}