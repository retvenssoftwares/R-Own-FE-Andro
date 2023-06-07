package app.retvens.rown.NavigationFragments.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.HomeFragment
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetComment
import app.retvens.rown.bottomsheet.BottomSheetLocation
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import me.relex.circleindicator.CircleIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsViewActivity : AppCompatActivity() {

    lateinit var viewPagerAdapter: ImageSlideActivityAdapter
    lateinit var indicator: CircleIndicator

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_view)

//        val postImage = findViewById<ShapeableImageView>(R.id.post_pic)
        val caption = findViewById<TextView>(R.id.caption)

        val likeButton = findViewById<ImageView>(R.id.like_post)
        val commentButton = findViewById<ImageView>(R.id.comment)
        val savedImage = findViewById<ImageView>(R.id.savedPost)
        val viewPager = findViewById<ViewPager>(R.id.post_pic)
        indicator = findViewById<CircleIndicator>(R.id.indicator)

        caption.text = intent.getStringExtra("caption")

        val image = intent.getStringArrayListExtra("postPic")
        val postId = intent.getStringExtra("postId")
        val profilePic = intent.getStringExtra("profilePic")

//        Glide.with(applicationContext).load(image).into(postImage)

        viewPagerAdapter = ImageSlideActivityAdapter(baseContext,image!!)
        viewPager.adapter = viewPagerAdapter


        indicator.setViewPager(viewPager)

        likeButton.setOnClickListener {

            likePost(postId)
        }

        commentButton.setOnClickListener {
            val bottomSheet = BottomSheetComment(postId!!,profilePic!!)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
        }


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


}