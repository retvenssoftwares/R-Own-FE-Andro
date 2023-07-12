package app.retvens.rown.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.BasicInformationAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.R
import app.retvens.rown.utils.setupFullHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetJobTitle : BottomSheetDialogFragment() {

    var mListener: OnBottomJobTitleClickListener ? = null
    fun setOnJobTitleClickListener(listener: OnBottomJobTitleClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetJobTitle? {
        return BottomSheetJobTitle()
    }
    interface OnBottomJobTitleClickListener{
        fun bottomJobTitleClick(jobTitleFrBo : String)
    }

    companion object {
        const val Job_Title_TAG = "BottomSheetDailog"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    private lateinit var recyclerView:RecyclerView
    private lateinit var basicInformationAdapter: BasicInformationAdapter
    private lateinit var serarchBar: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_job_title, container, false)
    }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.jobs_recycler)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        serarchBar = view.findViewById(R.id.searchJob)

        getJobsList()
    }

    private fun getJobsList() {

        val getJob = RetrofitBuilder.profileCompletion.getJobTitle()

        getJob.enqueue(object : Callback<List<GetJobDataClass>?>,
            BasicInformationAdapter.OnJobClickListener {
            override fun onResponse(
                call: Call<List<GetJobDataClass>?>,
                response: Response<List<GetJobDataClass>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    val originalData = response.toList()
                    basicInformationAdapter = BasicInformationAdapter(requireContext(),response)
                    basicInformationAdapter.notifyDataSetChanged()
                    recyclerView.adapter = basicInformationAdapter

                    basicInformationAdapter.setOnJobClickListener(this)

                    serarchBar.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            val filterData = originalData.filter { item ->
                                item.job_title.contains(p0.toString(),ignoreCase = true)
                            }

                            basicInformationAdapter.updateData(filterData)
                        }

                        override fun afterTextChanged(p0: Editable?) {

                        }

                    })

                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetJobDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onJobClick(job: GetJobDataClass) {
                mListener?.bottomJobTitleClick(job.job_title)
                dismiss()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}