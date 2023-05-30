package app.retvens.rown.viewAll.viewAllBlogs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.DataCollections.saveId.SaveBlog
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.BlogsDetailsActivity
import app.retvens.rown.R
import app.retvens.rown.utils.dateFormat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllBlogsAdapter(var listS : List<AllBlogsData>, val context: Context) : RecyclerView.Adapter<AllBlogsAdapter.ExploreBlogsViewHolder>() {

    class ExploreBlogsViewHolder(itemView: View) : ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.blog_title)
        val date = itemView.findViewById<TextView>(R.id.blog_date)

        val blogCategory = itemView.findViewById<TextView>(R.id.blogCategory)
        val bloggerName = itemView.findViewById<TextView>(R.id.blogger)
        val cover = itemView.findViewById<ImageView>(R.id.blog_cover)
        val profile = itemView.findViewById<ImageView>(R.id.blogger_profile)

        val blogLike = itemView.findViewById<ImageView>(R.id.blogs_card_like)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreBlogsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_blogs_card, parent, false)
        return ExploreBlogsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreBlogsViewHolder, position: Int) {
//        getUserInfo(holder, listS[position].User_id)
        var like = true
        var operatioin = "push"

        holder.blogCategory.text = listS[position].category_name
        holder.title.text = listS[position].blog_title
        if (listS[position].date_added != null) {
            holder.date.text = dateFormat(listS[position].date_added)
        }

        if (listS[position].saved == "saved"){
            operatioin = "pop"
            like = false
            holder.blogLike.setImageResource(R.drawable.svg_heart_liked)
        } else {
            operatioin = "push"
            like = true
            holder.blogLike.setImageResource(R.drawable.svg_heart)
        }

        holder.blogLike.setOnClickListener {
            if (listS[position].blog_id != null) {
                savePosts(listS[position].blog_id, holder, operatioin, like) {
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

        holder.bloggerName.text = listS[position].User_name
        Glide.with(context).load(listS[position].Profile_pic).into(holder.profile)

        Glide.with(context).load(listS[position].blog_image).into(holder.cover)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BlogsDetailsActivity::class.java)
            intent.putExtra("cover", listS[position].blog_image)
            intent.putExtra("title", listS[position].blog_title)
            intent.putExtra("content", listS[position].blog_content)
            intent.putExtra("date", listS[position].date_added)
            intent.putExtra("userName", listS[position].User_name)
            intent.putExtra("userProfile", listS[position].Profile_pic)
            intent.putExtra("blogId", listS[position].blog_id)
            intent.putExtra("saved", listS[position].saved)
            intent.putExtra("like", listS[position].like)
            context.startActivity(intent)
        }
    }
    private fun getUserInfo(holder: ExploreBlogsViewHolder, userId: String) {

        val send = RetrofitBuilder.retrofitBuilder.fetchUser(userId)

        send.enqueue(object : Callback<UserProfileRequestItem?> {
            override fun onResponse(
                call: Call<UserProfileRequestItem?>,
                response: Response<UserProfileRequestItem?>
            ) {
                if (response.isSuccessful){
                    if (response.body() != null) {
                        val response = response.body()!!

                    }
                }else{
                    Toast.makeText(context,response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileRequestItem?>, t: Throwable) {
                Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun searchView(searchText : List<AllBlogsData>){
        listS = searchText
        notifyDataSetChanged()
    }
    private fun savePosts(blogId: String?, holder: ExploreBlogsViewHolder, operation: String, like: Boolean, onLiked : (Int) -> Unit) {
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
                        holder.blogLike.setImageResource(R.drawable.svg_heart_liked)
                        onLiked.invoke(0)
                    } else {
                        holder.blogLike.setImageResource(R.drawable.svg_heart)
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

}