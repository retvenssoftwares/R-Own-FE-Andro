package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HotelOwnerFragment : Fragment(), BackHandler {

    lateinit var hotelType : TextInputEditText
    lateinit var noOfHotels : TextInputEditText
    lateinit var hotelOwnerStarET : TextInputEditText
    lateinit var singleHotelLayout : ConstraintLayout
    lateinit var chainHotelLayout : ConstraintLayout

    lateinit var cameraHotelChain : ImageView
    lateinit var hotelChainProfile : ImageView

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        cropImage(cameraImageUri)
    }
    lateinit var dialog: Dialog

    private var nextScreen : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_owner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraImageUri = createImageUri()!!

        singleHotelLayout = view.findViewById(R.id.cons_single_hotel)
        chainHotelLayout = view.findViewById(R.id.cons_chain_hotel)

        noOfHotels = view.findViewById<TextInputEditText>(R.id.noOfHotels)
        hotelType = view.findViewById<TextInputEditText>(R.id.hotel_type_et)
        hotelOwnerStarET = view.findViewById<TextInputEditText>(R.id.hotel_owner_star_et)

        hotelChainProfile = view.findViewById(R.id.hotel_chain_profile)
        cameraHotelChain = view.findViewById(R.id.camera_hotelChain)
        cameraHotelChain.setOnClickListener {
            openBottomCameraSheet()
        }

        hotelOwnerStarET.setOnClickListener {
            openBottomStarSelection()
        }
            hotelType.setOnClickListener {
            openHotelTypeBottom()
            }

        view.findViewById<CardView>(R.id.card_owner_next).setOnClickListener {
            if (nextScreen == 1){

                val fragment = HotelOwnerChainFragment()
                val hotels = noOfHotels.text.toString()
                if (hotels.isEmpty()){
                    Toast.makeText(requireContext(), "input hotels", Toast.LENGTH_SHORT).show()
                } else{

                    val bundle = Bundle()
                    bundle.putString("hotels", hotels)

                    fragment.arguments = bundle

                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragment_username,fragment)
                    transaction?.commit()

                }
            }
        }

    }

    private fun openBottomCameraSheet() {

        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_camera)

        dialog.findViewById<ImageView>(R.id.delete_img).setOnClickListener {
            deleteImage()
        }

        dialog.findViewById<LinearLayout>(R.id.pick_from_gallery).setOnClickListener {
            openGallery()
        }
        dialog.findViewById<LinearLayout>(R.id.pick_from_camera).setOnClickListener {
            openCamera()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun deleteImage() {
        croppedImageUri = null
        hotelChainProfile.setImageURI(croppedImageUri)
        dialog.dismiss()
    }

    private fun openCamera() {
        contract.launch(cameraImageUri)
        dialog.dismiss()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
        dialog.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {

                cropImage(imageUri)

            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = resultingImage.uri

                compressImage(croppedImage)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context,"Try Again : ${resultingImage.error}",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun cropImage(imageUri: Uri) {
        val options = CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)

        options.setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setOutputCompressQuality(20)
            .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            .start(requireActivity())
    }

    private fun createImageUri(): Uri? {
        val image = File(requireContext().filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(requireContext(),
            "app.retvens.rown.fileProvider",
            image
        )
    }

    fun compressImage(imageUri: Uri): Uri {
        lateinit var compressed : Uri
        try {
            val imageBitmap : Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,imageUri)
            val path : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val fileName = String.format("%d.jpg",System.currentTimeMillis())
            val finalFile = File(path,fileName)
            val fileOutputStream = FileOutputStream(finalFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,30,fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            compressed = Uri.fromFile(finalFile)

            croppedImageUri = compressed
            hotelChainProfile.setImageURI(croppedImageUri)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            activity?.sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }


    private fun openBottomStarSelection() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_hotel_rating)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.oneStar).setOnClickListener {
            hotelOwnerStarET.setText("1 -3 Star")
            dialogRole.dismiss()
        }

        dialogRole.findViewById<RelativeLayout>(R.id.fourStar).setOnClickListener {
            hotelOwnerStarET.setText("4 Star")
            dialogRole.dismiss()
        }

        dialogRole.findViewById<RelativeLayout>(R.id.fiveStar).setOnClickListener {
            hotelOwnerStarET.setText("5 Star")
            dialogRole.dismiss()
        }

        dialogRole.findViewById<RelativeLayout>(R.id.sixStar).setOnClickListener {
            hotelOwnerStarET.setText("6 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.sevenStar).setOnClickListener {
            hotelOwnerStarET.setText("7 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.eightStar).setOnClickListener {
            hotelOwnerStarET.setText("8 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.nineStar).setOnClickListener {
            hotelOwnerStarET.setText("9 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.tenStar).setOnClickListener {
            hotelOwnerStarET.setText("10 Star")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.elevenStar).setOnClickListener {
            hotelOwnerStarET.setText(">11 Star")
            dialogRole.dismiss()
        }

    }

    private fun openHotelTypeBottom() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_hotel_type)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.singleHotelRL).setOnClickListener {
            chainHotelLayout.visibility = View.GONE
            singleHotelLayout.visibility = View.VISIBLE
            nextScreen = 0
            hotelType.setText("Single Hotel")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.hotelChainRL).setOnClickListener {
            chainHotelLayout.visibility = View.VISIBLE
            singleHotelLayout.visibility = View.GONE
            nextScreen = 1
            hotelType.setText("Hotel Chain")
            dialogRole.dismiss()
        }
    }

    override fun handleBackPressed(): Boolean {

        val fragment = BasicInformationFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return true
    }

}