package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.databinding.VendorsCardHomeBinding
import app.retvens.rown.viewAll.vendorsDetails.VendorDetailsActivity
import com.bumptech.glide.Glide

//import com.karan.multipleviewrecyclerview.RecyclerItem

class VendorsChildAdapter(private val context: Context, private val viewType: Int,
                          private val vendorsRecyclerData : List<ProfileServicesDataItem>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class VendorsViewHolder(private val binding : VendorsCardHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindVendorsView(recyclerItem: ProfileServicesDataItem){
            binding.vendorName.text = recyclerItem.vendorName
            binding.vendorsId.text = recyclerItem.service_name
            binding.avgPrice.text = recyclerItem.vendorServicePrice

            Glide.with(context).load(recyclerItem.vendorImage).into(binding.vendorCover)
            Glide.with(context).load(recyclerItem.Profile_pic).into(binding.vendorProfile)
            binding.vendorCover.setOnClickListener {
                val intent = Intent(context, VendorDetailsActivity::class.java)
                intent.putExtra("user_id", recyclerItem.user_id)
                intent.putExtra("vendorImage", recyclerItem.vendorImage)
                intent.putExtra("vendorName", recyclerItem.vendorName)
                context.startActivity(intent)
            }
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