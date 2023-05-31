package app.retvens.rown.sideNavigation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.saveId.SaveEvent
import app.retvens.rown.NavigationFragments.exploreForUsers.events.ExploreEventsAdapter
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.hotels.HotelDetailsProfileActivity
import app.retvens.rown.R
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqAdapter(val listS : List<faqData>, val context: Context) : RecyclerView.Adapter<FaqAdapter.ExploreHotelsViewHolder>() {

    class ExploreHotelsViewHolder(itemView: View) : ViewHolder(itemView){
        val ques = itemView.findViewById<TextView>(R.id.ques)
        val ans = itemView.findViewById<TextView>(R.id.answer)

        val card_q = itemView.findViewById<CardView>(R.id.card_question)
        val card_a = itemView.findViewById<CardView>(R.id.card_answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreHotelsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_faq, parent, false)
        return ExploreHotelsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreHotelsViewHolder, position: Int) {

        var isVisibleC = false


        holder.ques.text = listS[position].question
        holder.ans.text = listS[position].answer

        holder.card_q.setOnClickListener {
                if (!isVisibleC) {
                    holder.card_a.visibility = View.VISIBLE
                    isVisibleC = true
                } else {
                    holder.card_a.visibility = View.GONE
                    isVisibleC = false
                }
        }
    }

}