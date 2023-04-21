package app.retvens.rown.NavigationFragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.databinding.CreateCummunityItemBinding
import app.retvens.rown.databinding.HotellAwardsBinding
import app.retvens.rown.databinding.VendorsCardHomeBinding
import com.karan.multipleviewrecyclerview.DataItem

//import com.karan.multipleviewrecyclerview.RecyclerItem

class CommunityChildAdapter(private val viewType: Int,
                            private val communityRecyclerData : List<DataItem.CreateCommunityRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CommunityViewHolder(private val binding : CreateCummunityItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindCommunityView(recyclerItem: DataItem.CreateCommunityRecyclerData){
            binding.cImg.setImageResource(recyclerItem.image)
            binding.title.text = recyclerItem.title
            binding.members.text = recyclerItem.members
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