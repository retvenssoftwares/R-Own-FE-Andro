package app.retvens.rown.NavigationFragments.profile.vendorsReview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R
import app.retvens.rown.utils.dateFormat

class VendorsReviewAdapter(val listS : List<UserReview>, val context: Context) : RecyclerView.Adapter<VendorsReviewAdapter.VendorsReviewViewHolder>() {

    class VendorsReviewViewHolder(itemView: View) : ViewHolder(itemView){
        val ratingNo = itemView.findViewById<TextView>(R.id.ratingNo)
        val review = itemView.findViewById<TextView>(R.id.review)
        val fullName = itemView.findViewById<TextView>(R.id.fullName)
        val date = itemView.findViewById<TextView>(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorsReviewViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_what_people_say, parent, false)
        return VendorsReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: VendorsReviewViewHolder, position: Int) {
        holder.ratingNo.text = listS[position].Rating_number
        holder.review.text = listS[position].Description
        holder.fullName.text = listS[position].Full_name
        holder.date.text = dateFormat(listS[position].date_added)
    }
}