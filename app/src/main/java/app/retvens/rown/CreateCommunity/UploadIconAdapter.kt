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
import com.bumptech.glide.Glide
import okhttp3.internal.immutableListOf
import java.util.ArrayList

class UploadIconAdapter(val context: Context,private val number: ArrayList<String>?,private val name: ArrayList<String>?,private val profile: ArrayList<String>?) : RecyclerView.Adapter<UploadIconAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.membersName)
        val image = itemView.findViewById<ImageView>(R.id.memberspic)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listgrpmembers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = name?.get(position)
        holder.name.text = data.toString()
        holder.image.visibility = View.VISIBLE

        val pic = profile?.get(position)

        if (pic!!.isNotEmpty()) {
            Glide.with(context).load(pic).into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.svg_user)
        }

    }

    override fun getItemCount(): Int {
        return name!!.size
    }
}