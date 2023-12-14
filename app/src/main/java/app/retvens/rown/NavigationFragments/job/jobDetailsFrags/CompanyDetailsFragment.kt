package app.retvens.rown.NavigationFragments.job.jobDetailsFrags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.retvens.rown.R


class CompanyDetailsFragment(val companyDetailsRes:String,val companyWebsitesRes:String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_company_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val companyDetails=view.findViewById<TextView>(R.id.companyID)
        val companyWeb=view.findViewById<TextView>(R.id.webTextView)

        companyDetails.text=companyDetailsRes
        companyWeb.text=companyWebsitesRes

    }
}