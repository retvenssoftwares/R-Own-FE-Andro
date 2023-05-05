package app.retvens.rown.Dashboard.profileCompletion.frags

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HospitalityExpertAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.CompanyDatacClass
import app.retvens.rown.DataCollections.ProfileCompletion.JobData
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetJobType
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobTitleFragment : Fragment(), BackHandler, BottomSheetJobType.OnBottomJobTypeClickListener {

    lateinit var eTypeET : TextInputEditText
    lateinit var recentCoET : TextInputEditText
    private lateinit var jobTitle:TextInputEditText
    private lateinit var jobTitleLayout : TextInputLayout
    private lateinit var jobType:TextInputEditText
    private lateinit var companyName:TextInputEditText
    private lateinit var start:TextInputEditText
    private lateinit var end:TextInputEditText
    private lateinit var startLayout : TextInputLayout
    private lateinit var endLayout : TextInputLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var hospitalityExpertAdapter: HospitalityExpertAdapter
    private lateinit var dialogRole:Dialog
    private lateinit var searchBar:EditText

    lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_title, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eTypeET = view.findViewById(R.id.eType_et)
        recentCoET = view.findViewById(R.id.recentComET)
        recentCoET.setOnClickListener {
            openRecentCompanyBottom()
        }

        eTypeET.setOnClickListener {
            val bottomSheet = BottomSheetJobType()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobType.Job_TYPE_TAG)}
            bottomSheet.setOnJobTypeClickListener(this)
        }

        val submit = view.findViewById<CardView>(R.id.card_job_next)

        jobTitle = view.findViewById(R.id.rJob_et)
        jobType = view.findViewById(R.id.eType_et)
        companyName = view.findViewById(R.id.recentComET)
        start = view.findViewById(R.id.et_session_Start)
        end = view.findViewById(R.id.et_session_end)
        jobTitleLayout = view.findViewById(R.id.rJob_etLayout)
        startLayout = view.findViewById(R.id.session_start)
        endLayout = view.findViewById(R.id.session_end)

        submit.setOnClickListener {
            if(jobTitle.length() < 3){
                jobTitleLayout.error = "Please enter your Job Tittle"
            } else if(start.length() < 3){
                jobTitleLayout.isErrorEnabled = false
                startLayout.error = "Please enter start year"
            } else if(end.length() < 3){
                jobTitleLayout.isErrorEnabled = false
                startLayout.isErrorEnabled = false
                endLayout.error = "Please enter end year"
            } else {
                progressDialog = Dialog(requireContext())
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(requireContext()).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                setJobDetails()
            }
        }

    }

    private fun setJobDetails() {
        val title = jobTitle.text.toString()
        val type = jobType.text.toString()
        val company = companyName.text.toString()
        val start = start.text.toString()
        val end = end.text.toString()

        val data = JobData(JobData.JobDetails(type,title,company,start,end))

        val sharedPreferences = context?.getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences?.getString("user_id", "").toString()

        val update = RetrofitBuilder.profileCompletion.setJobDetail(user_id,data)

        update.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context,DashBoardActivity::class.java))
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

    private fun openRecentCompanyBottom() {

        dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_select_company)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        recyclerView = dialogRole.findViewById(R.id.company_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchBar = dialogRole.findViewById(R.id.searchCompany)

        getCompany()

    }

    private fun getCompany() {
        val get = RetrofitBuilder.profileCompletion.getCompany()

        get.enqueue(object : Callback<List<CompanyDatacClass>?>,
            HospitalityExpertAdapter.OnJobClickListener {
            override fun onResponse(
                call: Call<List<CompanyDatacClass>?>,
                response: Response<List<CompanyDatacClass>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    val originalData = response.toList()
                    hospitalityExpertAdapter = HospitalityExpertAdapter(requireContext(),response)
                    hospitalityExpertAdapter.notifyDataSetChanged()
                    recyclerView.adapter = hospitalityExpertAdapter
                    hospitalityExpertAdapter.setOnJobClickListener(this)

                    searchBar.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            val filterData = originalData.filter { item ->
                                item.company_name.contains(p0.toString(),ignoreCase = true)
                            }

                            hospitalityExpertAdapter.updateData(filterData)
                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }

                    })

                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CompanyDatacClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
            }

            override fun onJobClick(job: CompanyDatacClass) {
                recentCoET.setText(job.company_name)
                dialogRole.dismiss()
            }
        })
    }

    override fun handleBackPressed(): Boolean {

        val fragment = BasicInformationFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_username,fragment)
        transaction?.commit()

        return  true
    }

    override fun bottomJobTypeClick(jobTypeFrBo: String) {
        eTypeET.setText(jobTypeFrBo)
    }

}