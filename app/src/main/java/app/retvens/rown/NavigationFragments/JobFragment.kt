package app.retvens.rown.NavigationFragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.explorejob.Applied
import app.retvens.rown.Dashboard.explorejob.AppliedJobAdapter
import app.retvens.rown.NavigationFragments.job.RecentJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCTC
import app.retvens.rown.bottomsheet.BottomSheetJobDepartment
import app.retvens.rown.bottomsheet.BottomSheetJobDesignation
import app.retvens.rown.bottomsheet.BottomSheetJobFilter
import app.retvens.rown.bottomsheet.BottomSheetJobType
import app.retvens.rown.bottomsheet.BottomSheetLocation
import app.retvens.rown.bottomsheet.BottomSheetNoticePeriod
import app.retvens.rown.bottomsheet.BottomSheetRating
import com.google.android.material.textfield.TextInputEditText


class JobFragment : Fragment(), BottomSheetJobFilter.OnBottomJobClickListener,
    BottomSheetJobDepartment.OnBottomJobDepartmentClickListener,
    BottomSheetJobDesignation.OnBottomJobDesignationClickListener,
    BottomSheetJobType.OnBottomJobTypeClickListener,
    BottomSheetNoticePeriod.OnBottomNoticeClickListener,
    BottomSheetCTC.OnBottomCTCClickListener,
    BottomSheetLocation.OnBottomLocationClickListener{

    lateinit var exploreJob :CardView
    lateinit var requestJob :CardView
    lateinit var appliedJob :CardView

    lateinit var exploreJobLayout :LinearLayout
    lateinit var requestJobLayout :LinearLayout
    lateinit var appliedJobLayout :LinearLayout

    /*-----------------------EXPLORE FOR A JOB--------------------------------*/
    /*-----------------------EXPLORE FOR A JOB--------------------------------*/

    lateinit var suggestedRecycler : RecyclerView
    lateinit var recentJobRecycler : RecyclerView
    lateinit var filter : ImageView

    /*-----------------------REQUEST FOR A JOB--------------------------------*/
    /*-----------------------REQUEST FOR A JOB--------------------------------*/
    lateinit var selectDepartmentET : TextInputEditText
    lateinit var selectDesignationET : TextInputEditText
    lateinit var selectJobEmploymentET : TextInputEditText
    lateinit var selectJobLocationET : TextInputEditText
    lateinit var selectNoticeET : TextInputEditText
    lateinit var expectedCTCeET : TextInputEditText

    /*----------------------------APPLY FOR A JOB-----------------------------*/
    /*----------------------------APPLY FOR A JOB-----------------------------*/
    lateinit var appliedRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploreJob = view.findViewById(R.id.card_job_explore)
        requestJob = view.findViewById(R.id.card_request_job)
        appliedJob = view.findViewById(R.id.card_applied_job)

        exploreJobLayout = view.findViewById(R.id.ll_job_explore)
        requestJobLayout = view.findViewById(R.id.ll_request_job)
        appliedJobLayout = view.findViewById(R.id.ll_applied_job)

        /*-----------------------EXPLORE FOR A JOB--------------------------------*/
        /*-----------------------EXPLORE FOR A JOB--------------------------------*/

        exploreJob.setOnClickListener {
            exploreJob.setCardBackgroundColor(Color.parseColor("#ADD134"))
            requestJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            appliedJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobLayout.visibility = View.VISIBLE
            requestJobLayout.visibility = View.GONE
            appliedJobLayout.visibility = View.GONE
        }
        exploreAJob()

        /*-----------------------REQUEST FOR A JOB--------------------------------*/
        /*-----------------------REQUEST FOR A JOB--------------------------------*/
        requestJob.setOnClickListener {
            exploreJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            requestJob.setCardBackgroundColor(Color.parseColor("#ADD134"))
            appliedJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            exploreJobLayout.visibility = View.GONE
            appliedJobLayout.visibility = View.GONE
            requestJobLayout.visibility = View.VISIBLE
        }
        requestAJob()

        /*----------------------------APPLY FOR A JOB-----------------------------*/
        /*----------------------------APPLY FOR A JOB-----------------------------*/
        appliedJob.setOnClickListener {
            exploreJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            requestJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            appliedJob.setCardBackgroundColor(Color.parseColor("#ADD134"))
            appliedJobLayout.visibility = View.VISIBLE
            exploreJobLayout.visibility = View.GONE
            requestJobLayout.visibility = View.GONE
        }
        appliedForAJob()
    }

    /*-----------------------EXPLORE FOR A JOB--------------------------------*/
    /*-----------------------EXPLORE FOR A JOB--------------------------------*/
    private fun exploreAJob() {

        /*-----------------------EXPLORE FOR A JOB--------------------------------*/
        /*-----------------------EXPLORE FOR A JOB--------------------------------*/

        filter = requireView().findViewById(R.id.filter_user_jobs)
        filter.setOnClickListener {
            val bottomSheet = BottomSheetJobFilter()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobFilter.Job_TAG)}
            bottomSheet.setOnJobClickListener(this)
        }

        suggestedRecycler = requireView().findViewById(R.id.suggested_recycler)
        suggestedRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        suggestedRecycler.setHasFixedSize(true)

        val listSuggeJobs = mutableListOf<SuggestedJobData>()
        listSuggeJobs.add(SuggestedJobData("Android Devloper"))
        listSuggeJobs.add(SuggestedJobData("UI Devloper"))
        listSuggeJobs.add(SuggestedJobData("Devloper"))
        listSuggeJobs.add(SuggestedJobData("Android Devloper"))
        listSuggeJobs.add(SuggestedJobData("UI Devloper"))
        listSuggeJobs.add(SuggestedJobData("Devloper"))

        val suggestedJobAdapter = SuggestedJobAdapter(listSuggeJobs, requireContext())
        suggestedRecycler.adapter = suggestedJobAdapter
        suggestedJobAdapter.notifyDataSetChanged()

        /*-----------------------EXPLORE FOR A JOB--------------------------------*/
        /*-----------------------EXPLORE FOR A JOB--------------------------------*/
        recentJobRecycler = requireView().findViewById(R.id.recent_job_recycler)
        recentJobRecycler.layoutManager = LinearLayoutManager(context)
        recentJobRecycler.setHasFixedSize(true)

        val recentJobAdapter = RecentJobAdapter(listSuggeJobs, requireContext())
        recentJobRecycler.adapter = recentJobAdapter
        recentJobAdapter.notifyDataSetChanged()
    }

    /*-----------------------REQUEST FOR A JOB--------------------------------*/
    /*-----------------------REQUEST FOR A JOB--------------------------------*/
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
    override fun bottomJobTypeClick(jobTypeFrBo: String) {
        selectJobEmploymentET.setText(jobTypeFrBo)
    }

    override fun bottomNoticeClick(noticeFrBo: String) {
        selectNoticeET.setText(noticeFrBo)
    }

    override fun bottomCTCClick(CTCFrBo: String) {
        expectedCTCeET.setText(CTCFrBo)
    }

