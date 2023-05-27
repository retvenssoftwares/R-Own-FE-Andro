package app.retvens.rown.NavigationFragments.exploreForUsers.blogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SaveBlog
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityBlogsDetailsBinding
import app.retvens.rown.utils.dateFormat
import app.retvens.rown.viewAll.viewAllBlogs.LikeBlog
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogsDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityBlogsDetailsBinding

    var isLiked = false
    var isSaved = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBlogsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cover = intent.getStringExtra("cover")
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val blogDate = intent.getStringExtra("date")
        val userName = intent.getStringExtra("userName")
        val userProfile = intent.getStringExtra("userProfile")
        val blogId = intent.getStringExtra("blogId")

        Glide.with(this).load(cover).into(binding.blogPic)
        binding.blogTitle.text = title
        binding.blogContent.text = content
        if (blogDate != null) {
            binding.date.text= dateFormat(blogDate)
        }
        binding.userName.text = userName

        Glide.with(this).load(userProfile).into(binding.userProfile)

        binding.likePost.setOnClickListener {
            likePost(blogId)
        }

        binding.savePost.setOnClickListener {
            savePosts(blogId)
        }
    }

    private fun savePosts(blogId: String?) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.viewAllApi.saveBlog(user_id, SaveBlog(blogId!!))
        savePost.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (!isSaved) {
                        isSaved = !isSaved
                        binding.likePost.setImageResource(R.drawable.svg_saved_profile)
                    }else {
                        isSaved = !isSaved
                        binding.likePost.setImageResource(R.drawable.svg_save_post)
                    }
                    Toast.makeText(applicationContext, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun likePost(blogId: String?) {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val like = RetrofitBuilder.viewAllApi.likeBlog(blogId!!, LikeBlog(user_id))
        like.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (!isLiked) {
                        isLiked = !isLiked
                        binding.likePost.setImageResource(R.drawable.likes)
                    }else {
                        isLiked = !isLiked
                        binding.likePost.setImageResource(R.drawable.svg_like_post)
                    }
                    Toast.makeText(applicationContext, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
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