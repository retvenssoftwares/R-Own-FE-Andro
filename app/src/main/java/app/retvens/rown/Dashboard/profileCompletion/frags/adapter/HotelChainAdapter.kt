package app.retvens.rown.Dashboard.profileCompletion.frags.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HotelChainAdapter(private var chainList: List<HotelChainData>, val context: Context, val coverUri: Uri?) : RecyclerView.Adapter<HotelChainAdapter.HotelChainViewHolder>() {

//    lateinit var dialog: Dialog
//
//    var PICK_IMAGE_REQUEST_CODE : Int = 0
//    //Cropped image uri
//    private var croppedHotelChainImageUri: Uri?= null  // Final Uri for Hotel chain
//
//    var REQUEST_CAMERA_PERMISSION : Int = 0
//    lateinit var cameraHotelChainImageUri : Uri

    var mListener: onCoverImageClickListener ? = null
    fun setOnCoverClickListener(listener: onCoverImageClickListener?){
        mListener = listener
    }

    interface onCoverImageClickListener{
        fun onCoverImageClick(userType : String)
    }
    class HotelChainViewHolder(itemView: View) : ViewHolder(itemView){
        val nameET = itemView.findViewById<TextInputEditText>(R.id.chainHotelNameET)
        val location = itemView.findViewById<TextInputEditText>(R.id.chainHotelLocationET)
        val rating = itemView.findViewById<TextInputEditText>(R.id.chainHotelRatingET)

        val nameTIL = itemView.findViewById<TextInputLayout>(R.id.chainHotelNameLayout)
        val locationTIL = itemView.findViewById<TextInputLayout>(R.id.chainHotelLocationLayout)
        val ratingTIL = itemView.findViewById<TextInputLayout>(R.id.chainHotelRatingLayout)

        val chainCover = itemView.findViewById<ImageView>(R.id.hotel_chain_cover_1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelChainViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.chain_hotels_item, parent, false)
        return HotelChainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chainList.size
    }

    override fun onBindViewHolder(holder: HotelChainViewHolder, position: Int) {
        val currentItem = chainList[position]
        holder.nameTIL.setHint(currentItem.hotelNameHint)
        holder.locationTIL.setHint(currentItem.locationHint)
        holder.ratingTIL.setHint(currentItem.hotelStarHint)
        holder.chainCover.setImageURI(coverUri)

        holder.chainCover.setOnClickListener {
            mListener?.onCoverImageClick("ChainAdapter")
        }

        holder.rating.setOnClickListener {

        }
    }
}
