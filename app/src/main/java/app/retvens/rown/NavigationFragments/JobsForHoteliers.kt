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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.NavigationFragments.jobforvendors.ExploreRequestingChildFragmnet
import app.retvens.rown.NavigationFragments.jobforvendors.JobPostActivity
import app.retvens.rown.NavigationFragments.jobforvendors.JobPostedChildFragmnet
import app.retvens.rown.NavigationFragments.jobforvendors.explore_employees_fragment
import app.retvens.rown.R


class JobsForHoteliers : Fragment() {

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
            val dialogRole = Dialog(requireContext())
            dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogRole.setContentView(R.layout.bottom_sheet_job_filter)
            dialogRole.setCancelable(true)

            dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
            dialogRole.window?.setGravity(Gravity.BOTTOM)
            dialogRole.show()
        }


//        val fragment = JobPostedChildFragmnet
//        val transaction = childFragmentManager.beginTransaction()
//        transaction.replace(R.id.child_jobs_fragments_container,fragment)
//        transaction.commit()
        val childFragment: Fragment = JobPostedChildFragmnet()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_jobs_fragments_container, childFragment).commit()

    }
}