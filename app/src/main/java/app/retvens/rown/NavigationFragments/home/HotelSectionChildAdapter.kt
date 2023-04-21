package app.retvens.rown.NavigationFragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.databinding.HotelSectionItemBinding
import app.retvens.rown.databinding.HotellAwardsBinding
import app.retvens.rown.databinding.PopularBlogsCardHomeBinding
import app.retvens.rown.databinding.VendorsCardHomeBinding
import com.karan.multipleviewrecyclerview.DataItem
import com.karan.multipleviewrecyclerview.DataItemType
//import com.karan.multipleviewrecyclerview.RecyclerItem

class HotelSectionChildAdapter(private val viewType: Int,
    private val hotelSectionRecyclerData : List<DataItem.HotelSectionRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HotelSectionViewHolder(private val binding : HotelSectionItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindHotelSectionView(recyclerItem: DataItem.HotelSectionRecyclerData){
            binding.hotelSectionCover.setImageResource(recyclerItem.Cover)
            binding.hotelSectionName.text= recyclerItem.Title
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val binding = HotelSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HotelSectionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hotelSectionRecyclerData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HotelSectionViewHolder -> {
                holder.bindHotelSectionView(hotelSectionRecyclerData[position])
            }
        }
    }
}