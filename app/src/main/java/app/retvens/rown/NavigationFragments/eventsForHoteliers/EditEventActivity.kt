package app.retvens.rown.NavigationFragments.eventsForHoteliers

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.UserProfileResponse
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCountryStateCity
import app.retvens.rown.bottomsheet.BottomSheetEventCategory
import app.retvens.rown.bottomsheet.BottomSheetVenueByLocation
import app.retvens.rown.databinding.ActivityEditEventBinding
import app.retvens.rown.utils.setTime
import app.retvens.rown.utils.showCalendar
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Random
import kotlin.streams.asSequence

class EditEventActivity : AppCompatActivity(),
    BottomSheetCountryStateCity.OnBottomCountryStateCityClickListener,
    BottomSheetVenueByLocation.OnBottomVBLClickListener, BottomSheetEventCategory.OnBottomEcClickListener{
    lateinit var binding:ActivityEditEventBinding

    var PICK_IMAGE_REQUEST_CODE : Int = 0
    //Cropped image uri
    private var croppedImageUri: Uri?= null

    var REQUEST_CAMERA_PERMISSION : Int = 0

    lateinit var progressDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.coverEventUpload.setOnClickListener {
            //Requesting Permission For CAMERA
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
                )
            }
            openGallery()
        }
        setDateAndTimeDialog()

        val location = intent.getStringExtra("location")
        val venue = intent.getStringExtra("venue")
        binding.etLocationEvent.setText(location)
        binding.etEventVenue.setText(venue)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val eventCategory = intent.getStringExtra("eventCategory")
        binding.eventNameEdit.setText(title)
        binding.eventDescriptionEdit.setText(description)
        binding.eventCategory.setText(eventCategory)
        binding.eventCategory.setOnClickListener {
            val bottomSheet = BottomSheetEventCategory()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetEventCategory.EC_TAG)}
            bottomSheet.setOnECclickListener(this)
        }

        val email = intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone")
        val website = intent.getStringExtra("website")
        val booking = intent.getStringExtra("booking")
        val price = intent.getStringExtra("price")
        binding.etEmailEvent.setText(email)
        binding.etPhoneVenue.setText(phone)
        binding.etWebsite.setText(website)
        binding.etBooking.setText(booking)
        binding.price.setText(price)

        val cover = intent.getStringExtra("cover")
        val eStartDate = intent.getStringExtra("eStartDate")
        val eStartTime = intent.getStringExtra("eStartTime")
        val eEndDate = intent.getStringExtra("eEndDate")
        val eEndTime = intent.getStringExtra("eEndTime")
        val rStartDate = intent.getStringExtra("rStartDate")
        val rStartTime = intent.getStringExtra("rStartTime")
        val rEndDate = intent.getStringExtra("rEndDate")
        val rEndTime = intent.getStringExtra("rEndTime")
        Glide.with(this).load(cover).into(binding.coverEventPost)
        binding.selectEventStartDate.text = eStartDate
        binding.selectEventStartTime.text = eStartTime
        binding.selectEventEndDate.text = eEndDate
        binding.selectEventEndTime.text = eEndTime
        binding.registrationStartDate.text = rStartDate
        binding.registrationStartTime.text = rStartTime
        binding.registrationEndDate.text = rEndDate
        binding.registrationEndTime.text = rEndTime

        binding.etEventVenue.setOnClickListener {
            if(binding.etLocationEvent.text.toString() == "Select Venue Location"){
                binding.userLocationPostEvent.error = "Please select location"
            } else {
                binding.userLocationPostEvent.isErrorEnabled = false
                val bottomSheet = BottomSheetVenueByLocation(binding.etLocationEvent.text.toString())
                val fragManager = supportFragmentManager
                fragManager.let { bottomSheet.show(it, BottomSheetVenueByLocation.EC_TAG) }
                bottomSheet.setOnECclickListener(this)
            }
        }
        binding.etLocationEvent.setOnClickListener {
            val bottomSheet = BottomSheetCountryStateCity()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCountryStateCity.CountryStateCity_TAG)}
            bottomSheet.setOnCountryStateCityClickListener(this)
        }

        binding.nextEventCreate.setOnClickListener {
           if (binding.etPhoneVenue.text.toString()
                    .isEmpty() || binding.etWebsite.text.toString()
                    .isEmpty() || binding.etBooking.text.toString()
                    .isEmpty() || binding.price.text.toString()
                    .isEmpty() || binding.etEmailEvent.text.toString()
                    .isEmpty() || binding.eventDescriptionEdit.text.toString()
                    .isEmpty() || binding.eventCategory.text.toString()
                    .isEmpty() || binding.eventNameEdit.text.toString()
                    .isEmpty()
                || binding.selectEventStartDate.text.toString() == "Select Date"
                || binding.selectEventStartTime.text.toString() == "Select Time"
                || binding.selectEventEndDate.text.toString()  == "Select Date"
                || binding.selectEventEndTime.text.toString() == "Select Time"
                || binding.registrationStartDate.text.toString() == "Select Date"
                || binding.registrationStartTime.text.toString() == "Select Time"
                || binding.registrationEndDate.text.toString() == "Select Date"
                || binding.registrationEndTime.text.toString() == "Select Time"
            ) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(true)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val image = progressDialog.findViewById<ImageView>(R.id.imageview)

                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()

                updateEvent()
            }
        }
    }

    private fun updateEvent() {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val location = binding.etLocationEvent.text.toString()
        val venue = binding.etEventVenue.text.toString()
        val title = binding.eventNameEdit.text.toString()
        val description = binding.eventDescriptionEdit.text.toString()
        val eventCategory = binding.eventCategory.text.toString()
        val category_id = intent.getStringExtra("category_id")
        val eventId = intent.getStringExtra("eventId")
        val email = binding.etEmailEvent.text.toString()
        val phone = binding.etPhoneVenue.text.toString()
        val website = binding.etWebsite.text.toString()
        val booking = binding.etBooking.text.toString()
        val price = binding.price.text.toString()

        val eventStartDate = binding.selectEventStartDate.text.toString()
        val eventStartTime = binding.selectEventStartTime.text.toString()
        val eventEndDate = binding.selectEventEndDate.text.toString()
        val eventEndTime = binding.selectEventEndTime.text.toString()

        val registrationStartDate = binding.registrationStartDate.text.toString()
        val registrationStartTime = binding.registrationStartTime.text.toString()
        val registrationEndDate = binding.registrationEndDate.text.toString()
        val registrationEndTime = binding.registrationEndTime.text.toString()
        if (croppedImageUri != null) {

            val randomString = Random().ints(user_id.length.toLong(), 0, user_id.length)
                .asSequence()
                .map(user_id::get)
                .joinToString("")

            val filesDir = applicationContext.filesDir
            val file = File(filesDir, "$randomString.png")

            val inputStream = contentResolver.openInputStream(croppedImageUri!!)
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

            val upEve = RetrofitBuilder.EventsApi.updateEvent(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user_id),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), venue.toString()),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    category_id.toString()
                ),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), title.toString()),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    description.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventCategory.toString()
                ),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), phone.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), booking.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), price.toString()),
                MultipartBody.Part.createFormData("event_thumbnail", file.name, requestBody),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventStartDate.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventStartTime.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventEndDate.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventEndTime.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    registrationStartDate.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    registrationStartTime.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    registrationEndDate.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    registrationEndTime.toString()
                ),
            )

            upEve.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "$category_id -> ${response.body()?.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("done", response.body()!!.message.toString())
                        progressDialog.dismiss()
                        startActivity(
                            Intent(
                                applicationContext,
                                AllEventsPostedActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        progressDialog.dismiss()
                        Log.d("done", response.code().toString())
                        Toast.makeText(
                            applicationContext,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        t.localizedMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else{
            val upWoI = RetrofitBuilder.EventsApi.updateEventWithoutImg(
                user_id,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), user_id),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), location.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), venue.toString()),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    category_id.toString()
                ),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), title.toString()),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    description.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventCategory.toString()
                ),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), phone.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), website.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), booking.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), price.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), eventStartDate.toString()),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventStartTime.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventEndDate.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    eventEndTime.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    registrationStartDate.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    registrationStartTime.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    registrationEndDate.toString()
                ),
                RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    registrationEndTime.toString()
                ),
            )
            upWoI.enqueue(object : Callback<UserProfileResponse?> {
                override fun onResponse(
                    call: Call<UserProfileResponse?>,
                    response: Response<UserProfileResponse?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "$category_id -> ${response.body()?.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("done", response.body()!!.message.toString())
                        progressDialog.dismiss()
                        startActivity(
                            Intent(
                                applicationContext,
                                AllEventsPostedActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        progressDialog.dismiss()
                        Log.d("done", response.code().toString())
                        Toast.makeText(
                            applicationContext,
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        t.localizedMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun setDateAndTimeDialog() {

        binding.selectEventStartDate.setOnClickListener {
            showCalendar(context = this){
                binding.selectEventStartDate.text = it
            }
        }
        binding.selectEventStartTime.setOnClickListener {
            setTime(this) {
                binding.selectEventStartTime.text = it
            }
        }

        binding.selectEventEndDate.setOnClickListener {
            showCalendar(context = this){
                binding.selectEventEndDate.text = it
            }
        }
        binding.selectEventEndTime.setOnClickListener {
            setTime(this) {
                binding.selectEventEndTime.text = it
            }
        }


        binding.registrationStartDate.setOnClickListener {
            showCalendar(context = this){
                binding.registrationStartDate.text = it
            }
        }
        binding.registrationStartTime.setOnClickListener {
            setTime(this) {
                binding.registrationStartTime.text = it
            }
        }

        binding.registrationEndDate.setOnClickListener {
            showCalendar(context = this){
                binding.registrationEndDate.text = it
            }
        }
        binding.registrationEndTime.setOnClickListener {
            setTime(this) {
                binding.registrationEndTime.text = it
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val imageUri = data.data
            if (imageUri != null) {
//                compressImage(imageUri)
                app.retvens.rown.utils.cropImage(imageUri, this)

            }
        }  else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val croppedImage = UCrop.getOutput(data!!)!!

                compressImage(croppedImage)

            }else if (resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(applicationContext,"Try Again",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun compressImage(imageUri: Uri): Uri {
        lateinit var compressed : Uri
        try {
            val imageBitmap : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
            val path : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val fileName = String.format("%d.jpg",System.currentTimeMillis())
            val finalFile = File(path,fileName)
            val fileOutputStream = FileOutputStream(finalFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,30,fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            compressed = Uri.fromFile(finalFile)

            croppedImageUri = compressed
            binding.coverEventPost.setImageURI(croppedImageUri)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.setData(compressed)
            sendBroadcast(intent)
        }catch (e: IOException){
            e.printStackTrace()
        }
        return compressed
    }

    override fun bottomCountryStateCityClick(CountryStateCityFrBo: String) {
        binding.etLocationEvent.setText(CountryStateCityFrBo)
    }
    override fun bottomVBLCClick(eventC: String, NumericCodeFrBo: String) {
        binding.etEventVenue.setText(eventC)
    }
    override fun bottomEcClick(eventC: String, NumericCodeFrBo: String) {
        binding.eventCategory.setText(eventC)
    }
}