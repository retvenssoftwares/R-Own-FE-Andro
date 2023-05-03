package app.retvens.rown.ApiRequest

import app.retvens.rown.DataCollections.FeedCollection.FetchPostDataClass
import retrofit2.Call
import retrofit2.http.GET

interface FeedsApi {

    @GET("getpost")
    fun getPost():Call<List<FetchPostDataClass>>

}