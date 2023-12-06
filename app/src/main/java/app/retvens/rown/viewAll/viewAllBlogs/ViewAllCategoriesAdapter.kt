package app.retvens.rown.viewAll.viewAllBlogs

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R
import com.bumptech.glide.Glide

class ViewAllCategoriesAdapter(var listS : ArrayList<ViewAllCategoriesData>, val context: Context) : RecyclerView.Adapter<ViewAllCategoriesAdapter.ViewAllVendorsViewHolder>() {

    class ViewAllVendorsViewHolder(itemView: View) : ViewHolder(itemView){
        val categoryName = itemView.findViewById<TextView>(R.id.personal_notification_name)
        val blogsQuantity = itemView.findViewById<TextView>(R.id.personal_notification_time)
        val profile = itemView.findViewById<ImageView>(R.id.personal_notification_profile)
        val endPic = itemView.findViewById<ImageView>(R.id.personal_notification_end_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllVendorsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_personal_notification, parent, false)
        return ViewAllVendorsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ViewAllVendorsViewHolder, position: Int) {
        holder.endPic.setPadding(40)
        holder.endPic.setImageResource(R.drawable.ic_right_arrow)

        holder.blogsQuantity.text =  "${listS[position].blog_count} blogs"

        Glide.with(context).load(listS[position].Image).into(holder.profile)
        holder.categoryName.text = listS[position].category_name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AllBlogsActivity::class.java)
            intent.putExtra("id", listS[position].category_id)
            intent.putExtra("name", listS[position].category_name)
            context.startActivity(intent)
        }

    }

    fun searchView(searchText : ArrayList<ViewAllCategoriesData>){
        listS = searchText
        notifyDataSetChanged()
    }
    fun removeEmptyCategoryFromList(data: List<ViewAllCategoriesData>){
        try {
            data.forEach {
                if (it.blog_count < 1){
                    listS.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

}