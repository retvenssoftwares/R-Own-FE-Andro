package app.retvens.rown.NavigationFragments.exploreForUsers.hotels

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

class ExploreHotelsAdapter(val listS : List<HotelData>, val context: Context) : RecyclerView.Adapter<ExploreHotelsAdapter.ExploreHotelsViewHolder>() {

    class ExploreHotelsViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.venue_name)
        val location = itemView.findViewById<TextView>(R.id.location_hotel)

        val rating = itemView.findViewById<RatingBar>(R.id.ratingBar)

        val cover = itemView.findViewById<ImageView>(R.id.hotel_venue_cover)
        val like = itemView.findViewById<ImageView>(R.id.card_like)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreHotelsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_explore_hotels_card, parent, false)
        return ExploreHotelsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreHotelsViewHolder, position: Int) {

        var like = true
        var operatioin = "push"

        holder.name.text = listS[position].hotelName
        holder.location.text = listS[position].location

        Glide.with(context).load(listS[position].hotelCoverpicUrl).into(holder.cover)

        if (listS[position].saved != "no"){
            operatioin = "pop"
            like = false
            holder.like.setImageResource(R.drawable.svg_heart_liked)
        } else {
            operatioin = "push"
            like = true
            holder.like.setImageResource(R.drawable.svg_heart)
        }

        holder.like.setOnClickListener {
            saveHotel(listS[position].hotel_id, holder, operatioin, like){
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
            val intent = Intent(context, HotelDetailsActivity::class.java)
            intent.putExtra("name", listS[position].hotelName)
            intent.putExtra("logo", listS[position].hotelCoverpicUrl)
            intent.putExtra("hotelId", listS[position].hotel_id)
            intent.putExtra("hotelAddress", listS[position].location)
            intent.putExtra("saved", listS[position].saved)
            context.startActivity(intent)
        }
    }

    private fun saveHotel(eventId: String?, holder: ExploreHotelsAdapter.ExploreHotelsViewHolder, operation: String, like: Boolean, onLiked : (Int) -> Unit) {

        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

//        Toast.makeText(context, "$eventId   $user_id", Toast.LENGTH_SHORT).show()

        val saveEvent = RetrofitBuilder.exploreApis.saveHotel(user_id, SaveHotel(operation,eventId!!))
        saveEvent.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    if (like){
                        holder.like.setImageResource(R.drawable.svg_heart_liked)
                        onLiked.invoke(0)
                    } else {
                        holder.like.setImageResource(R.drawable.svg_heart)
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