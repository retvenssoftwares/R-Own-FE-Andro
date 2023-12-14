package app.retvens.rown.NavigationFragments.job.jobDetailsFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.retvens.rown.R

class DescriptionFragment(val jobDescription:String,val skillsReq:String) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val description = view.findViewById<TextView>(R.id.description_job)
        val skills = view.findViewById<TextView>(R.id.skills_job)

        description.text = jobDescription
        skills.text = skillsReq

    }
}