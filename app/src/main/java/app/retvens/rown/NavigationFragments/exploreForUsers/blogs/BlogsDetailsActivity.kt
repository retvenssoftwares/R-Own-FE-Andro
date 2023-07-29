package app.retvens.rown.NavigationFragments.exploreForUsers.blogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SaveBlog
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetBlogComment
import app.retvens.rown.bottomsheet.BottomSheetLocation
import app.retvens.rown.databinding.ActivityBlogsDetailsBinding
import app.retvens.rown.utils.dateFormat
import app.retvens.rown.viewAll.viewAllBlogs.GetBlogById
import app.retvens.rown.viewAll.viewAllBlogs.GetBlogByIdItem
import app.retvens.rown.viewAll.viewAllBlogs.LikeBlog
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException

class BlogsDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityBlogsDetailsBinding

    var isLiked = true
    var isSaved = true

    var operatioin = "push"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBlogsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { onBackPressed() }

        val cover = intent.getStringExtra("cover")
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val blogDate = intent.getStringExtra("date")
        val userName = intent.getStringExtra("userName")
        val userProfile = intent.getStringExtra("userProfile")
        val blogId = intent.getStringExtra("blogId").toString()

        val saved = intent.getStringExtra("saved")
        val like = intent.getStringExtra("like")

//        Toast.makeText(applicationContext, like.toString(), Toast.LENGTH_SHORT).show()

        if (saved == "saved"){
            operatioin = "pop"
            isSaved = false
            binding.savePost.setImageResource(R.drawable.svg_saved)
        } else {
            operatioin = "push"
            isSaved = true
            binding.savePost.setImageResource(R.drawable.svg_save_post)
        }
        if (like == "liked"){
            isLiked = false
            binding.likePost.setImageResource(R.drawable.liked_vectore)
        } else {
            isLiked = true
            binding.likePost.setImageResource(R.drawable.svg_like_post)
        }
        getBlog(blogId)

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
//        Toast.makeText(applicationContext, blogId.toString(), Toast.LENGTH_SHORT).show()
        binding.comment.setOnClickListener {
            val bottomSheet = BottomSheetBlogComment(blogId!!,cover!!)
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
        }
        binding.savePost.setOnClickListener {
            savePosts(blogId)
        }

    }

    private fun getBlog(blogId: String?) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        Log.e("blogId",blogId!!)

        val blog = RetrofitBuilder.viewAllApi.getBlogsByBlogId(blogId!!, user_id)
        blog.enqueue(object : Callback<List<GetBlogByIdItem>?> {
            override fun onResponse(
                call: Call<List<GetBlogByIdItem>?>,
                response: Response<List<GetBlogByIdItem>?>
            ) {
                Log.d("res", response.body().toString())
                if (response.isSuccessful){
                    val res = response.body()!!

//                    Toast.makeText(applicationContext, res.get(0).saved, Toast.LENGTH_SHORT).show()
//                    if(res.get(0).saved == "saved"){
//                        operatioin = "pop"
//                        isSaved = false
//                        binding.savePost.setImageResource(R.drawable.svg_saved)
//                    } else {
//                        operatioin = "push"
//                        isSaved = true
//                        binding.savePost.setImageResource(R.drawable.svg_save_post)
//                    }
                    
                    if(res.get(0).like == "liked"){
                        isLiked = false
                        binding.likePost.setImageResource(R.drawable.liked_vectore)
                    }
                    try {
                        binding.userName.text = res.get(0).User_name
                        binding.blogTitle.text = res.get(0).blog_title
                        binding.blogContent.text = res.get(0).blog_content
                        Glide.with(applicationContext).load(res.get(0).blog_image).into(binding.blogPic)
                        Glide.with(applicationContext).load(res.get(0).Profile_pic).into(binding.userProfile)


                    } catch (e:IndexOutOfBoundsException){Log.d("e", e.toString())}
                }
            }

            override fun onFailure(call: Call<List<GetBlogByIdItem>?>, t: Throwable) {

            }
        })

    }

    private fun savePosts(blogId: String?) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.viewAllApi.saveBlog(user_id, SaveBlog(operatioin,blogId!!))
        savePost.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (isSaved) {
                        isSaved = !isSaved
                        binding.savePost.setImageResource(R.drawable.svg_saved)
                    }else {
                        isSaved = !isSaved
                        binding.savePost.setImageResource(R.drawable.svg_save_post)
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
                    if (isLiked) {
                        isLiked = !isLiked
                        binding.likePost.setImageResource(R.drawable.liked_vectore)
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