package app.retvens.rown.NavigationFragments.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SavePost
import app.retvens.rown.NavigationFragments.TimesStamp
import app.retvens.rown.NavigationFragments.profile.profileForViewers.OwnerProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.UserProfileActivity
import app.retvens.rown.NavigationFragments.profile.profileForViewers.VendorProfileActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetComment
import app.retvens.rown.bottomsheet.BottomSheetEditYourPost
import app.retvens.rown.bottomsheet.BottomSheetLocation
import app.retvens.rown.bottomsheet.BottomSheetPostEdit
import app.retvens.rown.bottomsheet.BottomSheetReport
import app.retvens.rown.bottomsheet.BottomSheetReportPost
import app.retvens.rown.utils.postLike
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

class PostDetailsActivity : AppCompatActivity(), ImageSlideActivityAdapter.OnImageClickListener {

    lateinit var viewPagerAdapter: ImageSlideActivityAdapter
    lateinit var indicator: CircleIndicator

    lateinit var savedPost : ImageView
    lateinit var likeButton : ImageView
    lateinit var likedAnimation : ImageView
    lateinit var likeCountText : TextView
    private  var likeCount = 0
    private  var postId = ""
    private  var postid = ""
    private  var like = ""
    private  var likeSaved = ""
    private var isLike = true
    var save = true
    var operatioin = "push"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        findViewById<ImageButton>(R.id.createCommunity_backBtn).setOnClickListener { onBackPressed() }

        savedPost = findViewById(R.id.savePost)

        val actionButton = findViewById<ImageView>(R.id.actionButton)

        val name = findViewById<TextView>(R.id.user_name_post)
        val profile = findViewById<ShapeableImageView>(R.id.post_profile)

        val username = findViewById<TextView>(R.id.user_name)
        val caption = findViewById<TextView>(R.id.caption)
        likeButton = findViewById<ImageView>(R.id.like_post)
        likedAnimation = findViewById<ImageView>(R.id.likedAnimation)
        val commentButtom = findViewById<ImageView>(R.id.comment)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        indicator = findViewById<CircleIndicator>(R.id.indicator)
        likeCountText = findViewById<TextView>(R.id.like_count)
        val commentC = findViewById<TextView>(R.id.comment_count)
        val time = findViewById<TextView>(R.id.post_time)
        val postLocation = findViewById<TextView>(R.id.location)

        name.text = intent.getStringExtra("profileName").toString()
        username.text = intent.getStringExtra("userName").toString()

        //caption.text = intent.getStringExtra("caption").toString()
        val captionString: String = intent.getStringExtra("caption").toString()
        val maxCharLimit: Int = 100
        val readMoreText: String = "Read More"
        val readLessText: String = "Read Less"
        caption.text = getTrimmedText(captionString, maxCharLimit, readMoreText)
        caption.setOnClickListener {
            if (caption.text == getTrimmedText(captionString, maxCharLimit, readMoreText)) {
                // Expand the TextView to show full text
                caption.text = getTrimmedText(captionString, Int.MAX_VALUE, readLessText)
            } else {
                // Collapse the TextView to show trimmed text
                caption.text = getTrimmedText(captionString, maxCharLimit, readMoreText)
            }
        }


        val postPic = intent.getStringArrayListExtra("postPic")
        val commentCount = intent.getStringExtra("commentCount")
        val location = intent.getStringExtra("location")
        val profilePic = intent.getStringExtra("profilePic")
        val role = intent.getStringExtra("role")
        val user_id = intent.getStringExtra("user_id")
        val isSaved = intent.getStringExtra("isSaved")
        val savedFrag = intent.getStringExtra("savedFrag")
        Toast.makeText(applicationContext, "$savedFrag", Toast.LENGTH_SHORT).show()

        postId = intent.getStringExtra("postId").toString()
        postid = intent.getStringExtra("postid").toString()
        like = intent.getStringExtra("like").toString()
        likeSaved = intent.getStringExtra("likeSaved").toString()
        likeCount = intent.getStringExtra("likeCount")?.toInt()!!

        if (postId == "null"){
            postId = postid
        }
        if(like == "null"){
            like = likeSaved
        }

        if (postPic!!.isNotEmpty()) {
            Glide.with(applicationContext).load(profilePic).into(profile)
        } else {
            profile.setImageResource(R.drawable.svg_user)
        }

        profile.setOnClickListener {
            if(role == "Business Vendor / Freelancer"){
                val intent = Intent(this, VendorProfileActivity::class.java)
                intent.putExtra("userId",user_id)
                startActivity(intent)
            }else if (role == "Hotel Owner"){
                val intent = Intent(this, OwnerProfileActivity::class.java)
                intent.putExtra("userId",user_id)
                startActivity(intent)
            } else {
                val intent = Intent(this, UserProfileActivity::class.java)
                intent.putExtra("userId",user_id)
                startActivity(intent)
            }
        }

