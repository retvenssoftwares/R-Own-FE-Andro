package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ReportDataClass
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetReportPost(val userId:String,val postId:String) : BottomSheetDialogFragment() {

    private lateinit var reportText:String
    var mListener: OnBottomRatingClickListener ? = null
    private var selectedReasonIndex: Int = -1
    fun setOnRatingClickListener(listener: OnBottomRatingClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetReportPost? {
        return BottomSheetReportPost(userId,postId)
    }
    interface OnBottomRatingClickListener{
        fun bottomRatingClick(ratingFrBo : String)
    }

    companion object {
        const val RATING_TAG = "BottomSheetRatingDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_reason_for_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
            reportText = "It’s spam"
            selectedReasonIndex = 0
            updateUI()
        }
        view.findViewById<RelativeLayout>(R.id.one_to_three).setOnClickListener {
            reportText = "I just don’t like it"
            selectedReasonIndex = 1
            updateUI()
        }

        view.findViewById<RelativeLayout>(R.id.three_to_six).setOnClickListener {
            reportText = "Suicide, self-injury or eating disorders"
            selectedReasonIndex = 2
            updateUI()
        }

        view.findViewById<RelativeLayout>(R.id.six_to_ten).setOnClickListener {
            reportText = "Sale of illegal or regulated goods"
            selectedReasonIndex = 3
            updateUI()
        }
        view.findViewById<RelativeLayout>(R.id.ten_to_fif).setOnClickListener {
            reportText = "Interior Nudity or sexual activity"
            selectedReasonIndex = 4
            updateUI()
        }
        view.findViewById<RelativeLayout>(R.id.fif_to_tf).setOnClickListener {
            reportText = "Hate speech or symbols"
            selectedReasonIndex = 5
            updateUI()

        }
        view.findViewById<RelativeLayout>(R.id.more_than_tf).setOnClickListener {
            reportText = "Violence or dangerous orgnaisations"
            selectedReasonIndex = 6
            updateUI()
        }
        view.findViewById<RelativeLayout>(R.id.report_8).setOnClickListener {
            reportText = "Bullying or harassment"
            selectedReasonIndex = 7
            updateUI()
        }
        view.findViewById<RelativeLayout>(R.id.report_9).setOnClickListener {
            reportText = "Intellectual property violation"
            selectedReasonIndex = 8
            updateUI()
        }
        view.findViewById<RelativeLayout>(R.id.report_10).setOnClickListener {
            reportText = "Scam or fraud"
            selectedReasonIndex = 9
                updateUI()
        }
        view.findViewById<RelativeLayout>(R.id.report_11).setOnClickListener {
            reportText = "False Information"
            selectedReasonIndex = 10
            updateUI()
        }

        view.findViewById<CardView>(R.id.card_report_next).setOnClickListener {
            report()
        }
    }

    private fun updateUI() {
        val imageOne = view?.findViewById<ImageView>(R.id.one)
        val imageTwo = view?.findViewById<ImageView>(R.id.two)
        val imageThree = view?.findViewById<ImageView>(R.id.three)
        val imageFour = view?.findViewById<ImageView>(R.id.four)
        val imageFive = view?.findViewById<ImageView>(R.id.five)
        val imageSix = view?.findViewById<ImageView>(R.id.six)
        val imageSeven = view?.findViewById<ImageView>(R.id.seven)
        val imageEight = view?.findViewById<ImageView>(R.id.eight)
        val imageNine = view?.findViewById<ImageView>(R.id.nine)
        val imageTen = view?.findViewById<ImageView>(R.id.ten)
        val imageElevem = view?.findViewById<ImageView>(R.id.eleven)

        imageOne?.setImageResource(R.drawable.rounded_image)
        imageTwo?.setImageResource(R.drawable.rounded_image)
        imageThree?.setImageResource(R.drawable.rounded_image)
        imageFour?.setImageResource(R.drawable.rounded_image)
        imageFive?.setImageResource(R.drawable.rounded_image)
        imageSix?.setImageResource(R.drawable.rounded_image)
        imageSeven?.setImageResource(R.drawable.rounded_image)
        imageEight?.setImageResource(R.drawable.rounded_image)
        imageNine?.setImageResource(R.drawable.rounded_image)
        imageTen?.setImageResource(R.drawable.rounded_image)
        imageElevem?.setImageResource(R.drawable.rounded_image)

        when(selectedReasonIndex){
            0 -> imageOne?.setImageResource(R.drawable.vector_select_dot)
            1 -> imageTwo?.setImageResource(R.drawable.vector_select_dot)
            2 -> imageThree?.setImageResource(R.drawable.vector_select_dot)
            3 -> imageFour?.setImageResource(R.drawable.vector_select_dot)
            4 -> imageFive?.setImageResource(R.drawable.vector_select_dot)
            5 -> imageSix?.setImageResource(R.drawable.vector_select_dot)
            6 -> imageSeven?.setImageResource(R.drawable.vector_select_dot)
            7 -> imageEight?.setImageResource(R.drawable.vector_select_dot)
            8 -> imageNine?.setImageResource(R.drawable.vector_select_dot)
            9 -> imageTen?.setImageResource(R.drawable.vector_select_dot)
            10 -> imageElevem?.setImageResource(R.drawable.vector_select_dot)

        }
    }

    private fun report() {

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val report = RetrofitBuilder.ProfileApis.addreport(ReportDataClass("Profile",user_id,userId,postId,reportText))

        report.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {

            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}