/*----------------------------APPLY FOR A JOB-----------------------------*/
/*----------------------------APPLY FOR A JOB-----------------------------*/
    private fun appliedForAJob() {
        appliedRecyclerView = requireView().findViewById(R.id.applied_recycler)
        appliedRecyclerView.layoutManager = LinearLayoutManager(context)
        appliedRecyclerView.setHasFixedSize(true)

        val listJobs = mutableListOf<Applied>()
        listJobs.add(Applied("Android Devloper", ""))
        listJobs.add(Applied("UI Devloper", ""))
        listJobs.add(Applied("Devloper", ""))
        listJobs.add(Applied("Android Devloper", ""))
        listJobs.add(Applied("UI Devloper", ""))
        listJobs.add(Applied("Devloper", ""))
        listJobs.add(Applied("Android Devloper", ""))
        listJobs.add(Applied("UI Devloper", ""))
        listJobs.add(Applied("Devloper", ""))

        val appliedJobAdapter = AppliedJobAdapter(requireContext(), listJobs)
        appliedRecyclerView.adapter = appliedJobAdapter
        appliedJobAdapter.notifyDataSetChanged()
    }

    override fun bottomJobClick(jobFrBo: String) {

    }

    override fun bottomJobDepartmentClick(jobDepartFrBo: String) {

    }

    override fun bottomJobDesignationClick(jobDesignationFrBo: String) {

    }

    override fun bottomLocationClick(LocationFrBo: String) {

    }

}