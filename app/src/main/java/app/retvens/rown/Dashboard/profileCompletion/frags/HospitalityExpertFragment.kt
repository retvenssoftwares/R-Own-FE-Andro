package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText

class HospitalityExpertFragment : Fragment(), BackHandler {


    lateinit var eTypeET : TextInputEditText
    lateinit var recentCoET : TextInputEditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hospitality_expert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        eTypeET = view.findViewById(R.id.hos_expert_job_type)
        recentCoET = view.findViewById(R.id.hos_expert_company)
        recentCoET.setOnClickListener {
            openRecentCompanyBottom()
        }

        eTypeET.setOnClickListener {
            showBottomJobType()
        }


    }


    private fun openRecentCompanyBottom() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_select_company)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<LinearLayout>(R.id.tatapl).setOnClickListener {
            recentCoET.setText("Tata Consultancy ltd.")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<LinearLayout>(R.id.tatasteel).setOnClickListener {
            recentCoET.setText("Tata Consultancy ltd.")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<LinearLayout>(R.id.useTata).setOnClickListener {
            recentCoET.setText("Tata")
            dialogRole.dismiss()
        }
    }

    private fun showBottomJobType() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_job_type)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
            eTypeET.setText("Select one -")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.fullTime).setOnClickListener {
            eTypeET.setText("Full-Time")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.partTime).setOnClickListener {
            eTypeET.setText("Part-Time")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.selfE).setOnClickListener {
            eTypeET.setText("Self-Employed")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Freelancer).setOnClickListener {
            eTypeET.setText("Freelancer")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Internship).setOnClickListener {
            eTypeET.setText("Internship")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Trainee).setOnClickListener {
            eTypeET.setText("Trainee")
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