package app.retvens.rown.NavigationFragments.exploreForUsers.events

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.saveId.SaveEvent
import app.retvens.rown.NavigationFragments.eventForUsers.AllEventsAdapter
import app.retvens.rown.NavigationFragments.eventForUsers.onGoingEvents.OnGoingEventsData
import app.retvens.rown.R
import app.retvens.rown.utils.dateFormat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreEventsAdapter(val listS : List<OnGoingEventsData>, val context: Context) : RecyclerView.Adapter<ExploreEventsAdapter.ExploreEventViewHolder>() {

    class ExploreEventViewHolder(itemView: View) : ViewHolder(itemView){
        val event_date = itemView.findViewById<TextView>(R.id.blog_date)
        val title = itemView.findViewById<TextView>(R.id.blog_title)
        val blogType = itemView.findViewById<TextView>(R.id.blogType)
        val cover = itemView.findViewById<ImageView>(R.id.blog_cover)

        val bloggerProfile = itemView.findViewById<ImageView>(R.id.blogger_profile)
        val bloggerNamw = itemView.findViewById<TextView>(R.id.blogger)


        val saveEvent = itemView.findViewById<ImageView>(R.id.blogs_card_like)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreEventViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_events_card, parent, false)
        return ExploreEventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreEventViewHolder, position: Int) {

        var like = true
        var operatioin = "push"

        holder.title.text = listS[position].event_title

        if (listS[position].date_added != null) {
            holder.event_date.text = dateFormat(listS[position].date_added)
        }

        if (listS[position].saved == "saved"){
            operatioin = "pop"
            like = false
            holder.saveEvent.setImageResource(R.drawable.svg_heart_liked)
        } else {
            operatioin = "push"
            like = true
            holder.saveEvent.setImageResource(R.drawable.svg_heart)
        }

        Glide.with(context).load(listS[position].Profile_pic).into(holder.bloggerProfile)
        holder.bloggerNamw.text = listS[position].User_name

        Glide.with(context).load(listS[position].event_thumbnail).into(holder.cover)

        holder.blogType.text = listS[position].event_category

        holder.saveEvent.setOnClickListener {
            saveEvent(listS[position].event_id, holder, operatioin, like){
                if (it == 0){
                    operatioin="pop"
                    like = !like
                } else {
                    operatioin = "push"
                    like = !like
                }
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EventDetailsActivity::class.java)
            intent.putExtra("title", listS[position].event_title)
            intent.putExtra("about", listS[position].event_description)
            intent.putExtra("price", listS[position].price)
            intent.putExtra("userId", listS[position].user_id)
            intent.putExtra("User_name", listS[position].User_name)
            intent.putExtra("Profile_pic", listS[position].Profile_pic)
            intent.putExtra("eventId", listS[position].event_id)
            intent.putExtra("cover", listS[position].event_thumbnail)
            intent.putExtra("location", listS[position].location)
            intent.putExtra("saved", listS[position].saved)
            context.startActivity(intent)
        }
    }

    private fun saveEvent(eventId: String?, holder: ExploreEventsAdapter.ExploreEventViewHolder, operation: String, like: Boolean, onLiked : (Int) -> Unit) {

        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

//        Toast.makeText(context, "$eventId   $user_id", Toast.LENGTH_SHORT).show()

        val saveEvent = RetrofitBuilder.EventsApi.saveEvent(user_id, SaveEvent(operation,eventId!!))
        saveEvent.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    if (like){
                        holder.saveEvent.setImageResource(R.drawable.svg_heart_liked)
                        onLiked.invoke(0)
                    } else {
                        holder.saveEvent.setImageResource(R.drawable.svg_heart)
                        onLiked.invoke(1)
                    }
                    Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

}