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
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.ApplyJobsResponse
import app.retvens.rown.DataCollections.JobsCollection.JobDetailsDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.ActivitiesFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.CompanyDetailsFragment
import app.retvens.rown.NavigationFragments.job.jobDetailsFrags.DescriptionFragment
import app.retvens.rown.NavigationFragments.job.savedJobs.SaveJob
import app.retvens.rown.NavigationFragments.job.savedJobs.SavedJobsActivity
import app.retvens.rown.R
import app.retvens.rown.authentication.UploadRequestBody
import app.retvens.rown.databinding.ActivityJobDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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



    lateinit var binding : ActivityJobDetailsBinding
    private  var pdfUri:Uri? = null
    var PICK_PDF_REQUEST_CODE : Int = 0

    private lateinit var progressDialog:Dialog
    private lateinit var name:TextInputEditText
    private lateinit var experience:TextInputEditText
    private lateinit var resume:TextInputEditText
    private lateinit var intro:TextInputEditText
    private var skills:String = ""
    private var description:String= ""
    private var companyDertails:String=""
    private var companyWeb:String=""
    private var fragmentPositon="1"
    private  var peopleName:String=""
    private  var peopleRole:String=""
    private  var peopleProfile:String=""
    lateinit var operation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setCancelable(true)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val image = progressDialog.findViewById<ImageView>(R.id.imageview)

        Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()

        binding.backJobsDetails.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val jobId = intent.getStringExtra("jobID").toString()
        val userId = intent.getStringExtra("userId").toString()
        val saved = intent.getStringExtra("saved").toString()

        operation = if (saved != "not saved"){
            binding.saveJobsDetails.setImageResource(R.drawable.svg_saved_green)
            "pop"
        }else{
            binding.saveJobsDetails.setImageResource(R.drawable.svg_jobs_explore)
            "push"
        }
        getJobDetail(jobId, userId)



        val status = intent.getStringExtra("applyStatus")


        val text = findViewById<TextView>(R.id.appliedText)

        if (status == "Applied"){
            text.setBackgroundColor(Color.parseColor("#AFAFAF"))
            text.text = "Applied"
        }

        binding.saveJobsDetails.setOnClickListener{
            Log.e("binding.saveJobsDetails","Clicked")
            operation = if (operation == "push"){
                saveJob(jobId, "push")
                binding.saveJobsDetails.setImageResource(R.drawable.svg_saved_green)
                "pop"
            } else {
                saveJob(jobId, "pop")
                binding.saveJobsDetails.setImageResource(R.drawable.svg_jobs_explore)
                "push"
            }
        }

        binding.descriptionJobCardText.setOnClickListener {

            removeStyleAll()
            setStyle(binding.descriptionJobCardText)

            fragmentPositon="1"

            val fragment: Fragment = DescriptionFragment(description,skills)
            fragmentReplace(fragment)

        }
        binding.companyJobCardText.setOnClickListener {

            removeStyleAll()
            setStyle(binding.companyJobCardText)

            fragmentPositon="2"

            val fragment:Fragment=CompanyDetailsFragment(companyDertails,skills)
            fragmentReplace(fragment)
        }




        binding.activityJobCardText.setOnClickListener {

            removeStyleAll()
            setStyle(binding.activityJobCardText)

            fragmentPositon="3"

            val fragment:Fragment=ActivitiesFragment(peopleName,peopleRole,peopleProfile)
            fragmentReplace(fragment)

        }
        binding.cardApplyJob.setOnClickListener {


                if (status != "Applied"){

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
                    }else if (resume.length() < 3){
                        resumeLayout.error = "Select resume"
                    }else if (intro.length() < 3){
                        introLayout.error = "Enter Proper details"
                    }else{
                        dialogRole.dismiss()
                        applyforJob()
                    }

                }
                }else{
                    Toast.makeText(applicationContext,"You already Applied For this Position",Toast.LENGTH_SHORT).show()
                }

        }

    }

    private fun getJobDetail(jobId:String, userId:String){

        val getJobDetail = RetrofitBuilder.jobsApis.getJobDetail(jobId,userId)

        getJobDetail.enqueue(object : Callback<JobDetailsDataClass?> {
            override fun onResponse(
                call: Call<JobDetailsDataClass?>,
                response: Response<JobDetailsDataClass?>
            ) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()

                    val response = response.body()

                    if (response != null) {
                        binding.jobNameDetails.setText(response.jobTitle.toString())
                        binding.locationJobDetails.setText(response.jobLocation)
                        binding.jobtype.setText(response.jobType)

                        Glide.with(applicationContext).load(response.companyImage).placeholder(R.drawable.png_blog)
                            .into(binding.profileJobsDetails)

                        val jobStatus=response.status
                        description = response.jobDescription
                        skills = response.skillsRecq
                        peopleName=response.people.Full_name
                        peopleRole=response.people.Role
                        peopleProfile=response.people.Profile_pic
                        companyWeb=response.websiteLink
//                        companyDertails=response.companyDetails

                        if (fragmentPositon=="1"){

                            val fragment: Fragment = DescriptionFragment(description,skills)
                            fragmentReplace(fragment)


                        }
                        else if (fragmentPositon=="2")
                        {
                            val fragment:Fragment=CompanyDetailsFragment(companyDertails,skills)
                            fragmentReplace(fragment)

                        }
                        else if (fragmentPositon=="3"){

                            val fragment:Fragment=ActivitiesFragment(peopleName,peopleRole,peopleProfile)
                            fragmentReplace(fragment)
                        }
//                        if(jobStatus!="Not Applied")
//                        {
//                            Toast.makeText(applicationContext,"omomom",Toast.LENGTH_SHORT).show()
//                            binding.appliedText.setTextColor(ContextCompat.getColor(applicationContext, R.color.light_grey))
//                            binding.appliedText.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_grey))
//                            binding.appliedText.isClickable=true
//
//                        }

                    }
                }
                else{
                    progressDialog.dismiss()
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<JobDetailsDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error",t.message.toString())
            }
        })
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

        val experience = experience.text.toString()+" Year"
        val intro = intro.text.toString()
        val jobId = intent.getStringExtra("jobId").toString()

        val sharedPreferences = getSharedPreferences("SaveUserId", MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val send = RetrofitBuilder.jobsApis.applyJob(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),experience),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),intro),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),user_id),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),jobId),
            MultipartBody.Part.createFormData("resume", file.name, body)
        )


        progressDialog.show()
        send.enqueue(object : Callback<ApplyJobsResponse?> {
            override fun onResponse(
                call: Call<ApplyJobsResponse?>,
                response: Response<ApplyJobsResponse?>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    val response = response.body()!!
                    Toast.makeText(applicationContext,response.message.toString(),Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApplyJobsResponse?>, t: Throwable) {
                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }
//
//    private fun pushId(ApplicationId:String,jid:String) {
//
//        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
//        val user_id = sharedPreferences?.getString("user_id", "").toString()
//
//        val userID = intent.getStringExtra("userId")
//
//        val push = PushApplicantIdData(user_id,ApplicationId)
//
//        val send = RetrofitBuilder.jobsApis.pushId(jid,push)
//        send.enqueue(object : Callback<UpdateResponse?> {
//            override fun onResponse(
//                call: Call<UpdateResponse?>,
//                response: Response<UpdateResponse?>
//            ) {
//                if (response.isSuccessful){
//                    val response = response.body()!!
//                    Toast.makeText(applicationContext,response.message,Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
//                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
//            }
//        })
//
//    }

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

    private fun setStyle(textViewId:TextView )
    {
        textViewId.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
        textViewId.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))
    }

    private fun removeStyle(textViewId:TextView){

        textViewId.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
        textViewId.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
    }
    private fun removeStyleAll(){

        removeStyle(binding.descriptionJobCardText)
        removeStyle(binding.companyJobCardText)
        removeStyle(binding.activityJobCardText)
    }
    private fun fragmentReplace(fragment:Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_fragments_container,fragment)
        transaction.commit()
    }

    private fun saveJob(jid: String, operation: String) {
        val sharedPreferences = applicationContext.getSharedPreferences("SaveUserId", MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val save = RetrofitBuilder.jobsApis.saveJobs(user_id, SaveJob(operation, jid))
        save.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}