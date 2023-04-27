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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HotelOwnerFragment : Fragment(), BackHandler {

    lateinit var hotelType : TextInputEditText
    lateinit var noOfHotels : TextInputEditText
    lateinit var singleHotelLayout : ConstraintLayout
    lateinit var chainHotelLayout : ConstraintLayout
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

        singleHotelLayout = view.findViewById(R.id.cons_single_hotel)
        chainHotelLayout = view.findViewById(R.id.cons_chain_hotel)

        noOfHotels = view.findViewById<TextInputEditText>(R.id.noOfHotels)
        hotelType = view.findViewById<TextInputEditText>(R.id.hotel_type_et)

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