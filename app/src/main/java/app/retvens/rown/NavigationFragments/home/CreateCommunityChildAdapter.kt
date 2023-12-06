package app.retvens.rown.NavigationFragments.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.databinding.CreateCummunityItemBinding

//import com.karan.multipleviewrecyclerview.RecyclerItem

class CreateCommunityChildAdapter(private val viewType: Int,
                                  private val createCommunityRecyclerData : ArrayList<DataItem.CreateCommunityRecyclerData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CreateCommunityViewHolder(private val binding : CreateCummunityItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindCreateCommunityView(recyclerItem: DataItem.CreateCommunityRecyclerData){
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
                return CreateCommunityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return createCommunityRecyclerData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CreateCommunityViewHolder -> {
                holder.bindCreateCommunityView(createCommunityRecyclerData[position])
            }
        }
    }

    fun removeCommunityFromList(data: List<DataItem.CreateCommunityRecyclerData>){
        try {
            data.forEach {
                if (it.display_status == "0"){
                    createCommunityRecyclerData.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

}