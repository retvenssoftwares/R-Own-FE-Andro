package app.retvens.rown.NavigationFragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.databinding.HotellAwardsBinding

//import com.karan.multipleviewrecyclerview.RecyclerItem

class AwardsChildAdapter(private val viewType: Int,
                         private val awardsRecyclerData : List<DataItem.AwardsRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HotelAwardsViewHolder(private val binding : HotellAwardsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindHotelAwardsView(recyclerItem: DataItem.AwardsRecyclerData){
            binding.awards.setImageResource(recyclerItem.Cover)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val binding = HotellAwardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HotelAwardsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return awardsRecyclerData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HotelAwardsViewHolder -> {
                holder.bindHotelAwardsView(awardsRecyclerData[position])
            }
        }
    }
}