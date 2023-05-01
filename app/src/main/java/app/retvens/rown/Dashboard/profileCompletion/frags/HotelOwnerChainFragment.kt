package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainAdapter
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HotelChainData
import app.retvens.rown.R
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

class HotelOwnerChainFragment : Fragment(), BackHandler, HotelChainAdapter.onCoverImageClickListener{

    lateinit var dialog: Dialog
    lateinit var chainRecyclerView: RecyclerView
    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedHotelChainCoverImageUri: Uri?= null  // Final Uri for Hotel chain Cover

    var REQUEST_CAMERA_PERMISSION : Int = 0
    lateinit var cameraHotelChainImageUri: Uri
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
//        cropImage(cameraHotelChainImageUri)
            croppedHotelChainCoverImageUri = cameraHotelChainImageUri
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_owner_chain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cameraHotelChainImageUri = createImageUri()!!

        chainRecyclerView = view.findViewById(R.id.chainHotelRecyclerView)
        chainRecyclerView.layoutManager = LinearLayoutManager(context)
        chainRecyclerView.setHasFixedSize(true)

        setUpChainAdapter()

    }

    private fun setUpChainAdapter() {
        val chainHotelList : MutableList<HotelChainData> = mutableListOf()

        var n : Int = 2

        if(arguments?.getString("hotels") != null){
            val no = arguments?.getString("hotels")
             n = no!!.toInt()
        }

        for (i in 1..n) {
            val hotelChainData = HotelChainData(
                R.drawable.png_post,
                "Hotel $i Name",
                "Hotel $i Star rating",
                "Hotel $i Location"
            )
            chainHotelList.add(hotelChainData)
        }

        val hotelChainAdapter = HotelChainAdapter(chainHotelList, requireContext(), croppedHotelChainCoverImageUri)
        chainRecyclerView.adapter = hotelChainAdapter
        hotelChainAdapter.notifyDataSetChanged()
        hotelChainAdapter.setOnCoverClickListener(this@HotelOwnerChainFragment)
    }

    override fun handleBackPressed(): Boolean {

        val fragment = HotelOwnerFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return true

    }

    override fun onCoverImageClick(userType: String) {
        openBottomCameraSheet()
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
        croppedHotelChainCoverImageUri = null
        dialog.dismiss()
    }
    private fun openCamera() {
        contract.launch(cameraHotelChainImageUri)
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

//                cropImage(imageUri)

                    croppedHotelChainCoverImageUri = imageUri
            }
        }  else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val resultingImage = CropImage.getActivityResult(data)
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = resultingImage.uri

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(context,"Try Again : ${resultingImage.error}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun createImageUri(): Uri? {
        val image = File(requireContext().filesDir,"camera_photo.png")
        return FileProvider.getUriForFile(requireContext(),
            "app.retvens.rown.fileProvider",
            image
        )
    }
}