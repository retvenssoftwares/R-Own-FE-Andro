package app.retvens.rown.NavigationFragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.retvens.rown.NavigationFragments.job.ApplyForJobFragment
import app.retvens.rown.NavigationFragments.job.JobExploreFragment
import app.retvens.rown.NavigationFragments.job.RequestForJobFragment
import app.retvens.rown.NavigationFragments.job.savedJobs.SavedJobsActivity
import app.retvens.rown.NavigationFragments.job.savedJobs.SavedJobsData
import app.retvens.rown.NavigationFragments.jobforvendors.ExploreRequestingChildFragmnet
import app.retvens.rown.NavigationFragments.jobforvendors.JobPostedChildFragmnet
import app.retvens.rown.NavigationFragments.jobforvendors.explore_employees_fragment
import app.retvens.rown.R
import app.retvens.rown.utils.profileCompletionStatus

class JobFragment : Fragment(){

    lateinit var exploreJob :CardView
    lateinit var requestJob :CardView
    lateinit var appliedJob :CardView

    lateinit var ll_menu :HorizontalScrollView
    lateinit var child_j_fragments_container :FrameLayout

    lateinit var savedJobs :ImageView
    lateinit var nothing :ImageView

    var selectedFrag = 0

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

        ll_menu = view.findViewById(R.id.ll_menu)
        child_j_fragments_container = view.findViewById(R.id.child_j_fragments_container)

        nothing = view.findViewById<ImageView>(R.id.nothing)
        savedJobs = view.findViewById<ImageView>(R.id.savedJobs)

        if (profileCompletionStatus == "100"){
            nothing.visibility = View.GONE
            savedJobs.visibility = View.VISIBLE
            child_j_fragments_container.visibility = View.VISIBLE
            ll_menu.visibility = View.VISIBLE
        } else {
            nothing.visibility = View.VISIBLE
            savedJobs.visibility = View.GONE
            child_j_fragments_container.visibility = View.GONE
            ll_menu.visibility = View.GONE
        }

        savedJobs.setOnClickListener {
            startActivity(Intent(context, SavedJobsActivity::class.java))
        }

        val welcome = view.findViewById<TextView>(R.id.welcome_name)
        val sharedPreferencesName = context?.getSharedPreferences("SaveFullName", AppCompatActivity.MODE_PRIVATE)
        val profileName = sharedPreferencesName?.getString("full_name", "").toString()
        welcome.text = "Welcome, $profileName!"

        val childFragment: Fragment = JobExploreFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_j_fragments_container, childFragment).commit()

        /*-----------------------EXPLORE FOR A JOB--------------------------------*/
        /*-----------------------EXPLORE FOR A JOB--------------------------------*/

        exploreJob.setOnClickListener {
            exploreJob.setCardBackgroundColor(Color.parseColor("#ADD134"))
            requestJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            appliedJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            selectedFrag = 0

            val childFragment: Fragment = JobExploreFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_j_fragments_container, childFragment).commit()
        }

        /*-----------------------REQUEST FOR A JOB--------------------------------*/
        /*-----------------------REQUEST FOR A JOB--------------------------------*/
        requestJob.setOnClickListener {
            exploreJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            requestJob.setCardBackgroundColor(Color.parseColor("#ADD134"))
            appliedJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))

            selectedFrag = 1

            val childFragment: Fragment = RequestForJobFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_j_fragments_container, childFragment).commit()
        }

        /*----------------------------APPLY FOR A JOB-----------------------------*/
        /*----------------------------APPLY FOR A JOB-----------------------------*/
        appliedJob.setOnClickListener {
            exploreJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            requestJob.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
            appliedJob.setCardBackgroundColor(Color.parseColor("#ADD134"))

            selectedFrag = 2

            val childFragment: Fragment = ApplyForJobFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_j_fragments_container, childFragment).commit()
        }

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)

        refresh.setOnRefreshListener {
            if (selectedFrag == 0) {
                val childFragment: Fragment = JobExploreFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_j_fragments_container, childFragment).commit()
            } else if (selectedFrag == 1) {
                val childFragment: Fragment = RequestForJobFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_j_fragments_container, childFragment).commit()
            } else if (selectedFrag == 2) {
                val childFragment: Fragment = ApplyForJobFragment()
                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.child_j_fragments_container, childFragment).commit()
            }
            refresh.isRefreshing = false
        }


    }
}