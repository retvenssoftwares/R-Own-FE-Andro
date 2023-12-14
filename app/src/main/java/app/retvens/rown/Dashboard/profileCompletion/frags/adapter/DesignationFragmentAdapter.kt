package app.retvens.rown.Dashboard.profileCompletion.frags.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.R
import retrofit2.Callback

class DesignationFragmentAdapter(val context: Context, private var designationList:List<GetJobDataClass>): RecyclerView.Adapter<DesignationFragmentAdapter.ViewHolderClass>() {

    private var designationClickListener: OnDesignationClickListener? = null

    interface OnDesignationClickListener {
        fun onDesignationClick(designation: String)
    }

    fun setOnJobDesignationClickListener(listener: DesignationFragmentAdapter.OnDesignationClickListener) {
        this.designationClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignationFragmentAdapter.ViewHolderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.designation,parent,false)
        return ViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: DesignationFragmentAdapter.ViewHolderClass, position: Int) {
        val data = designationList[position]
        holder.designation.text = data.designation_name
        holder.itemView.setOnClickListener {
            designationClickListener?.onDesignationClick(data.designation_name)
        }
    }

    override fun getItemCount(): Int {
        return designationList.size
    }

    class ViewHolderClass(itemview: View): RecyclerView.ViewHolder(itemview){

        val designation: TextView = itemview.findViewById<TextView>(R.id.designation)


    }

    fun searchDesignation(searchText : List<GetJobDataClass>){
        designationList = searchText
        notifyDataSetChanged()
    }


}