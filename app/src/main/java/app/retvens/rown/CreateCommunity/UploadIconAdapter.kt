package app.retvens.rown.CreateCommunity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R
import okhttp3.internal.immutableListOf
import java.util.ArrayList

class UploadIconAdapter(val context: Context,private val dataList: ArrayList<String>?) : RecyclerView.Adapter<UploadIconAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.membersName)
        val image = itemView.findViewById<ImageView>(R.id.check)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listgrpmembers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList?.get(position)
        holder.name.text = data.toString()
        holder.image.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }
}