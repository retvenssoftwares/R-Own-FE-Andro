package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.exploreForUsers.blogs.BlogsDetailsActivity
import app.retvens.rown.databinding.PopularBlogsCardHomeBinding
import app.retvens.rown.viewAll.viewAllBlogs.ViewAllBlogsActivity

//import com.karan.multipleviewrecyclerview.RecyclerItem

class BlogsChildAdapter(
    private val context: Context,
    private val viewType: Int,
    private val blogsRecyclerData: List<DataItem.BlogsRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class BlogsViewHolder( private val binding : PopularBlogsCardHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBlogsView(recyclerItem: DataItem.BlogsRecyclerData){
            binding.blogCover.setImageResource(recyclerItem.BlogsCover)
            binding.bloggerProfile.setImageResource(recyclerItem.Profile)
            binding.blogTitle.text = recyclerItem.BlogTitle

            binding.blogCover.setOnClickListener {
                context.startActivity(Intent(context, BlogsDetailsActivity::class.java))
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
}