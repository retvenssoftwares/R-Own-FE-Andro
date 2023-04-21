package app.retvens.rown.NavigationFragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.databinding.HotellAwardsBinding
import app.retvens.rown.databinding.VendorsCardHomeBinding
import com.karan.multipleviewrecyclerview.DataItem

//import com.karan.multipleviewrecyclerview.RecyclerItem

class VendorsChildAdapter(private val viewType: Int,
                          private val vendorsRecyclerData : List<DataItem.VendorsRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class VendorsViewHolder(private val binding : VendorsCardHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindVendorsView(recyclerItem: DataItem.VendorsRecyclerData){
            binding.vendorCover.setImageResource(recyclerItem.VendorCover)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val binding = VendorsCardHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return VendorsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vendorsRecyclerData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is VendorsViewHolder -> {
                holder.bindVendorsView(vendorsRecyclerData[position])
            }
        }
    }
}