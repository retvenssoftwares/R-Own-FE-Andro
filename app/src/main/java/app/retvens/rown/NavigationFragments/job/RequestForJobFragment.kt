package app.retvens.rown.NavigationFragments.job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.JobResponseDataClass
import app.retvens.rown.DataCollections.JobsCollection.RequestJobDataClass
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCTC
import app.retvens.rown.bottomsheet.BottomSheetJobDepartment
import app.retvens.rown.bottomsheet.BottomSheetJobDesignation
import app.retvens.rown.bottomsheet.BottomSheetJobType
import app.retvens.rown.bottomsheet.BottomSheetLocation
import app.retvens.rown.bottomsheet.BottomSheetNoticePeriod
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RequestForJobFragment : Fragment(),
    BottomSheetJobDepartment.OnBottomJobDepartmentClickListener,
    BottomSheetJobDesignation.OnBottomJobDesignationClickListener,
    BottomSheetJobType.OnBottomJobTypeClickListener,
    BottomSheetNoticePeriod.OnBottomNoticeClickListener,
    BottomSheetCTC.OnBottomCTCClickListener,
    BottomSheetLocation.OnBottomLocationClickListener {

    lateinit var selectDepartmentET : TextInputEditText
    lateinit var selectDesignationET : TextInputEditText
    lateinit var selectJobEmploymentET : TextInputEditText
    lateinit var selectJobLocationET : TextInputEditText
    lateinit var selectNoticeET : TextInputEditText
    lateinit var expectedCTCeET : TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_for_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestAJob()

        view.findViewById<CardView>(R.id.card_post_job_request).setOnClickListener {
            RequestJob()
        }
    }
    private fun requestAJob() {
        selectDepartmentET = requireView().findViewById(R.id.select_department_et)
        selectDepartmentET.setOnClickListener {
            val bottomSheet = BottomSheetJobDepartment()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobDepartment.Job_D_TAG)}
            bottomSheet.setOnJobDepartmentClickListener(this)
        }

        selectDesignationET = requireView().findViewById(R.id.select_designation_et)
        selectDesignationET.setOnClickListener {
            val bottomSheet = BottomSheetJobDesignation()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobDesignation.Job_Designation_TAG)}
            bottomSheet.setOnJobDesignationClickListener(this)
        }

        selectJobEmploymentET = requireView().findViewById(R.id.select_job_employment_et)
        selectJobEmploymentET.setOnClickListener {
            val bottomSheet = BottomSheetJobType()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobType.Job_TYPE_TAG)}
            bottomSheet.setOnJobTypeClickListener(this)
        }

        selectJobLocationET = requireView().findViewById(R.id.select_job_location_et)
        selectJobLocationET.setOnClickListener {
            val bottomSheet = BottomSheetLocation()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
            bottomSheet.setOnLocationClickListener(this)
        }

        selectNoticeET = requireView().findViewById(R.id.select_notice_et)
        selectNoticeET.setOnClickListener {
            val bottomSheet = BottomSheetNoticePeriod()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetNoticePeriod.Notice_TAG)}
            bottomSheet.setOnNoticeClickListener(this)
        }

        expectedCTCeET = requireView().findViewById(R.id.select_ctc_et)
        expectedCTCeET.setOnClickListener {
            val bottomSheet = BottomSheetCTC()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCTC.CTC_TAG)}
            bottomSheet.setOnCTCClickListener(this)
        }
    }

    private fun RequestJob() {

        val department = selectDepartmentET.text.toString()
        val designation = selectDesignationET.text.toString()
        val type = selectJobEmploymentET.text.toString()
        val location = selectJobLocationET.text.toString()
        val noticePeriod = selectNoticeET.text.toString()
        val ctc = expectedCTCeET.text.toString()

        val sharedPreferences = requireContext()?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val data = RequestJobDataClass(user_id,department,type,designation,noticePeriod,location,ctc)

        val send = RetrofitBuilder.jobsApis.requestJob(data)

        send.enqueue(object : Callback<JobResponseDataClass?> {
            override fun onResponse(
                call: Call<JobResponseDataClass?>,
                response: Response<JobResponseDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JobResponseDataClass?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun bottomJobDepartmentClick(jobDepartFrBo: String) {
        selectDepartmentET.setText(jobDepartFrBo)
    }

    override fun bottomJobDesignationClick(jobDesignationFrBo: String) {
        selectDesignationET.setText(jobDesignationFrBo)
    }

    override fun bottomJobTypeClick(jobTypeFrBo: String) {
        selectJobEmploymentET.setText(jobTypeFrBo)
    }

    override fun bottomNoticeClick(noticeFrBo: String) {
        selectNoticeET.setText(noticeFrBo)
    }

    override fun bottomCTCClick(CTCFrBo: String) {
        expectedCTCeET.setText(CTCFrBo)
    }


    override fun bottomLocationClick(LocationFrBo: String, NumericCodeFrBo: String) {
        selectJobLocationET.setText(LocationFrBo)
    }
}