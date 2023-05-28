package app.retvens.rown.NavigationFragments.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.FeedCollection.LikesCollection
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetComment
import app.retvens.rown.bottomsheet.BottomSheetLocation
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        val name = findViewById<TextView>(R.id.user_name_post)
        val profile = findViewById<ShapeableImageView>(R.id.post_profile)
        val postPic = findViewById<ShapeableImageView>(R.id.post_pic)
        val username = findViewById<TextView>(R.id.user_name)
        val caption = findViewById<TextView>(R.id.caption)
        val likeButton = findViewById<ImageView>(R.id.like_post)
        val commentButtom = findViewById<ImageView>(R.id.comment)

        name.text = intent.getStringExtra("profileName").toString()
        username.text = intent.getStringExtra("userName").toString()
        caption.text = intent.getStringExtra("caption").toString()

        val postImage = intent.getStringExtra("postPic")
        val profilePic = intent.getStringExtra("profilePic")

        Glide.with(applicationContext).load(profilePic).into(profile)
        Glide.with(applicationContext).load(postImage).into(postPic)

        val postId = intent.getStringExtra("postId")

        likeButton.setOnClickListener {
            likePost(postId)
        }

        commentButtom.setOnClickListener {
            val bottomSheet = BottomSheetComment(postId!!)
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