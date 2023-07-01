package app.retvens.rown.viewAll.vendorsDetails

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.onboarding.ContactResponse
import app.retvens.rown.DataCollections.onboarding.GetInterests
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorServicesAdapter(val context: Context, var interestList : List<ProfileServicesDataItem>) : RecyclerView.Adapter<VendorServicesAdapter.InterestViewHolder>() {

    var addedItems : MutableList<GetInterests> ? = mutableListOf()

    interface onItemClickListener{
        fun onItemClickInterest(addedItems : MutableList<GetInterests>)
    }
    private var mListener: onItemClickListener? = null
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.interest_grid_item,parent,false)
        return InterestViewHolder(view)
    }

    override fun getItemCount(): Int {
        return interestList.size
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val currentItem = interestList[position]
        holder.name.text = currentItem.service_name
    }

    class InterestViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.interest_name)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.interest_item_layout)
    }

    fun search(searchText : List<ProfileServicesDataItem>){
        interestList = searchText
        notifyDataSetChanged()
    }

}