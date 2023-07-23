package app.retvens.rown.ApiRequest

import app.retvens.rown.Dashboard.notificationScreen.personal.PersonalNotificationDataItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Notification {

    @GET("getPersonalNotification/{user_id}")
    fun getPersonalNotification(
        @Path("user_id") user_id : String,
        @Query("page") page: String
    ) : Call<List<PersonalNotificationDataItem>>

    @GET("getconnectionNotification/{user_id}")
    fun getConnectionNotification(
        @Path("user_id") user_id : String,
        @Query("page") page: String
    ) : Call<List<PersonalNotificationDataItem>>

}