        name.setOnClickListener {
            if(role == "Business Vendor / Freelancer"){
                val intent = Intent(this, VendorProfileActivity::class.java)
                intent.putExtra("userId",user_id)
                startActivity(intent)
            }else if (role == "Hotel Owner"){
                val intent = Intent(this, OwnerProfileActivity::class.java)
                intent.putExtra("userId",user_id)
                startActivity(intent)
            } else {
                val intent = Intent(this, UserProfileActivity::class.java)
                intent.putExtra("userId",user_id)
                startActivity(intent)
            }
        }

        postLocation.text = location

        likeCountText.text = likeCount.toString()
        commentC.text = commentCount.toString()

        try {
            val timeStamp = intent.getStringExtra("time")!!

            val extact = TimesStamp.convertTimeToText(timeStamp)
            time.text = extact.toString()
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
        }





        Log.e("pic",postPic.toString())

        if (isSaved == "saved" || savedFrag == "saved"){
            operatioin = "pop"
            save = false
            savedPost.setImageResource(R.drawable.svg_saved)
        } else if (isSaved == "not saved" || savedFrag == "not saved") {
            operatioin = "push"
            save = true
            savedPost.setImageResource(R.drawable.svg_save_post)
        }

        viewPagerAdapter = ImageSlideActivityAdapter(baseContext, postPic!!)
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.setOnImageClickListener(this)


        if (postPic.size > 1) {
            indicator.setViewPager(viewPager)
        } else {
            indicator.visibility = View.GONE
        }

        var isLike = intent.getStringExtra("islike").toBoolean()

        if (like == "Liked" || like == "liked"){
            isLike = false
//            Toast.makeText(applicationContext, "$isLike", Toast.LENGTH_SHORT).show()
            likeButton.setImageResource(R.drawable.liked_vectore)
        }else if (like == "Unliked" || like == "not liked"){
            isLike = true
//            Toast.makeText(applicationContext, "$isLike", Toast.LENGTH_SHORT).show()
            likeButton.setImageResource(R.drawable.svg_like_post)
        }


        likeButton.setOnClickListener {
            if (isLike) {
                postLike(postId, applicationContext) {
                    isLike = false
                    likeButton.setImageResource(R.drawable.liked_vectore)
                    likeCount += 1
//                    post.Like_count = count.toString()
                    likeCountText.text = likeCount.toString()
                }
            } else {
                postLike(postId, applicationContext) {
                    isLike = true
                    likeButton.setImageResource(R.drawable.svg_like_post)
                    likeCount -= 1
//                    post.Like_count = count.toString()
                    likeCountText.text = likeCount.toString()
                }
            }
        }


        actionButton.setOnClickListener {

            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val User_id = sharedPreferences?.getString("user_id", "").toString()

            if (User_id == user_id){
                val bottomSheet = BottomSheetPostEdit(postId,captionString,location!!)
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetPostEdit.Hotelier_TAG)}
            }else{
                val bottomSheet = BottomSheetReportPost(user_id!!,postId)
                val fragManager = supportFragmentManager
                fragManager.let{bottomSheet.show(it, BottomSheetReportPost.RATING_TAG)}
            }


        }

        commentButtom.setOnClickListener {
            val bottomSheet = BottomSheetComment(postId,profilePic!!)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
        }

        savedPost.setOnClickListener {
            savePosts(postId)
        }
    }

    fun getTrimmedText(text: String, charLimit: Int, suffix: String): String {
        return if (text.length > charLimit) {
//           val txt = Html.fromHtml(text.substring(0, charLimit) + "..." + "<font color='blue'> <u>$suffix</u></font>")
//            txt.toString()
                text.substring(0, charLimit) + "... " + suffix
        } else {
            text
        }
    }

    private fun savePosts(blogId: String) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.feedsApi.savePost(user_id, SavePost(operatioin,blogId))
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
                    isLike = true
                    likeButton.setImageResource(R.drawable.svg_like_post)
                    likeCount -= 1
//                    post.Like_count = count.toString()
                    likeCountText.text = likeCount.toString()
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

    override fun imageClick(CTCFrBo: String) {
        if (CTCFrBo == "image"){
            val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_bottom)
            likedAnimation.startAnimation(anim)
            likedAnimation.visibility = View.VISIBLE

            var count = likeCount
            if (isLike){
                postLike(postId, applicationContext) {
                    isLike = false
                    likeButton.setImageResource(R.drawable.liked_vectore)
                    count += 1
                    likeCountText.text = count.toString()
                }
            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out_bottom)
                likedAnimation.startAnimation(anim)
                likedAnimation.visibility = View.GONE }, 1000)
        }
    }
}