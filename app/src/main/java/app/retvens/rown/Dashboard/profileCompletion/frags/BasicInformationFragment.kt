package app.retvens.rown.Dashboard.profileCompletion.frags

import android.annotation.SuppressLint
import android.app.Dialog
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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.DataCollections.ProfileCompletion.BasicInfoClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BasicInformationFragment : Fragment(), BackHandler {

    private var isStudent : Boolean = true
    private var isHotelier : Boolean = true

    private var nextFrag : Int = 0

    lateinit var myRoleInHos : TextInputLayout
    lateinit var myRoleInHosET : TextInputEditText
    lateinit var myRecentJobET : TextInputEditText

    private lateinit var university:TextInputEditText
    private lateinit var start:TextInputEditText
    private lateinit var end:TextInputEditText

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
        start = view.findViewById(R.id.et_session_Start)
        end = view.findViewById(R.id.et_end)


        val recentJob = view.findViewById<TextInputLayout>(R.id.recentJobLayout)

        view.findViewById<RelativeLayout>(R.id.student).setOnClickListener {

            if (isStudent){
                switchStudent.setImageResource(R.drawable.switch_yes)
                switchStudentText.text = "Yes"
                nextFrag = 0
                studentInfo.visibility = View.VISIBLE
                recentJob.visibility = View.GONE
                isStudent = false
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
                myRoleInHos.visibility = View.VISIBLE
                recentJob.visibility = View.GONE
                isHotelier = false
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
            setJobTitle()
        }

//        requireActivity().onBackPressedDispatcher.addCallback(
//            requireActivity(),
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    Log.d("TAG", "Fragment back pressed invoked")
//                    // Do custom work here
//
//                    // if you want onBackPressed() to be called as normal afterwards
////            if (isEnabled) {
////                isEnabled = false
////                requireActivity().onBackPressed()
////            }
//                }
//            }
//        )
    }

    private fun setJobTitle() {
        val sharedPreferences =  requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()

        val university = university.text.toString()
        val start = start.text.toString()
        val end = end.text.toString()
        val role:String

        val roleInHotel = myRoleInHosET.text.toString()
        val roleInJob = myRecentJobET.text.toString()



        Toast.makeText(requireContext(),"${myRecentJobET.text.toString()}",Toast.LENGTH_SHORT).show()

        if (roleInJob.isNotEmpty()){
            role = roleInJob
        }else if (roleInHotel.isNotEmpty()){
            role = roleInHotel
        }
        else{
            role = ""
        }

        val info = BasicInfoClass(role, BasicInfoClass.EducationData(university,start,end))

        val update = RetrofitBuilder.profileCompletion.setJobRole("Oo7PCzo0-",info)

        update.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()

                    when (nextFrag) {
                        0 -> {
                            val fragment = JobTitleFragment()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()
                        }
                        1 -> {

                            val fragment = HotelOwnerFragment()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()

                        }
                        2 -> {

                            val fragment = HospitalityExpertFragment()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()

                        }
                        3 -> {

                            val fragment = VendorsFragment()
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.fragment_username,fragment)
                            transaction?.commit()

                        }
                    }

                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })


    }

    @SuppressLint("SetTextI18n")
    private fun openBottomRecentJob() {
        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_job_title)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<LinearLayout>(R.id.hotel_owner).setOnClickListener {
            myRecentJobET.setText("Assistant")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<LinearLayout>(R.id.hos_expert).setOnClickListener {
            myRecentJobET.setText("Associate")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<LinearLayout>(R.id.vendor).setOnClickListener {
            myRecentJobET.setText("Administrative Assistant")
            dialogRole.dismiss()
        }
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
}