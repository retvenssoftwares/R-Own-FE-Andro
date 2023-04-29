package app.retvens.rown.Dashboard.profileCompletion.frags.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File

class HotelChainAdapter(private var chainList : List<HotelChainData>, val context: Context) : RecyclerView.Adapter<HotelChainAdapter.HotelChainViewHolder>() {

    lateinit var dialog: Dialog

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedHotelChainImageUri: Uri?= null  // Final Uri for Hotel chain

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraHotelChainImageUri: Uri
    class HotelChainViewHolder(itemView: View) : ViewHolder(itemView){
        val nameET = itemView.findViewById<TextInputEditText>(R.id.chainHotelNameET)
        val location = itemView.findViewById<TextInputEditText>(R.id.chainHotelLocationET)
        val rating = itemView.findViewById<TextInputEditText>(R.id.chainHotelRatingET)

        val nameTIL = itemView.findViewById<TextInputLayout>(R.id.chainHotelNameLayout)
        val locationTIL = itemView.findViewById<TextInputLayout>(R.id.chainHotelLocationLayout)
        val ratingTIL = itemView.findViewById<TextInputLayout>(R.id.chainHotelRatingLayout)
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

    }
}
