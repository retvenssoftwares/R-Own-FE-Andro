package app.retvens.rown.ApiRequest

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val Base_url = "http://64.227.150.47/"

object RetrofitBuilder {


    val gson = GsonBuilder().setLenient().create()

    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(Base_url)
        .build()
        .create(RownUrl::class.java)

    val retrofitBuilder2 = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(Base_url)
        .build()

    val profileCompletion = retrofitBuilder2.create(PriofileCompletionApis::class.java)

    val feedsApi = retrofitBuilder2.create(FeedsApi::class.java)

    val jobsApis = retrofitBuilder2.create(JobsApis::class.java)

    val connectionApi = retrofitBuilder2.create(ConnectionApis::class.java)

    val viewAllApi = retrofitBuilder2.create(ViewAll::class.java)

    val EventsApi = retrofitBuilder2.create(EventApis::class.java)

    val ProfileApis = retrofitBuilder2.create(ProfileApis::class.java)
}