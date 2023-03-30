package app.retvens.rown.ApiRequest

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val Base_url = "https://rown-retvens.vercel.app/"

object RetrofitBuilder {


    val gson = GsonBuilder().setLenient().create()

    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(Base_url)
        .build()
        .create(RownUrl::class.java)


}