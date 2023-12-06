package app.retvens.rown.Dashboard.profileCompletion.frags

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.BasicInfoClass
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetJobTitle
import app.retvens.rown.utils.endYearDialog
import app.retvens.rown.utils.profileComStatus
import app.retvens.rown.utils.profileCompletionStatus
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mesibo.api.Mesibo
import com.whiteelephant.monthpicker.MonthPickerDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class BasicInformationFragment : Fragment(),
    BackHandler,BasicInformationAdapter.OnJobClickListener,
    BottomSheetJobTitle.OnBottomJobTitleClickListener {

    private var isStudent : Boolean = true
    private var isHotelier : Boolean = true

    private var nextFrag : Int = 0
    private var selectedYear : Int = 1900

    private lateinit var myRoleInHos : TextInputLayout
    lateinit var myRoleInHosET : TextInputEditText
    lateinit var myRecentJobET : TextInputEditText

    private lateinit var university:TextInputEditText
    private lateinit var universityLayout:TextInputLayout
    private lateinit var start:TextInputEditText
    private lateinit var end:TextInputEditText
    lateinit var progressDialog : Dialog

    private lateinit var startLayout : TextInputLayout
    private lateinit var endLayout : TextInputLayout

    lateinit var presentText : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchStudent = view.findViewById<ImageView>(R.id.switch_student)
        val switchStudentText = view.findViewById<TextView>(R.id.switch_student_text)
        val studentInfo = view.findViewById<LinearLayout>(R.id.ll_stud)

        val switchHotelier = view.findViewById<ImageView>(R.id.switch_hotel)
        val switchHotelText = view.findViewById<TextView>(R.id.switch_hotel_text)
        myRoleInHos = view.findViewById(R.id.my_role_hospitality)
        myRoleInHosET = view.findViewById(R.id.myRoleInHosET)
        myRecentJobET = view.findViewById(R.id.my_recent_job)

        //Define Student Details
        university = view.findViewById(R.id.dob_et)
        universityLayout = view.findViewById(R.id.university)
        start = view.findViewById(R.id.et_session_Start)
        end = view.findViewById(R.id.et_end)
        startLayout = view.findViewById(R.id.session_start)
        endLayout = view.findViewById(R.id.session_end)

        presentText = view.findViewById(R.id.presentText)
        presentText.setOnClickListener {
            end.setText("Present")
            endLayout.isErrorEnabled = false
        }

        val decline = view.findViewById<ImageView>(R.id.decline)
        decline.setOnClickListener {
            startActivity(Intent(requireContext(), DashBoardActivity::class.java))
        }

        start.setOnClickListener {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val builder : MonthPickerDialog.Builder  = MonthPickerDialog.Builder(requireContext(),
                MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYears ->
                    start.setText("$selectedYears")
                    selectedYear = selectedYears
                    end.setText("")
                }, Calendar.YEAR, Calendar.MONTH)

            builder
                .setActivatedYear(currentYear)
//                .setMaxYear(2030)
                .setTitle("Select Starting Year")
                .setYearRange(1950, currentYear)
                .showYearOnly()
                .setOnYearChangedListener {
                    start.setText("$it")
                    selectedYear = it
                    end.setText("")
                }
                .build()
                .show()
        }

        end.setOnClickListener {
           endYearDialog(requireContext(), end, selectedYear)
        }

        val recentJob = view.findViewById<TextInputLayout>(R.id.recentJobLayout)

        view.findViewById<RelativeLayout>(R.id.student).setOnClickListener {
            if (isStudent){
                switchStudent.setImageResource(R.drawable.switch_yes)
                switchStudentText.text = "Yes"
                nextFrag = 0
                studentInfo.visibility = View.VISIBLE
                recentJob.visibility = View.GONE
                isStudent = false



                switchHotelier.setImageResource(R.drawable.switch_no)
                switchHotelText.text = "No"
                myRoleInHos.visibility = View.GONE
                isHotelier = true
            }else{
                switchStudent.setImageResource(R.drawable.switch_no)
                switchStudentText.text = "No"
                studentInfo.visibility = View.GONE
                isStudent = true
            }

            if(isHotelier && isStudent){
            recentJob.visibility = View.VISIBLE
            }
        }

        view.findViewById<RelativeLayout>(R.id.hotelier).setOnClickListener {

            if (isHotelier){
                switchHotelier.setImageResource(R.drawable.switch_yes)
                switchHotelText.text = "Yes"
                nextFrag = 1
                openBottomRoleinHos()
                myRoleInHos.visibility = View.VISIBLE
                recentJob.visibility = View.GONE
                isHotelier = false

                switchStudent.setImageResource(R.drawable.switch_no)
                switchStudentText.text = "No"
                studentInfo.visibility = View.GONE
                isStudent = true
            }else{
                switchHotelier.setImageResource(R.drawable.switch_no)
                switchHotelText.text = "No"
                nextFrag = 0
                myRoleInHos.visibility = View.GONE
                isHotelier = true
            }

            if(isHotelier && isStudent){
            recentJob.visibility = View.VISIBLE
            }
        }



        myRecentJobET.setOnClickListener {
            nextFrag = 0
            openBottomRecentJob()
        }
        myRoleInHosET.setOnClickListener {
            openBottomRoleinHos()
        }

        view.findViewById<CardView>(R.id.card_basic_next).setOnClickListener {
            if (!isHotelier && !isStudent){
                if (myRoleInHosET.text.toString() == "My Role in hospitality"){
                    myRoleInHos.error = "Select your Role in hospitality"
                } else {
                    progressDialog = Dialog(requireContext())
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    progressDialog.setCancelable(false)
                    progressDialog.setContentView(R.layout.progress_dialoge)
                    progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                    Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                    progressDialog.show()

                    setJobTitle(myRoleInHosET.text.toString())
//                    Toast.makeText(requireContext(), "S H", Toast.LENGTH_SHORT).show()
                }
            } else if (!isHotelier){
                if (myRoleInHosET.text.toString() == "My Role in hospitality"){
                    myRoleInHos.error = "Select your Role in hospitality"
                } else {
                    progressDialog = Dialog(requireContext())
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    progressDialog.setCancelable(false)
                    progressDialog.setContentView(R.layout.progress_dialoge)
                    progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                    Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                    progressDialog.show()

                    setJobTitle(myRoleInHosET.text.toString())
//                    Toast.makeText(requireContext(), "H", Toast.LENGTH_SHORT).show()
                }
            } else if (!isStudent) {
                if (university.text.toString() == ""){
                    universityLayout.error = "Select your College"
                } else if (start.length() < 3){
                    startLayout.error = "Select Year"
                } else if (end.length() < 3){
                    endLayout.error = "Select Year"
                } else  {
                    progressDialog = Dialog(requireContext())
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    progressDialog.setCancelable(false)
                    progressDialog.setContentView(R.layout.progress_dialoge)
                    progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                    Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                    progressDialog.show()

                    saveUniversity()
                    setJobTitle("Normal User")
//                    Toast.makeText(requireContext(), "S", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (myRecentJobET.text.toString() == "Most Recent Job Title"){
                    recentJob.error = "Select your Recent Job Title"
                } else  {
                    progressDialog = Dialog(requireContext())
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    progressDialog.setCancelable(false)
                    progressDialog.setContentView(R.layout.progress_dialoge)
                    progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                    Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                    progressDialog.show()

                    setJobTitle("Normal User")
//                    Toast.makeText(requireContext(), "Job Title", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUniversity() {

        val university = university.text.toString()
        val start = start.text.toString()
        val end = end.text.toString()

        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val data = UserProfileRequestItem.StudentEducation(university,start,end)

        val updateEducation = RetrofitBuilder.profileCompletion.addEducation(user_id,data)

        updateEducation.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
//                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }

    private fun setJobTitle(role : String) {
        val sharedPreferences =  context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val university = university.text.toString()
        val start = start.text.toString()
        val end = end.text.toString()



        val info = BasicInfoClass(role, BasicInfoClass.EducationData(university,start,end))

        val update = RetrofitBuilder.profileCompletion.setJobRole(user_id,info)

        update.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val sharedPreferences1 = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
                    val user_id1 = sharedPreferences1?.getString("user_id", "").toString()

                    val sharedPreferences2 = requireContext().getSharedPreferences("savePhoneNo", AppCompatActivity.MODE_PRIVATE)
                    val phone2 = sharedPreferences2?.getString("savePhoneNumber", "").toString()
                    progressDialog.dismiss()

                    when (nextFrag) {
                        0 -> {
                            profileComStatus(context!!, "80")
                            profileCompletionStatus = "80"
                            val fragment = JobTitleFragment()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()


                            val setStatus = encodeData(user_id1,"Normal User")

                            val profilez = Mesibo.getProfile(phone2)

                            profilez.status = setStatus
                            profilez.save()
                        }
                        1 -> {
                            profileComStatus(context!!, "75")
                            profileCompletionStatus = "75"
                            app.retvens.rown.utils.role = "Hotel Owner"
                            val fragment = HotelOwnerFragment()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()
                            val setStatus = encodeData(user_id1,"Hotel Owner")

                            val profilez = Mesibo.getProfile(phone2)

                            profilez.status = setStatus
                            profilez.save()
                        }
                        2 -> {
                            profileComStatus(context!!, "85")
                            profileCompletionStatus = "85"
                            val fragment = HospitalityExpertFragment()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()

                            val setStatus = encodeData(user_id1,"Hospitality Expert")

                            val profilez = Mesibo.getProfile(phone2)

                            profilez.status = setStatus
                            profilez.save()
                        }
                        3 -> {
                            profileComStatus(context!!, "90")
                            profileCompletionStatus = "90"
                            app.retvens.rown.utils.role = "Business Vendor / Freelancer"
                            val fragment = VendorsFragment()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()

                            val setStatus = encodeData(user_id1,"Business Vendor / Freelancer")

                            val profilez = Mesibo.getProfile(phone2)

                            profilez.status = setStatus
                            profilez.save()
                        }
                    }

                }else{
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    fun encodeString(input: String, shift: Int): String {
        val encodedData = StringBuilder()
        for (char in input) {
            val encodedChar = when {
                char.isLetter() -> {
                    val base = if (char.isLowerCase()) 'a' else 'A'
                    val encodedAscii = (char.toInt() - base.toInt() + shift) % 26
                    (encodedAscii + base.toInt()).toChar()
                }
                else -> char
            }
            encodedData.append(encodedChar)
        }
        return encodedData.toString()
    }

    fun encodeData(userID: String, userRole: String): String {
        val encodedUserID = encodeString(userID, 5)
        val encodedUserRole = encodeString(userRole, 6)
        return "$encodedUserID|$encodedUserRole"
    }

    private fun openBottomRecentJob() {
        val bottomSheet = BottomSheetJobTitle()
        val fragManager = (activity as FragmentActivity).supportFragmentManager
        fragManager.let{bottomSheet.show(it, BottomSheetJobTitle.Job_Title_TAG)}
        bottomSheet.setOnJobTitleClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun openBottomRoleinHos() {
        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_hospitality_role)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<LinearLayout>(R.id.hotel_owner).setOnClickListener {
            myRoleInHosET.setText("Hotel Owner")
            nextFrag = 1
            dialogRole.dismiss()
        }
        dialogRole.findViewById<LinearLayout>(R.id.hos_expert).setOnClickListener {
            myRoleInHosET.setText("Hospitality Expert")
            nextFrag = 2
            dialogRole.dismiss()
        }
        dialogRole.findViewById<LinearLayout>(R.id.vendor).setOnClickListener {
            myRoleInHosET.setText("Business Vendor / Freelancer")
            nextFrag = 3
            dialogRole.dismiss()
        }
    }

    override fun handleBackPressed(): Boolean {

        val fragment = LocationFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return true
    }

    override fun onJobClick(job: GetJobDataClass) {

    }

    override fun bottomJobTitleClick(jobTitleFrBo: String) {
        myRecentJobET.setText(jobTitleFrBo)
    }
}