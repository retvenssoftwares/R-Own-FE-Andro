package app.retvens.rown.NavigationFragments.job

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.ApplyJobsResponse
import app.retvens.rown.DataCollections.JobsCollection.PushApplicantIdData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.job.JobDetailsActivity.Companion.newInstance
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.ActivitiesFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.CompanyDetailsFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.DescriptionFragment
import app.retvens.rown.NavigationFragments.profile.setting.saved.SavedPostsFragment
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.databinding.ActivityJobDetailsBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mesibo.mediapicker.BaseFragment.newInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream



class JobDetailsActivity : AppCompatActivity() {

    companion object {
        private const val ARG_VALUE = "arg_value"

        fun newInstance(value: String): DescriptionFragment {
            val fragment = DescriptionFragment()
            val args = Bundle()
            args.putString(ARG_VALUE, value)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var binding : ActivityJobDetailsBinding
    private  var pdfUri:Uri? = null
    var PICK_PDF_REQUEST_CODE : Int = 0


    private lateinit var name:TextInputEditText
    private lateinit var experience:TextInputEditText
    private lateinit var resume:TextInputEditText
    private lateinit var intro:TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backJobsDetails.setOnClickListener {
            onBackPressed()
        }

        val desc = intent.getStringExtra("description")
        val skill = intent.getStringExtra("skill")

        val bundle = Bundle()
        bundle.putString("desc", desc)
        bundle.putString("skill",skill)
        val fragment: Fragment = DescriptionFragment()
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_fragments_container,fragment)
        transaction.commit()

        binding.descriptionJobCardText.setOnClickListener {
            binding.descriptionJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.descriptionJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activityJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activityJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            val desc = intent.getStringExtra("description")
            val skill = intent.getStringExtra("skill")

            val bundle = Bundle()
            bundle.putString("desc", desc)
            bundle.putString("skill",skill)

            val fragment = DescriptionFragment()
            fragment.arguments = bundle

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_fragments_container, fragment)
            transaction.commit()
        }
        binding.companyJobCardText.setOnClickListener {

            binding.companyJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.companyJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.descriptionJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.activityJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.activityJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))



            val fragment: Fragment = CompanyDetailsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_fragments_container,fragment)
            transaction.commit()
        }
        binding.activityJobCardText.setOnClickListener {

            binding.activityJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            binding.activityJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))

            binding.companyJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.companyJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            binding.descriptionJobCardText.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            binding.descriptionJobCardText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))

            val fragment: Fragment = ActivitiesFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.details_fragments_container,fragment)
            transaction.commit()
        }


            binding.cardApplyJob.setOnClickListener {

                val dialogRole = Dialog(this)
                dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogRole.setContentView(R.layout.bottom_sheet_aply_for_job)
                dialogRole.setCancelable(true)

                dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
                dialogRole.window?.setGravity(Gravity.BOTTOM)
                dialogRole.show()

                val nameLayout = dialogRole.findViewById<TextInputLayout>(R.id.nameOfApplicantLayout)
                val experienceLayout = dialogRole.findViewById<TextInputLayout>(R.id.experienceLayout)
                val introLayout = dialogRole.findViewById<TextInputLayout>(R.id.introLayout)
                val resumeLayout = dialogRole.findViewById<TextInputLayout>(R.id.resumeLayout)

                name = dialogRole.findViewById<TextInputEditText>(R.id.nameOfApplicant)
                experience = dialogRole.findViewById<TextInputEditText>(R.id.experience)
                resume = dialogRole.findViewById<TextInputEditText>(R.id.eType_et)
                intro = dialogRole.findViewById<TextInputEditText>(R.id.intro)

                resume.setOnClickListener {

                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "application/pdf"
                    startActivityForResult(intent,PICK_PDF_REQUEST_CODE)

                }

                val apply = dialogRole.findViewById<CardView>(R.id.card_job_next)
                apply.setOnClickListener {
                    if (name.length() < 3){
                        nameLayout.error = "Enter Proper details"
                    }else if(experience.length() < 1){
                        experienceLayout.error = "Enter Proper details"
                    }else if (pdfUri != null){
                        resumeLayout.error = "Select resume"
                    }else if (intro.length() < 3){
                        introLayout.error = "Enter Proper details"
                    }else{
                        applyforJob()
                    }

                }


        }





        var jobTitle = findViewById<TextView>(R.id.job_name_details)
        var location = findViewById<TextView>(R.id.location_job_details)
        var type = findViewById<TextView>(R.id.jobtype)
        var workType = findViewById<TextView>(R.id.worktype)


        jobTitle.text = intent?.getStringExtra("title")
        type.text = intent?.getStringExtra("type")
        workType.text = "Onsite"
//
//        val data = intent.getStringExtra("description")
//        description.text = data

        val locat = intent.getStringExtra("location")
        val company = intent.getStringExtra("company")


        location.text = "$company.$locat"


    }

    private fun applyforJob() {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(
            pdfUri!!,"r",null
        )?:return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =  File(cacheDir, "${contentResolver.getFileName(pdfUri!!)}.pdf")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val body = UploadRequestBody(file,"pdf")

        val name = name.text.toString()
        val experience = experience.text.toString()
        val intro = intro.text.toString()
        val jobId = intent.getStringExtra("jobId").toString()

        Toast.makeText(applicationContext,jobId,Toast.LENGTH_SHORT).show()

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val send = RetrofitBuilder.jobsApis.applyJob(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),experience),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),intro),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),user_id),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),jobId),
            MultipartBody.Part.createFormData("resume", file.name, body)
        )

        send.enqueue(object : Callback<ApplyJobsResponse?> {
            override fun onResponse(
                call: Call<ApplyJobsResponse?>,
                response: Response<ApplyJobsResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    pushId(response.applicationId,response.job_id)
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApplyJobsResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun pushId(ApplicationId:String,jid:String) {

        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val userID = intent.getStringExtra("userId")

        val push = PushApplicantIdData(user_id,ApplicationId)

        val send = RetrofitBuilder.jobsApis.pushId(jid,push)
        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            pdfUri = data.data!!
            resume.setText(pdfUri.toString())
        }
    }

    private fun ContentResolver.getFileName(coverPhotoPart: Uri): String {

        var name = ""
        val returnCursor = this.query(coverPhotoPart,null,null,null,null)
        if (returnCursor!=null){
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }
}