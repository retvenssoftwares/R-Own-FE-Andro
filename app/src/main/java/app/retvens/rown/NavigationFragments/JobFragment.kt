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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.Dashboard.explorejob.Applied
import app.retvens.rown.Dashboard.explorejob.AppliedJobAdapter
import app.retvens.rown.NavigationFragments.job.RecentJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobAdapter
import app.retvens.rown.NavigationFragments.job.SuggestedJobData
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText


class JobFragment : Fragment() {

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

            val dialogRole = Dialog(requireContext())
            dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogRole.setContentView(R.layout.bottom_sheet_job_filter)
            dialogRole.setCancelable(true)

            dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialogRole.window?.setGravity(Gravity.BOTTOM)
            dialogRole.show()

            val jt= dialogRole.findViewById<RelativeLayout>(R.id.filter_job_type)
            jt.setOnClickListener {
                showBottomJobType()
            }
            val fL = dialogRole.findViewById<RelativeLayout>(R.id.filter_location)
            fL.setOnClickListener {
                openLocationSheet()
            }

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
            openBottomDepartment()
        }
        selectDesignationET = requireView().findViewById(R.id.select_designation_et)
        selectDesignationET.setOnClickListener {
            openBottomDesignation()
        }
        selectJobEmploymentET = requireView().findViewById(R.id.select_job_employment_et)
        selectJobEmploymentET.setOnClickListener {
            showBottomJobType()
        }
        selectJobLocationET = requireView().findViewById(R.id.select_job_location_et)
        selectJobLocationET.setOnClickListener {
            openLocationSheet()
        }
        selectNoticeET = requireView().findViewById(R.id.select_notice_et)
        selectNoticeET.setOnClickListener {
            showBottomNotice()
        }
        expectedCTCeET = requireView().findViewById(R.id.select_ctc_et)
        expectedCTCeET.setOnClickListener {
            showBottomCTC()
        }
    }
    private fun openBottomDepartment() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_select_department)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()



    }
    private fun openBottomDesignation() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_select_designation)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()



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
            selectJobEmploymentET.setText("Select one -")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.fullTime).setOnClickListener {
            selectJobEmploymentET.setText("Full-Time")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.partTime).setOnClickListener {
            selectJobEmploymentET.setText("Part-Time")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.selfE).setOnClickListener {
            selectJobEmploymentET.setText("Self-Employed")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Freelancer).setOnClickListener {
            selectJobEmploymentET.setText("Freelancer")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Internship).setOnClickListener {
            selectJobEmploymentET.setText("Internship")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Trainee).setOnClickListener {
            selectJobEmploymentET.setText("Trainee")
            dialogRole.dismiss()
        }
    }
    private fun openLocationSheet() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_location)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()


    }
    private fun showBottomNotice() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_notice_period)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
            selectNoticeET.setText("Select one -")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.no).setOnClickListener {
            selectNoticeET.setText("No")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.one_month_less).setOnClickListener {
            selectNoticeET.setText("< 1 Month")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.one_month).setOnClickListener {
            selectNoticeET.setText("1 Month")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.two_month).setOnClickListener {
            selectNoticeET.setText("2 Month")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.three_month).setOnClickListener {
            selectNoticeET.setText("3 Month")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.more_three_month).setOnClickListener {
            selectNoticeET.setText(">3 Month")
            dialogRole.dismiss()
        }
    }
    private fun showBottomCTC() {

        val dialogRole = Dialog(requireContext())
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_expected_ctc)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
            expectedCTCeET.setText("Select one -")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.one_to_three).setOnClickListener {
            expectedCTCeET.setText("1-3 Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.three_to_six).setOnClickListener {
            expectedCTCeET.setText("3-6 Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.six_to_ten).setOnClickListener {
            expectedCTCeET.setText("6-10 Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.ten_to_fif).setOnClickListener {
            expectedCTCeET.setText("10-15  Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.fif_to_tf).setOnClickListener {
            expectedCTCeET.setText("15-25 Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.more_than_tf).setOnClickListener {
            expectedCTCeET.setText(">25 Lakhs/p.a")
            dialogRole.dismiss()
        }
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

}