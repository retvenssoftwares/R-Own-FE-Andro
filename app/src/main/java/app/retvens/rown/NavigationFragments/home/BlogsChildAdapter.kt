package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SaveBlog
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.BlogsDetailsActivity
import app.retvens.rown.R
import app.retvens.rown.databinding.PopularBlogsCardHomeBinding
import app.retvens.rown.utils.dateFormat
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import com.karan.multipleviewrecyclerview.RecyclerItem

class BlogsChildAdapter(
    private val context: Context,
    private val viewType: Int,
    private val blogsRecyclerData: ArrayList<AllBlogsData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class BlogsViewHolder( private val binding : PopularBlogsCardHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBlogsView(recyclerItem: AllBlogsData){
            Glide.with(context).load(recyclerItem.blog_image).into(binding.blogCover)
            Glide.with(context).load(recyclerItem.Profile_pic).into(binding.bloggerProfile)
            binding.blogTitle.text = recyclerItem.blog_title

            var like = true
            var operatioin = "push"

            binding.blogCategory.text = recyclerItem.category_name
            if( recyclerItem.date_added != null) {
                binding.date.text = dateFormat(recyclerItem.date_added)
            } else{
                binding.date.text = "fetching"
            }

            if (recyclerItem.saved == "saved"){
                operatioin = "pop"
                like = false
                binding.blogsCardLike.setImageResource(R.drawable.svg_heart_liked)
            } else {
                operatioin = "push"
                like = true
                binding.blogsCardLike.setImageResource(R.drawable.svg_heart)
            }

            binding.blogsCardLike.setOnClickListener {
                if (recyclerItem.blog_id != null) {
                    savePosts(recyclerItem.blog_id, binding, operatioin, like) {
                        if (it == 0) {
                            operatioin = "pop"
                            like = !like
                        } else {
                            operatioin = "push"
                            like = !like
                        }
                    }
                }
            }

            binding.blogger.text = recyclerItem.User_name

            binding.readMoreBlog.setOnClickListener {
                val intent = Intent(context, BlogsDetailsActivity::class.java)
                intent.putExtra("cover", recyclerItem.blog_image)
                intent.putExtra("title", recyclerItem.blog_title)
                intent.putExtra("content", recyclerItem.blog_content)
                intent.putExtra("date", recyclerItem.date_added)
                intent.putExtra("userName", recyclerItem.User_name)
                intent.putExtra("userProfile", recyclerItem.Profile_pic)
                intent.putExtra("blogId", recyclerItem.blog_id)
                intent.putExtra("saved", recyclerItem.saved)
                intent.putExtra("like", recyclerItem.like)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val binding = PopularBlogsCardHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return BlogsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return blogsRecyclerData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is BlogsViewHolder -> {
                holder.bindBlogsView(blogsRecyclerData[position])
            }
        }
    }

    private fun savePosts(blogId: String?, holder: PopularBlogsCardHomeBinding, operation: String, like: Boolean, onLiked: (Int) -> Unit) {
        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val savePost = RetrofitBuilder.viewAllApi.saveBlog(user_id, SaveBlog(operation,blogId!!))
        savePost.enqueue(object : Callback<UserProfileResponse?> {
            override fun onResponse(
                call: Call<UserProfileResponse?>,
                response: Response<UserProfileResponse?>
            ) {
                if (response.isSuccessful){
                    if (like){
                        holder.blogsCardLike.setImageResource(R.drawable.svg_heart_liked)
                        onLiked.invoke(0)
                    } else {
                        holder.blogsCardLike.setImageResource(R.drawable.svg_heart)
                        onLiked.invoke(1)
                    }
                    Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun removeBlogsFromList(data: List<AllBlogsData>){

        try {
            data.forEach {
                if (it.display_status == "0"){
                    blogsRecyclerData.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

}