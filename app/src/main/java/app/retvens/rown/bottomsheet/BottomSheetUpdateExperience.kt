package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HospitalityExpertAdapter
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfoo
import app.retvens.rown.DataCollections.ProfileCompletion.AddExperienceDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateExperienceDataClass
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.UserProfileRequestItem
import app.retvens.rown.R
import app.retvens.rown.utils.setupFullHeight
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetUpdateExperience(val data:NormalUserInfoo,val position:Int) : BottomSheetDialogFragment(),
    BottomSheetJobType.OnBottomJobTypeClickListener,
    BottomSheetJobTitle.OnBottomJobTitleClickListener,
    BottomSheetCompany.OnBottomCompanyClickListener  {

//    var mListener: OnBottomEditExClickListener ? = null
//    fun setOnEditExClickListener(listener: Callback<UserProfileRequestItem?>){
//        mListener = listener
//    }
    fun newInstance(): BottomSheetUpdateExperience? {
        return BottomSheetUpdateExperience(data,position)
    }
    interface OnBottomEditExClickListener{
        fun bottomEditClick(CTCFrBo : String)
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
        return inflater.inflate(R.layout.bottom_sheet_edit_expereince, container, false)
    }

    lateinit var eTypeET : TextInputEditText
    lateinit var recentCoET : TextInputEditText
    private lateinit var jobTitle: TextInputEditText
    private lateinit var jobTitleLayout : TextInputLayout
    private lateinit var jobType: TextInputEditText
    private lateinit var companyName: TextInputEditText
    private lateinit var start: TextInputEditText
    private lateinit var end: TextInputEditText
    private lateinit var startLayout : TextInputLayout
    private lateinit var endLayout : TextInputLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var hospitalityExpertAdapter: HospitalityExpertAdapter
    private lateinit var dialogRole: Dialog
    private lateinit var searchBar: EditText


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recentCoET = view.findViewById(R.id.recentComET)
        recentCoET.setOnClickListener {
            val bottomSheet = BottomSheetCompany()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCompany.Company_TAG)}
            bottomSheet.setOnCompanyClickListener(this)
//            openRecentCompanyBottom()
        }

        eTypeET = view.findViewById(R.id.eType_et)
        eTypeET.setOnClickListener {
            val bottomSheet = BottomSheetJobType()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobType.Job_TYPE_TAG)}
            bottomSheet.setOnJobTypeClickListener(this)
        }

        val cardSave = view.findViewById<CardView>(R.id.cardSave)
        val cardAdd = view.findViewById<CardView>(R.id.cardAdd)

        jobTitle = view.findViewById(R.id.rJob_et)
        jobType = view.findViewById(R.id.eType_et)
        companyName = view.findViewById(R.id.recentComET)
        start = view.findViewById(R.id.et_session_Start)
        end = view.findViewById(R.id.et_session_end)
        jobTitleLayout = view.findViewById(R.id.rJob_etLayout)
        startLayout = view.findViewById(R.id.session_start)
        endLayout = view.findViewById(R.id.session_end)


        jobTitle.setText(data.jobTitle)
        jobType.setText(data.jobType)
        companyName.setText(data.jobCompany)
        start.setText(data.jobStartYear)
        end.setText(data.jobEndYear)

        jobTitle.setOnClickListener {
            val bottomSheet = BottomSheetJobTitle()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobTitle.Job_Title_TAG)}
            bottomSheet.setOnJobTitleClickListener(this)
        }

        cardSave.setOnClickListener {
            updateExperience()

            dismiss()
        }
        cardAdd.setOnClickListener {
            dismiss()
        }



    }

    private fun updateExperience() {

        val index = position
        val title = jobTitle.text.toString()
        val type = jobType.text.toString()
        val company = companyName.text.toString()
        val start = start.text.toString()
        val end = end.text.toString()

        val data = UpdateExperienceDataClass(index,type,company,start,end,title)

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val updateData = RetrofitBuilder.profileCompletion.updateExperience(user_id,data)

        updateData.enqueue(object : Callback<UpdateResponse?> {
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
//        mListener = null
    }

    override fun bottomJobTitleClick(jobTitleFrBo: String) {
        jobTitle.setText(jobTitleFrBo)
    }

    override fun bottomJobTypeClick(jobTypeFrBo: String) {
        eTypeET.setText(jobTypeFrBo)
    }

    override fun bottomLocationClick(CompanyFrBo: String) {
        companyName.setText(CompanyFrBo)
    }
}