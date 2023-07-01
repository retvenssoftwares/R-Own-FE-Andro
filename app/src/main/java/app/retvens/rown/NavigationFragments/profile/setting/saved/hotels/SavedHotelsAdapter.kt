package app.retvens.rown.NavigationFragments.profile.setting.saved.hotels

import android.content.Context
import android.content.Intent
import android.util.Log
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
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.SaveHotel
import app.retvens.rown.R
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedHotelsAdapter(val listS : ArrayList<Hotel>, val context: Context) : RecyclerView.Adapter<SavedHotelsAdapter.ExploreHotelsViewHolder>() {

    class ExploreHotelsViewHolder(itemView: View) : ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.venue_name)
        val location = itemView.findViewById<TextView>(R.id.location_hotel)
        val hotelStar = itemView.findViewById<TextView>(R.id.hotelStar)
        val cover = itemView.findViewById<ImageView>(R.id.hotel_venue_cover)
        val like = itemView.findViewById<ImageView>(R.id.card_like)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreHotelsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_explore_hotels_card, parent, false)
        return ExploreHotelsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ExploreHotelsViewHolder, position: Int) {

        var like = true
        var operatioin = "push"

        holder.name.text = listS[position].hotelName

        val nameParts = listS[position].hotelAddress.split(",")
        val firstN = nameParts.getOrElse(0) { "" }

        holder.like.setImageResource(R.drawable.svg_heart_liked)

        holder.location.text = firstN
        holder.hotelStar.text = listS[position].hotelRating.toString() + " Hotel"

        Glide.with(context).load(listS[position].hotelCoverpicUrl).into(holder.cover)

        holder.like.setOnClickListener {
            saveHotel(listS[position].hotelid, holder, "pop", like) {
                try {
                    listS.remove(listS[position])
                    notifyDataSetChanged()
                }catch (e : Exception){}
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, HotelDetailsActivity::class.java)
            intent.putExtra("name", listS[position].hotelName)
            intent.putExtra("logo", listS[position].hotelCoverpicUrl)
            intent.putExtra("hotelId", listS[position].hotelid)
            intent.putExtra("hotelAddress", listS[position].hotelAddress)
            intent.putExtra("saved", "saved")
            context.startActivity(intent)
        }
    }

    private fun saveHotel(eventId: String?, holder: ExploreHotelsViewHolder, operation: String, like: Boolean, onLiked : (Int) -> Unit) {

        val sharedPreferences = context.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val saveEvent = RetrofitBuilder.exploreApis.saveHotel(user_id, SaveHotel(operation,eventId!!))
        saveEvent.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    holder.like.setImageResource(R.drawable.svg_heart)
                    onLiked.invoke(1)
//                    Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun removeHotelFromList(data: List<Hotel>){
        try {
            data.forEach {
                if (it.display_status == "0"){
                    listS.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }
}