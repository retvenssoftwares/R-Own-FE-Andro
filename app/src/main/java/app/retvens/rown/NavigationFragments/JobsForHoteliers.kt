package app.retvens.rown.NavigationFragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.NavigationFragments.jobforvendors.ExploreRequestingChildFragmnet
import app.retvens.rown.NavigationFragments.jobforvendors.JobPostActivity
import app.retvens.rown.NavigationFragments.jobforvendors.JobPostedChildFragmnet
import app.retvens.rown.NavigationFragments.jobforvendors.explore_employees_fragment
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetJobFilter


class JobsForHoteliers : Fragment(), BottomSheetJobFilter.OnBottomJobClickListener {

    lateinit var postAJob : CardView

    lateinit var cardJobPosted : CardView
    lateinit var cardExploreRequestedJob : CardView
    lateinit var cardExploreEmployees: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jobs_for_hoteliers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardJobPosted = view.findViewById(R.id.card_job_posted)
        cardExploreRequestedJob = view.findViewById(R.id.card_explore_request_job)
        cardExploreEmployees = view.findViewById(R.id.card_explore_employees)

        val welcome = view.findViewById<TextView>(R.id.welcome_name)
        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()
        welcome.text = "Welcome, $profileName!"

        cardJobPosted.setOnClickListener {
            cardJobPosted.setCardBackgroundColor(Color.parseColor("#ADD134"))
            cardExploreRequestedJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            cardExploreEmployees.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            val childFragment: Fragment = JobPostedChildFragmnet()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_jobs_fragments_container, childFragment).commit()
        }

        cardExploreRequestedJob.setOnClickListener {
            cardJobPosted.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            cardExploreRequestedJob.setCardBackgroundColor(Color.parseColor("#ADD134"))
            cardExploreEmployees.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            val childFragment: Fragment = ExploreRequestingChildFragmnet()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_jobs_fragments_container, childFragment).commit()
        }

        cardExploreEmployees.setOnClickListener {
            cardJobPosted.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            cardExploreRequestedJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            cardExploreEmployees.setCardBackgroundColor(Color.parseColor("#ADD134"))

            val childFragment: Fragment = explore_employees_fragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_jobs_fragments_container, childFragment).commit()
        }

        postAJob = view.findViewById(R.id.postAJob)
        postAJob.setOnClickListener {
            Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, JobPostActivity::class.java) )
        }

        view.findViewById<ImageView>(R.id.filter).setOnClickListener {
            val bottomSheet = BottomSheetJobFilter()
            val fragManager = (activity as FragmentActivity).supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobFilter.Job_TAG)}
            bottomSheet.setOnJobClickListener(this)
        }


        val childFragment: Fragment = JobPostedChildFragmnet()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_jobs_fragments_container, childFragment).commit()

    }
    private fun showBottomJobType(jobTypeText: TextView) {

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
            jobTypeText.text = "Select one"
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.fullTime).setOnClickListener {
            jobTypeText.text = "Full-Time"
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.partTime).setOnClickListener {
            jobTypeText.text = "Part-Time"
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.selfE).setOnClickListener {
            jobTypeText.text = "Self-Employed"
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Freelancer).setOnClickListener {
            jobTypeText.text = "Freelancer"
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Internship).setOnClickListener {
            jobTypeText.text = "Internship"
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Trainee).setOnClickListener {
            jobTypeText.text = "Trainee"
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

    override fun bottomJobClick(jobFrBo: String) {

    }
}