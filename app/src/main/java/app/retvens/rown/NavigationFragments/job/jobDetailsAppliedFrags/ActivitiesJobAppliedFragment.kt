package app.retvens.rown.NavigationFragments.job.jobDetailsAppliedFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.DataCollections.JobsCollection.CandidateDataClass
import app.retvens.rown.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivitiesJobAppliedFragment(val appliedStatus:String) : Fragment() {

    private lateinit var currentStaus:ImageView
    private lateinit var finalStatus:ImageView
    private lateinit var currentMasage:TextView
    private lateinit var finalMasage:TextView
    private lateinit var pointTwoB:ImageView
    private lateinit var pointThreeB:ImageView
    private lateinit var pointFourB:ImageView
    private lateinit var pointFiveB:ImageView

    private lateinit var pointOneC:ImageView
    private lateinit var pointTwoC:ImageView
    private lateinit var pointThreeC:ImageView
    private lateinit var pointFourC:ImageView
    private lateinit var pointFiveC:ImageView
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
        currentMasage = view.findViewById<TextView>(R.id.textViewCurrentStatus)
        finalMasage = view.findViewById<TextView>(R.id.textViewFinalStatus)

        pointTwoB=view.findViewById(R.id.pointTwoB)
        pointThreeB=view.findViewById(R.id.pointThreeB)
        pointFourB=view.findViewById(R.id.pointFourB)
        pointFiveB=view.findViewById(R.id.pointFiveB)

        pointOneC=view.findViewById(R.id.pointOneC)
        pointTwoC=view.findViewById(R.id.pointTwoC)
        pointThreeC=view.findViewById(R.id.pointThreeC)
        pointFourC=view.findViewById(R.id.pointFourC)
        pointFiveC=view.findViewById(R.id.pointFiveC)

        if (appliedStatus == "On Hold"){
            currentStaus.setImageResource(R.drawable.svg_tick)
        }else if (appliedStatus == "Scheduled"){
            currentStaus.setImageResource(R.drawable.svg_tick)
            currentMasage.text=appliedStatus
            finalMasage.text=""

           setThiedPointStyle()

        }else if (appliedStatus == "Criteria Doesn’t Match") {
            currentStaus.setImageResource(R.drawable.svg_tick)
            currentMasage.text=appliedStatus
            finalMasage.text=""

            setThiedPointStyle()

        }else if (appliedStatus == "Hired"){
            currentStaus.setImageResource(R.drawable.svg_tick)
            finalStatus.setImageResource(R.drawable.svg_tick)
            currentMasage.text=appliedStatus
            finalMasage.text=appliedStatus

           setFourPointStyle()

        }else if (appliedStatus == "Rejected"){
            currentStaus.setImageResource(R.drawable.svg_cross_red)
            finalStatus.setImageResource(R.drawable.svg_cross_red)
            currentMasage.text=appliedStatus
            finalMasage.text=appliedStatus

            setFourPointStyle()

        }else if (appliedStatus == "Promoted to further round"){
            currentStaus.setImageResource(R.drawable.svg_cross)
            finalStatus.setImageResource(R.drawable.svg_cross)

            setFourPointStyle()

        }


//        val status = arguments?.getString("AppID")
//        getStatus(status)

    }

    fun setThiedPointStyle(){
        pointTwoB.setImageResource(R.drawable.svg_point_s)
        pointThreeB.setImageResource(R.drawable.svg_point_s)
        pointFourB.setImageResource(R.drawable.svg_point_s)
        pointFiveB.setImageResource(R.drawable.svg_point_s)
        pointOneC.setImageResource(R.drawable.svg_point_s)
    }
    fun setFourPointStyle()
    {
        setThiedPointStyle()
        pointTwoC.setImageResource(R.drawable.svg_point_s)
        pointThreeC.setImageResource(R.drawable.svg_point_s)
        pointFourC.setImageResource(R.drawable.svg_point_s)
        pointFiveC.setImageResource(R.drawable.svg_point_s)
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
//
//            }
//        })
//
//    }
}