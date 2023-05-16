package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.databinding.CreateCummunityItemBinding
import app.retvens.rown.viewAll.viewAllCommunities.ClosedCommunityDetailsActivity
import app.retvens.rown.viewAll.viewAllCommunities.OpenCommunityDetailsActivity
//import com.karan.multipleviewrecyclerview.RecyclerItem

class CommunityChildAdapter(private val context: Context, private val viewType: Int,
                            private val communityRecyclerData : List<DataItem.CommunityRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CommunityViewHolder(private val binding : CreateCummunityItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindCommunityView(recyclerItem: DataItem.CommunityRecyclerData){
            binding.cImg.setImageResource(recyclerItem.image)
            binding.title.text = recyclerItem.title
            binding.members.text = recyclerItem.members
            binding.joinG.text = recyclerItem.join

            binding.joinG.setOnClickListener {
                if (recyclerItem.join == "Join"){
                    context.startActivity(Intent(context, OpenCommunityDetailsActivity::class.java))
                } else {
                    context.startActivity(Intent(context, ClosedCommunityDetailsActivity::class.java))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val binding = CreateCummunityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return CommunityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return communityRecyclerData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CommunityViewHolder -> {
                holder.bindCommunityView(communityRecyclerData[position])
            }
        }
    }
}