package app.retvens.rown.NavigationFragments.job.jobDetailsFrags

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.retvens.rown.R
import com.bumptech.glide.Glide


class ActivitiesFragment(
    context: Context,
    val employeeNameRes: String,
    val employeeRoleRes: String,
    val employeeProfileRes: String
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val employeeProfile=view.findViewById<ImageView>(R.id.employee_profile_explore)
        val employeeName=view.findViewById<TextView>(R.id.employee_name_explore)
        val employee_role=view.findViewById<TextView>(R.id.employee_role)

        employeeName.text=employeeNameRes
        employee_role.text=employeeRoleRes

        context?.let {
            Glide.with(it).load(employeeProfileRes).placeholder(R.drawable.png_blog)
                .into(employeeProfile)
        }

    //        context?.let { Glide.with(it).load(employeeProfileRes).into(employeeProfile) }

    }
}