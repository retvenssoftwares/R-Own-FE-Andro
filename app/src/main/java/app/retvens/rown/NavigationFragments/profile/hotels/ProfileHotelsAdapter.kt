package app.retvens.rown.NavigationFragments.profile.hotels

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.exploreForUsers.people.Post
import app.retvens.rown.R
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileHotelsAdapter(val listS : ArrayList<HotelsName>, val context: Context, val isOwner : Boolean) : RecyclerView.Adapter<ProfileHotelsAdapter.ProfileHotelsViewHolder>() {

    class ProfileHotelsViewHolder(itemView: View) : ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.venue_name)
        val locationHotel = itemView.findViewById<TextView>(R.id.location_hotel)

        val cover = itemView.findViewById<ImageView>(R.id.hotel_venue_cover)
        val hotelRating = itemView.findViewById<RatingBar>(R.id.hotelRating)
        val edit = itemView.findViewById<CardView>(R.id.read_more_blog)
        val del = itemView.findViewById<CardView>(R.id.del)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHotelsViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.item_profile_hotels_card, parent, false)
        return ProfileHotelsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listS.size
    }

    override fun onBindViewHolder(holder: ProfileHotelsViewHolder, position: Int) {
        if (!isOwner){
            holder.edit.visibility = View.GONE
            holder.del.visibility = View.GONE
        }
        holder.name.text = listS[position].hotelName
        holder.locationHotel.text = listS[position].hotelAddress

        Glide.with(context).load(listS[position].hotelCoverpicUrl).into(holder.cover)

//        if (listS[position] != null) {
//            holder.hotelRating.rating = listS[position].hotelRating.toFloat()
//        }

        holder.edit.setOnClickListener {
            val intent = Intent(context, EditHotelDetailsActivity::class.java)
            intent.putExtra("name", listS[position].hotelName)
            intent.putExtra("hotelId", listS[position].hotel_id)
            intent.putExtra("location", listS[position].hotelAddress)
            intent.putExtra("hotelDescription",listS[position].Hoteldescription)
            context.startActivity(intent)
        }

        holder.del.setOnClickListener {
            openBottomForDel(listS[position].hotel_id)
        }

        holder.itemView.setOnClickListener {
            if (isOwner) {
                val intent = Intent(context, HotelDetailsProfileActivity::class.java)
                intent.putExtra("name", listS[position].hotelName)
                intent.putExtra("logo", listS[position].hotelCoverpicUrl)
                intent.putExtra("hotelId", listS[position].hotel_id)
                intent.putExtra("hotelAddress", listS[position].hotelAddress)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, HotelDetailsActivity::class.java)
                intent.putExtra("name", listS[position].hotelName)
                intent.putExtra("logo", listS[position].hotelCoverpicUrl)
                intent.putExtra("hotelId", listS[position].hotel_id)
                intent.putExtra("hotelAddress", listS[position].hotelAddress)
                context.startActivity(intent)
            }
        }
    }
    private fun openBottomForDel(Id: String) {
        val dialogLanguage = Dialog(context)
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogLanguage.setContentView(R.layout.bottom_delete_service)
        dialogLanguage.setCancelable(true)

        dialogLanguage.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogLanguage.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLanguage.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogLanguage.window?.setGravity(Gravity.BOTTOM)
        dialogLanguage.show()

        dialogLanguage.findViewById<TextView>(R.id.remove).text = "Do you really want to delete this hotel?"

        dialogLanguage.findViewById<TextView>(R.id.yes).setOnClickListener {
            removeHotel(Id)
            dialogLanguage.dismiss()
        }
        dialogLanguage.findViewById<TextView>(R.id.not).setOnClickListener { dialogLanguage.dismiss() }
    }

    private fun removeHotel(id: String) {
        val respo = RetrofitBuilder.ProfileApis.removeHotel(
            id,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "0")
        )
        respo.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                Toast.makeText(
                    context,
                    response.body()?.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.localizedMessage?.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun removeHotelFromList(data: List<HotelsName>){

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