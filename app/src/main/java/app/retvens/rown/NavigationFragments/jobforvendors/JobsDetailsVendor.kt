package app.retvens.rown.NavigationFragments.jobforvendors

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.retvens.rown.R

class JobsDetailsVendor : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var recyclerView: RecyclerView
    private lateinit var jobsDetailsVendorAdapter: JobsDetailsVendorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs_details_vendor)

        val cardDescrip = findViewById<RelativeLayout>(R.id.descriptionCard)
        val cardSkill = findViewById<RelativeLayout>(R.id.skillCard)

        val description = findViewById<TextView>(R.id.textdes)
        val skill = findViewById<TextView>(R.id.textskill)

        val view1 = findViewById<RelativeLayout>(R.id.descriptionView)
        val view2 = findViewById<RelativeLayout>(R.id.skillView)


        cardDescrip.setOnClickListener {
           cardDescrip.setBackgroundColor(ContextCompat.getColor(this,R.color.card_background_color2))
            description.setTextColor(ContextCompat.getColor(this,R.color.white))

            cardSkill.setBackgroundColor(ContextCompat.getColor(this,R.color.card_background_color))
            skill.setTextColor(ContextCompat.getColor(this,R.color.textcolor))

            view1.visibility = View.VISIBLE
            view2.visibility = View.GONE
        }

        cardSkill.setOnClickListener {
            cardDescrip.setBackgroundColor(ContextCompat.getColor(this,R.color.card_background_color))
            description.setTextColor(ContextCompat.getColor(this,R.color.textcolor))

            cardSkill.setBackgroundColor(ContextCompat.getColor(this,R.color.card_background_color2))
            skill.setTextColor(ContextCompat.getColor(this,R.color.white))

            view2.visibility = View.VISIBLE
            view1.visibility = View.GONE
        }

        val title = findViewById<TextView>(R.id.detail_job_designation)
        val location = findViewById<TextView>(R.id.detail_job_location)
        val type = findViewById<TextView>(R.id.details_jobs_type)
        val descriptiontext = findViewById<TextView>(R.id.JobsDescription)
        val skillreq = findViewById<TextView>(R.id.JobsSkills)
        val salary = findViewById<TextView>(R.id.details_salary)

        title.text = intent.getStringExtra("title")
        type.text = intent.getStringExtra("type")
        descriptiontext.text = intent.getStringExtra("description")
        skillreq.text = intent.getStringExtra("skills")
        salary.text = intent.getStringExtra("salary")
        val company = intent.getStringExtra("company")
        val locat = intent.getStringExtra("location")

        location.setText("$company.$locat")



    }
}