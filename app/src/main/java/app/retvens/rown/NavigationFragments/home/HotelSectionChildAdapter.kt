package app.retvens.rown.NavigationFragments.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.HotelDetailsActivity
import app.retvens.rown.NavigationFragments.exploreForUsers.hotels.SaveHotel
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.R
import app.retvens.rown.databinding.HotelSectionItemBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import com.karan.multipleviewrecyclerview.RecyclerItem

class HotelSectionChildAdapter(val context: Context, private val viewType: Int,
                               private val hotelSectionRecyclerData : ArrayList<HotelData>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HotelSectionViewHolder(private val binding : HotelSectionItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindHotelSectionView(recyclerItem: HotelData){

            var like = true
            var operatioin = "push"

            Log.e("totalHotel",hotelSectionRecyclerData.toString())

            Glide.with(context).load(recyclerItem.hotelCoverpicUrl).into(binding.hotelSectionCover)

            binding.hotelSectionName.text= recyclerItem.hotelName
            val nameParts = recyclerItem.hotelAddress.split(",")
            val firstN = nameParts.getOrElse(0) { "" }

            binding.hotelSectionLocation.text= firstN

            if (recyclerItem.saved != "no"){
                operatioin = "pop"
                like = false
                binding.cardLike.setImageResource(R.drawable.svg_heart_liked)
            } else {
                operatioin = "push"
                like = true
                binding.cardLike.setImageResource(R.drawable.svg_heart)
            }

            binding.cardLike.setOnClickListener {
                saveHotel(recyclerItem.hotel_id, binding, operatioin, like){
                    if (it == 0){
                        operatioin="pop"
                        like = !like
                    } else {
                        operatioin = "push"
                        like = !like
                    }
                }
            }

            binding.hotelSectionCover.setOnClickListener {
                val intent = Intent(context, HotelDetailsActivity::class.java)
                intent.putExtra("name", recyclerItem.hotelName)
                intent.putExtra("logo", recyclerItem.hotelCoverpicUrl)
                intent.putExtra("hotelId", recyclerItem.hotel_id)
                intent.putExtra("hotelAddress", recyclerItem.hotelAddress)
                intent.putExtra("saved", recyclerItem.saved)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val binding = HotelSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HotelSectionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hotelSectionRecyclerData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HotelSectionViewHolder -> {
                holder.bindHotelSectionView(hotelSectionRecyclerData[position])
            }
        }
    }

    private fun saveHotel(eventId: String?, holder: HotelSectionItemBinding, operation: String, like: Boolean, onLiked: (Int) -> Unit) {

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
                        holder.cardLike.setImageResource(R.drawable.svg_heart_liked)
                        onLiked.invoke(0)
                    } else {
                        holder.cardLike.setImageResource(R.drawable.svg_heart)
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

    fun removeHotelFromList(data: List<HotelData>){

        try {
            data.forEach {
                if (it.display_status == "0"){
                    hotelSectionRecyclerData.remove(it)
                }
            }
        } catch (e : ConcurrentModificationException){
            Log.d("EPA", e.toString())
        }
    }

}