package app.retvens.rown.NavigationFragments.profile.vendorsReview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R
import app.retvens.rown.utils.dateFormat
import com.bumptech.glide.Glide

class QuickReviewAdapter(val listS : List<VendorReviewsData>, val context: Context) : RecyclerView.Adapter<QuickReviewAdapter.VendorsReviewViewHolder>() {

    class VendorsReviewViewHolder(itemView: View) : ViewHolder(itemView){
        val reviewCount = itemView.findViewById<TextView>(R.id.reviewCount)
        val review = itemView.findViewById<TextView>(R.id.review)
        val reviewImage = itemView.findViewById<ImageView>(R.id.reviewImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorsReviewViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_top_reviews, parent, false)
        return VendorsReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: VendorsReviewViewHolder, position: Int) {
        holder.reviewCount.text = listS[position].review_count.count
        holder.review.text = listS[position].reviews_name
        Glide.with(context).load(listS[position].quickReviewImage).into(holder.reviewImage)
    }
}