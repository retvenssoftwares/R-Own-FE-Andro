package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HospitalityExpertAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import app.retvens.rown.utils.setupFullHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetUpdateEducation(val data:UserProfileRequestItem.StudentEducation,val position:Int) : BottomSheetDialogFragment() {

    var mListener: OnBottomEditEdClickListener ? = null
    fun setOnEditEdClickListener(listener: OnBottomEditEdClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetUpdateEducation? {
        return BottomSheetUpdateEducation(data,position)
    }
    interface OnBottomEditEdClickListener{
        fun bottomEditEdClick(EdFrBo : String)
    }

    companion object {
        const val Edit_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_edit_education, container, false)
    }


    private lateinit var university:TextInputEditText
    private lateinit var start:TextInputEditText
    private lateinit var end:TextInputEditText

    lateinit var progressDialog : Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardSave = view.findViewById<CardView>(R.id.cardSave)
        val cardAdd = view.findViewById<CardView>(R.id.cardAdd)


        //Define Student Details
        university = view.findViewById(R.id.dob_et)
        start = view.findViewById(R.id.et_session_Start)
        end = view.findViewById(R.id.et_end)

        university.setText(data.educationPlace)
        start.setText(data.education_session_start)
        end.setText(data.education_session_end)

        cardSave.setOnClickListener {
            saveEducation()
            dismiss()
        }
        cardAdd.setOnClickListener {
            dismiss()
        }
    }

    private fun saveEducation() {

        val university = university.text.toString()
        val start = start.text.toString()
        val end = end.text.toString()

        val sharedPreferences = requireContext().getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val data = UpdateEducationDataClass(position,university,start,end)

        val updateEducation = RetrofitBuilder.profileCompletion.updateEducation(user_id,data)

        updateEducation.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}