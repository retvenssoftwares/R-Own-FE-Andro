package app.retvens.rown.NavigationFragments.home

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikeDataClass
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timerTask

class PostLikeActivity : AppCompatActivity() {
    private lateinit var recycler:RecyclerView
    private lateinit var adapter: LikeAdapter
    private lateinit var postId:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_like)

        val back = findViewById<ImageView>(R.id.re_backBtn)

        back.setOnClickListener {
            onBackPressed()
        }

        postId = intent.getStringExtra("like").toString()

        recycler = findViewById(R.id.likeUserRecycler)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        fetchLike(postId)

    }

    private fun fetchLike(postId: String?) {

        val fetch = RetrofitBuilder.feedsApi.getLike(postId!!)

        fetch.enqueue(object : Callback<LikeDataClass?> {
            override fun onResponse(
                call: Call<LikeDataClass?>,
                response: Response<LikeDataClass?>
            ) {
               if (response.isSuccessful){
                   val response = response.body()!!
                   adapter = LikeAdapter(applicationContext,response)
                   recycler.adapter = adapter
                   adapter.notifyDataSetChanged()
               }else{
                   Log.e("error",response.code().toString())
               }
            }

            override fun onFailure(call: Call<LikeDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }
}