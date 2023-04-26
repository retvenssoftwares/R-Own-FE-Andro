package app.retvens.rown.Dashboard.profileCompletion.frags

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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class LocationFragment : Fragment(), BackHandler {

    lateinit var etLocation : TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<TextInputLayout>(R.id.user_location_field).setEndIconOnClickListener {
            Toast.makeText(context, "Location", Toast.LENGTH_SHORT).show()
        }

        etLocation = view.findViewById(R.id.et_location)

        etLocation.setOnClickListener {
            openLocationSheet()
        }

        view.findViewById<CardView>(R.id.card_location_next).setOnClickListener {
            val fragment = BasicInformationFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_username,fragment)
            transaction?.commit()
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

    private fun openLocationSheet() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_location)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<LinearLayout>(R.id.location).setOnClickListener {
            etLocation.setText("Kharghar, Mumbai, Maharastra, India")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<LinearLayout>(R.id.location1).setOnClickListener {
            etLocation.setText("Kharghar, Mumbai, Maharastra, India")
            dialogRole.dismiss()
        }
    }

    override fun handleBackPressed(): Boolean {

        val fragment = UsernameFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return true
    }

}