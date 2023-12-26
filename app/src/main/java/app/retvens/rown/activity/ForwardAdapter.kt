package app.retvens.rown.activity


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R


class ForwardAdapter(private  var context: Context,private var userList: List<UserListData>,var onUserSelectedListener: OnUserSelectedListener): RecyclerView.Adapter<ForwardAdapter.viewholder>() {


    var isRightVisible: Boolean = false

    private var selectedMessages = ArrayList<UserListData>()
    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val check = itemView.findViewById<CheckBox>(R.id.check)
        val re_lay = itemView.findViewById<RelativeLayout>(R.id.re_lay)
        val re_lay_in = itemView.findViewById<RelativeLayout>(R.id.re_lay_in)
        val right = itemView.findViewById<ImageView>(R.id.right)
        val mes_rv_name = itemView.findViewById<TextView>(R.id.mes_rv_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForwardAdapter.viewholder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.forward_design, parent, false) // Replace with your actual layout resource
        return viewholder(itemView)
    }

    override fun onBindViewHolder(holder: ForwardAdapter.viewholder, position: Int) {

         val currentItem = userList[position]

        holder.mes_rv_name.text=currentItem.text

        if (currentItem.isSelected) {
            holder.right.visibility=View.VISIBLE
//            holder.itemView.setBackgroundResource(R.color.badge_background_color)
        } else {
            holder.right.visibility=View.GONE
//            holder.itemView.setBackgroundResource(android.R.color.transparent)
        }

     holder.re_lay.setOnClickListener {

         var selectedPersonName: String? = null

         Log.d("cvbnm,cvbnm", "onBindViewHolder: ")

            currentItem.isSelected = !currentItem.isSelected
            if (currentItem.isSelected) {
                selectedMessages.add(currentItem)

               selectedPersonName = currentItem.text
            } else {
                selectedMessages.remove(currentItem)
                var selectedPersonName: String? = ""
            }

         val selectedPersonNames: List<String> = selectedMessages.map { it.text }


         if (selectedMessages.size>0) {

                onUserSelectedListener.onItemSelected(true, selectedPersonNames)
            }
            else{

                onUserSelectedListener.onItemSelected(false,emptyList())
            }


            notifyDataSetChanged()

        }

    }

    override fun getItemCount(): Int= userList.size



}