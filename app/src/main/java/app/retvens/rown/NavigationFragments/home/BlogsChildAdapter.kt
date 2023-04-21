package app.retvens.rown.NavigationFragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.databinding.HotellAwardsBinding
import app.retvens.rown.databinding.PopularBlogsCardHomeBinding
import app.retvens.rown.databinding.VendorsCardHomeBinding
import com.karan.multipleviewrecyclerview.DataItem

//import com.karan.multipleviewrecyclerview.RecyclerItem

class BlogsChildAdapter(private val viewType: Int,
                        private val blogsRecyclerData : List<DataItem.BlogsRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class BlogsViewHolder(private val binding : PopularBlogsCardHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBlogsView(recyclerItem: DataItem.BlogsRecyclerData){
            binding.blogCover.setImageResource(recyclerItem.BlogsCover)
            binding.bloggerProfile.setImageResource(recyclerItem.Profile)
            binding.blogTitle.text = recyclerItem.BlogTitle
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