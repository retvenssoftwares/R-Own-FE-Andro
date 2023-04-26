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
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class BasicInformationFragment : Fragment(), BackHandler {

    private var isStudent : Boolean = true
    private var isHotelier : Boolean = true

    private var nextFrag : Int = 0

    lateinit var myRoleInHos : TextInputLayout
    lateinit var myRoleInHosET : TextInputEditText
    lateinit var myRecentJobET : TextInputEditText

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