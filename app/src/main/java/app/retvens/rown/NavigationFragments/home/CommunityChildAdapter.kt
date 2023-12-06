package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.databinding.CreateCummunityItemBinding
import app.retvens.rown.viewAll.viewAllCommunities.ClosedCommunityDetailsActivity
import app.retvens.rown.viewAll.viewAllCommunities.MembersCommunityDetailsActivity
import app.retvens.rown.viewAll.viewAllCommunities.OpenCommunityDetailsActivity
import com.bumptech.glide.Glide

//import com.karan.multipleviewrecyclerview.RecyclerItem

class CommunityChildAdapter(private val context: Context, private val viewType: Int,
                            private val communityRecyclerData : ArrayList<DataItem.CommunityRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CommunityViewHolder(private val binding : CreateCummunityItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindCommunityView(recyclerItem: DataItem.CommunityRecyclerData){
            Glide.with(context).load(recyclerItem.Profile_pic).into(binding.cImg)
            binding.title.text = recyclerItem.group_name
            binding.members.text = "${recyclerItem.Members.size.toString()}Members"
            binding.joinG.text = "Join"

            Log.e("totalcomm",communityRecyclerData.toString())

//            binding.joinG.setOnClickListener {
//                if (recyclerItem.join == "Join"){
//                    context.startActivity(Intent(context, OpenCommunityDetailsActivity::class.java))
//                } else {
//                    context.startActivity(Intent(context, ClosedCommunityDetailsActivity::class.java))
//                }
//            }
            binding.joinG.setOnClickListener {
                val intent = Intent(context,OpenCommunityDetailsActivity::class.java)
                intent.putExtra("groupId",recyclerItem.group_id.toLong())
                context.startActivity(intent)
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

    fun removeCommunityFromList(data: List<DataItem.CommunityRecyclerData>){
        try {
            data.forEach {
                if (it.display_status == "0"){
                    communityRecyclerData.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

}