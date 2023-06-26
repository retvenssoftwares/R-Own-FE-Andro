package app.retvens.rown.NavigationFragments.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SavePost
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetComment
import app.retvens.rown.bottomsheet.BottomSheetLocation
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import me.relex.circleindicator.CircleIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PostDetailsActivity : AppCompatActivity() {

    lateinit var viewPagerAdapter: ImageSlideActivityAdapter
    lateinit var indicator: CircleIndicator

    lateinit var savedPost : ImageView
    var save = true
    var operatioin = "push"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        findViewById<ImageButton>(R.id.createCommunity_backBtn).setOnClickListener { onBackPressed() }

        savedPost = findViewById(R.id.savePost)

        val name = findViewById<TextView>(R.id.user_name_post)
        val profile = findViewById<ShapeableImageView>(R.id.post_profile)

        val username = findViewById<TextView>(R.id.user_name)
        val caption = findViewById<TextView>(R.id.caption)
        val likeButton = findViewById<ImageView>(R.id.like_post)
        val likedAnimation = findViewById<ImageView>(R.id.likedAnimation)
        val commentButtom = findViewById<ImageView>(R.id.comment)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        indicator = findViewById<CircleIndicator>(R.id.indicator)
        val likeC = findViewById<TextView>(R.id.like_count)
        val commentC = findViewById<TextView>(R.id.comment_count)
        val time = findViewById<TextView>(R.id.post_time)
        val postLocation = findViewById<TextView>(R.id.location)

        name.text = intent.getStringExtra("profileName").toString()
        username.text = intent.getStringExtra("userName").toString()
        caption.text = intent.getStringExtra("caption").toString()

        val profilePic = intent.getStringExtra("profilePic")

        Glide.with(applicationContext).load(profilePic).into(profile)

        val postPic = intent.getStringArrayListExtra("postPic")
        val likeCount = intent.getStringExtra("likeCount")?.toInt()
        val commentCount = intent.getStringExtra("commentCount")
        val location = intent.getStringExtra("location")

        postLocation.text = location

        likeC.text = likeCount.toString()
        commentC.text = commentCount.toString()

        val timeStamp = intent.getStringExtra("time")

        val exactTime = convertTimeToText(timeStamp!!)

        time.text = exactTime

        Log.e("pic",postPic.toString())

        val postId = intent.getStringExtra("postId").toString()
        val like = intent.getStringExtra("like").toString()

        viewPagerAdapter = ImageSlideActivityAdapter(baseContext, postPic!!, like, postId, likeButton, likedAnimation)
        viewPager.adapter = viewPagerAdapter


        indicator.setViewPager(viewPager)

        var isLike = intent.getStringExtra("islike").toBoolean()

        if (like == "Liked" || like == "liked"){
            likeButton.setImageResource(R.drawable.liked_vectore)
        }else if (like == "Unliked" || like == "not liked"){
            likeButton.setImageResource(R.drawable.svg_like_post)
        }


        likeButton.setOnClickListener {

            likePost(postId)
            var count:Int
            if (like == "Liked" || like == "liked"){
                isLike = !isLike
                if (isLike){
                    likeButton.setImageResource(R.drawable.svg_like_post)
                    count = likeCount!!.toInt()-1
                    likeC.text = count.toString()
                }else{
                    likeButton.setImageResource(R.drawable.liked_vectore)
                    count = likeCount!!.toInt()
                    likeC.text = count.toString()
                }
            }
            if (like == "Unliked" || like == "not liked"){
                isLike = !isLike
                if (isLike){
                    likeButton.setImageResource(R.drawable.liked_vectore)
                    count = likeCount!!.toInt()+1
                    likeC.text = count.toString()
                }else{
                    likeButton.setImageResource(R.drawable.svg_like_post)
                    count = likeCount!!.toInt()
                    likeC.text = count.toString()
                }
            }

        }



        commentButtom.setOnClickListener {
            val bottomSheet = BottomSheetComment(postId!!,profilePic!!)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
        }

        val saved = intent.getStringExtra("saved")

        if (saved == "saved"){
            operatioin = "pop"
            save = false
            savedPost.setImageResource(R.drawable.svg_saved)
        } else {
            operatioin = "push"
            save = true
            savedPost.setImageResource(R.drawable.svg_save_post)
        }

        savedPost.setOnClickListener {
            savePosts(postId!!)
        }



//        val count:Int
//        if(media.islike){
//            media.likePost.setImageResource(R.drawable.liked_vectore)
//            count = media.Like_count.toInt()+1
//            media.likeCount.text = count.toString()
//        }else{
//            binding.likePost.setImageResource(R.drawable.svg_like_post)
//            count = banner.Like_count.toInt()
//            binding.likeCount.text = count.toString()
//        }

    }
    private fun savePosts(blogId: String) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.feedsApi.savePost(user_id, SavePost(operatioin,blogId!!))
        savePost.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (save){
                        savedPost.setImageResource(R.drawable.svg_saved)
                        operatioin = "pop"
                        save = !save
                    } else {
                        savedPost.setImageResource(R.drawable.svg_save_post)
                        operatioin = "push"
                        save = !save
                    }
                    Toast.makeText(applicationContext, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("savePost", "${response.toString()} ${response.body().toString()}")
                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun likePost(postId: String?) {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()


        val data = LikesCollection(user_id)

        val like = RetrofitBuilder.feedsApi.postLike(postId!!,data)

        like.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {

                if (response.isSuccessful) {
                    val response = response.body()!!
                    Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }

            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {

                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

    fun convertTimeToText(dataDate: String): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = "Ago"

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val pasTime: Date = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)

            if (second < 60) {
                convTime = "$second Sec $suffix"
            } else if (minute < 60) {
                convTime = "$minute Min $suffix"
            } else if (hour < 24) {
                convTime = "$hour Hrs $suffix"
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = "${day / 360} Yr $suffix"
                } else if (day > 30) {
                    convTime = "${day / 30} Months $suffix"
                } else {
                    convTime = "${day / 7} Week $suffix"
                }
            } else if (day < 7) {
                convTime = "$day D $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }
}