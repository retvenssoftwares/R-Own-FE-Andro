package app.retvens.rown.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.profileCompletion.frags.adapter.DesignationFragmentAdapter
import app.retvens.rown.DataCollections.ProfileCompletion.GetJobDataClass
import app.retvens.rown.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BottomSheetJobDesignation : BottomSheetDialogFragment() {

    var mListener: OnBottomJobDesignationClickListener ? = null
    fun setOnJobDesignationClickListener(listener: OnBottomJobDesignationClickListener?){
        mListener = listener
    }
    fun newInstance(): BottomSheetJobDesignation? {
        return BottomSheetJobDesignation()
    }
    interface OnBottomJobDesignationClickListener{
        fun bottomJobDesignationClick(jobDesignationFrBo : String)
    }


    companion object {
        const val Job_Designation_TAG = "BottomSheetDailog"
    }

    lateinit var shimmerLayout: LinearLayout
    lateinit var recyclerView : RecyclerView
    lateinit var designationFragmentAdapter : DesignationFragmentAdapter
    private lateinit var searchDesignation : EditText

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_select_designation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchDesignation = view.findViewById(R.id.search_designation)
        shimmerLayout = view.findViewById(R.id.shimmer_layout_tasks)

        recyclerView = view.findViewById(R.id.designation_recycler)
        //recyclerView. //recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

//        val jt= view.findViewById<LinearLayout>(R.id.hotel_owner)
//        jt.setOnClickListener {
//            mListener?.bottomJobDesignationClick("Assistance")
//            dismiss()
//        }
//        val jtm= view.findViewById<LinearLayout>(R.id.hos_expert)
//        jtm.setOnClickListener {
//            mListener?.bottomJobDesignationClick("Associate")
//            dismiss()
//        }
//        val jtmd= view.findViewById<LinearLayout>(R.id.vendor)
//        jtmd.setOnClickListener {
//            mListener?.bottomJobDesignationClick("Administrative Assistant")
//            dismiss()
//        }

        getDesignation()

    }

    private fun getDesignation() {

        val getDesignation = RetrofitBuilder.profileCompletion.getJobTitle()

        getDesignation.enqueue(object : Callback<List<GetJobDataClass>?>,
            DesignationFragmentAdapter.OnDesignationClickListener {
            override fun onResponse(
                call: Call<List<GetJobDataClass>?>,
                response: Response<List<GetJobDataClass>?>
            ) {
                if (response.isSuccessful && isAdded){
                    val response = response.body()!!
                    designationFragmentAdapter = DesignationFragmentAdapter(requireContext(),response)
                    designationFragmentAdapter.notifyDataSetChanged()
                    designationFragmentAdapter.setOnJobDesignationClickListener(this)
                    recyclerView.adapter = designationFragmentAdapter
                    shimmerLayout.visibility = View.GONE

                    searchDesignation.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            val original = response.toList()
                            val filter = original.filter { searchUser ->
                                searchUser.designation_name.contains(s.toString(),ignoreCase = true)
                            }
                            designationFragmentAdapter.searchDesignation(filter)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                }
                else{
                    Toast.makeText(requireContext(),response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GetJobDataClass>?>, t: Throwable) {
                Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

            override fun onDesignationClick(designation: String) {
                mListener?.bottomJobDesignationClick(designation)
                dismiss()
            }

        })

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
}