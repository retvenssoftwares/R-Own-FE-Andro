package app.retvens.rown.CreateCommunity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R
import java.util.ArrayList

class UploadIconAdapter(private val dataList: ArrayList<String>?) : RecyclerView.Adapter<UploadIconAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.membersName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listgrpmembers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList?.get(position)
        holder.name.text = data
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }
}