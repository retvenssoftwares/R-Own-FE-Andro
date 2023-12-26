package app.retvens.rown.NavigationFragments.job.jobDetailsAppliedFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.CandidateDataClass
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivitiesJobAppliedFragment : Fragment() {

    private lateinit var currentStaus:ImageView
    private lateinit var finalStatus:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities_job_applied, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val applied = view.findViewById<ImageView>(R.id.appliedjob)
        val viewed = view.findViewById<ImageView>(R.id.viewResume)
        currentStaus = view.findViewById<ImageView>(R.id.currentStatus)
        finalStatus = view.findViewById<ImageView>(R.id.finalStatus)

        val status = arguments?.getString("AppID")


//        getStatus(status)

    }

//    private fun getStatus(status: String?) {
//
//        val getStatus = RetrofitBuilder.jobsApis.getCandidate(status!!)
//
//        getStatus.enqueue(object : Callback<List<CandidateDataClass>?> {
//            override fun onResponse(
//                call: Call<List<CandidateDataClass>?>,
//                response: Response<List<CandidateDataClass>?>
//            ) {
//                if (response.isSuccessful){
//                    val response = response.body()!!
//                    val status = response.get(0).status
//                    if (status == "On Hold"){
//                        currentStaus.setImageResource(R.drawable.svg_tick)
//                    }else if (status == "Scheduled"){
//                        currentStaus.setImageResource(R.drawable.svg_tick)
//                    }else if (status == "Criteria Doesn’t Match") {
//                        currentStaus.setImageResource(R.drawable.svg_tick)
//                    }else if (status == "Hired"){
//                        currentStaus.setImageResource(R.drawable.svg_tick)
//                        finalStatus.setImageResource(R.drawable.svg_tick)
//                    }else if (status == "Rejected"){
//                        currentStaus.setImageResource(R.drawable.svg_tick)
//                        finalStatus.setImageResource(R.drawable.svg_tick)
//                    }else if (status == "Promoted to further round"){
//                        currentStaus.setImageResource(R.drawable.svg_tick)
//                        finalStatus.setImageResource(R.drawable.svg_tick)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<CandidateDataClass>?>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//
//    }
}