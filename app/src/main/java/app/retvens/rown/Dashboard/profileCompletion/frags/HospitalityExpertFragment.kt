package app.retvens.rown.Dashboard.profileCompletion.frags

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
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.Dashboard.profileCompletion.BackHandler
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.HospitalityExpertAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.*
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HospitalityExpertFragment : Fragment(), BackHandler {


    lateinit var eTypeET : TextInputEditText
    lateinit var recentCoET : TextInputEditText
    private lateinit var jobTitle:TextInputEditText
    private lateinit var start:TextInputEditText
    private lateinit var end:TextInputEditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var hospitalityExpertAdapter: HospitalityExpertAdapter
    private lateinit var dialogRole:Dialog
    private lateinit var searchBar:EditText
    private var originalData: List<CompanyDatacClass> = emptyList()


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


        jobTitle = view.findViewById(R.id.dob_et)
        start = view.findViewById(R.id.et_session_Start)
        end = view.findViewById(R.id.et_session_End)

        val submit = view.findViewById<CardView>(R.id.card_job_next)

        submit.setOnClickListener {
            setJobDetail()
        }

    }

    private fun setJobDetail() {
        val title = jobTitle.text.toString()
        val type = eTypeET.text.toString()
        val company = recentCoET.text.toString()
        val start = start.text.toString()
        val end = end.text.toString()

        val data = HospitalityexpertData(HospitalityexpertData.JobDetail(type,title,company,start,end))

        val update = RetrofitBuilder.profileCompletion.setHospitalityExpertDetails("Oo7PCzo0-",data)

        update.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context, DashBoardActivity::class.java))
                }else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
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

        getCompany()

        searchBar = dialogRole.findViewById(R.id.searchCompany)

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
                    originalData = response.toList()
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
                                item.company_name.contains(p0.toString(), ignoreCase = true)
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