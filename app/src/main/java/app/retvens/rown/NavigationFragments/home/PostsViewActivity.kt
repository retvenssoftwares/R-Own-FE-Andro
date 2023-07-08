package app.retvens.rown.NavigationFragments.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SavePost
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetComment
import app.retvens.rown.bottomsheet.BottomSheetLocation
import com.bumptech.glide.Glide
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener
import me.relex.circleindicator.CircleIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsViewActivity : AppCompatActivity() {

    lateinit var viewPagerAdapter: ImageSlideActivityAdapter
    lateinit var indicator: CircleIndicator
    lateinit var profilePic:String
    lateinit var postId:String
    private var image:ArrayList<String> = ArrayList()
    lateinit var caption:String
    private lateinit var captions:TextView
    lateinit var progressDialog:Dialog
    lateinit var savedPost : ImageView
    var save = true
    var operatioin = "push"

    var like = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_view)

//        val postImage = findViewById<ShapeableImageView>(R.id.post_pic)
        captions = findViewById<TextView>(R.id.caption)
        val createCommunity_backBtn = findViewById<ImageButton>(R.id.createCommunity_backBtn)

        savedPost = findViewById(R.id.savedPost)

        val likeButton = findViewById<ImageView>(R.id.like_post)
        val likedAnimation = findViewById<ImageView>(R.id.likedAnimation)
        val commentButton = findViewById<ImageView>(R.id.comment)
        val viewPager = findViewById<ViewPager>(R.id.post_pic)
        indicator = findViewById<CircleIndicator>(R.id.indicator)

        try {
            captions.text = intent.getStringExtra("caption")
            image = intent.getStringArrayListExtra("postPic")!!
            postId = intent.getStringExtra("postId").toString()
            profilePic = intent.getStringExtra("profilePic").toString()
        }catch (e:NullPointerException){
            val postIds = intent.getStringExtra("postId").toString()
            getPost(postIds)
        }




        like = intent.getStringExtra("like").toString()

//        Glide.with(applicationContext).load(image).into(postImage)
        if (like == "Liked"){
            likeButton.setImageResource(R.drawable.liked_vectore)
        }else if (like == "Unliked"){
            likeButton.setImageResource(R.drawable.svg_like_post)
        }

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({



//            viewPagerAdapter = ImageSlideActivityAdapter(baseContext,image!!, like, postId, likeButton, likedAnimation)
//            viewPager.adapter = viewPagerAdapter

        },1000)



        createCommunity_backBtn.setOnClickListener(DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View?) {
//                Toast.makeText(applicationContext, "SINGLE TAP DETECTED!!!", Toast.LENGTH_LONG).show();
                onBackPressed()
            }

            override fun onDoubleClick(view: View?) {
            }
        }))

        indicator.setViewPager(viewPager)

        if (like == "Liked" || like == "liked"){
            likeButton.setImageResource(R.drawable.liked_vectore)
        }else if (like == "Unliked" || like == "not liked"){
            likeButton.setImageResource(R.drawable.svg_like_post)
        }

        likeButton.setOnClickListener {

            if (like == "Unliked") {
                likeButton.setImageResource(R.drawable.liked_vectore)
                likePost(postId)
            } else {
                likeButton.setImageResource(R.drawable.svg_like_post)
                likePost(postId)
            }
        }

        commentButton.setOnClickListener {
            val bottomSheet = BottomSheetComment(postId!!,profilePic!!)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
        }

        val saved = intent.getStringExtra("isSaved")

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
            savePosts(postId)
        }

    }

    private fun getPost(postIds:String) {

        val getpost = RetrofitBuilder.feedsApi.getPosts(postIds)

        getpost.enqueue(object : Callback<PostItem?> {
            override fun onResponse(call: Call<PostItem?>, response: Response<PostItem?>) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    profilePic = response.Profile_pic
                    captions.text = response.caption
                    postId = response.post_id
                    response.media.forEach {
                        image.add(it.post)
                    }
                }
            }

            override fun onFailure(call: Call<PostItem?>, t: Throwable) {

            }
        })

    }

    private fun likePost(postId: String) {



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

}