package app.retvens.rown.NavigationFragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.NavigationFragments.job.JobExploreFragment
import app.retvens.rown.NavigationFragments.job.RequestForJobFragment
import app.retvens.rown.R

class JobFragment : Fragment(){

    lateinit var exploreJob :CardView
    lateinit var requestJob :CardView
    lateinit var appliedJob :CardView

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


            val childFragment: Fragment = JobExploreFragment()
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_j_fragments_container, childFragment).commit()
        }
    }
}