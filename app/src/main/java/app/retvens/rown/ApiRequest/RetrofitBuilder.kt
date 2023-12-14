package app.retvens.rown.ApiRequest

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.UUID
import java.util.concurrent.TimeUnit

const val Base_url = "http://64.227.150.47/main/"
//const val Feed_url = "http://api.lihttp://64.227.150.47/main/verown.com/mainFeed/"
//const val Explore_url = "http://api.liverown.com/mainExplore/"

object RetrofitBuilder {

    val gson = GsonBuilder().setLenient().create()

    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(Base_url)
        .build()
        .create(RownUrl::class.java)

    val retrofitBuilder2 = Retrofit.Builder()
        .client(getUnsafeOkHttpClient())
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

    val exploreApis = retrofitBuilder2.create(ExploreApis::class.java)

    val Notification = retrofitBuilder2.create(Notification::class.java)

    val calling = retrofitBuilder2.create(CallingInterface::class.java)


    fun getUnsafeOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", UUID.randomUUID().toString())
                    .build()
                chain.proceed(newRequest)
            }
        return builder.build()

    }
}